package database_interface;

import java.sql.*;
import java.util.ArrayList;

import exceptions.IncompleteInformationException;
import exceptions.InvalidAuthenticationException;
import exceptions.ObjectDoesNotExistException;
import exceptions.UniqueColumnValueAlreadyExists;
import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;
import models.AcceptedArticle;
import models.Authorship;
import models.Edition;
import models.Journal;
import models.JournalEditor;
import models.Submission;
import models.User;
import models.Volume;

public class DataAccessController implements DatabaseInterface {

    private Connection connection;
    private PreparedStatement statement;
    private String databaseURL;
    private String databaseUser;
    private String databaseUserPassword;

    public DataAccessController(DatabaseConstants connectionInfo) {
        this.databaseURL = connectionInfo.getDatabaseURL();
        this.databaseUser = connectionInfo.getDatabaseUser();
        this.databaseUserPassword = connectionInfo.getDatabaseUserPassword();
    }

    private void openConnection() throws SQLException {
        connection = DriverManager.getConnection(databaseURL, databaseUser, databaseUserPassword);
    }

    private void closeConnection() throws SQLException {
        if (connection != null)
            connection.close();
        if (statement != null)
            statement.close();
    }

    private boolean userExists(User user) throws SQLException {
        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT EXISTS(SELECT * FROM Users WHERE email=?)";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, user.getEmail());

            res = statement.executeQuery();
            res.next();
            int result = res.getInt(1);

            return result == 1;
        } finally {
            if (res != null)
                res.close();

            closeConnection();
        }
    }

    @Override
    public boolean registerBaseUser(User newUser)
            throws UserAlreadyExistsException, IncompleteInformationException, SQLException {

        if (userExists(newUser))
            throw new UserAlreadyExistsException(newUser.getEmail());

        if (newUser.getEmail() == null || newUser.getForenames() == null || newUser.getSurname() == null
                || newUser.getUniversity() == null
                || newUser.getPassword() == null
                || newUser.getEmail().equals("") || newUser.getForenames().equals("") || newUser.getSurname().equals("")
                || newUser.getUniversity().equals("")
                || newUser.getPassword().equals(""))
            throw new IncompleteInformationException();

        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "INSERT INTO Users (email, forenames, surname, university, password) VALUES (?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, newUser.getEmail());
            statement.setString(2, newUser.getForenames());
            statement.setString(3, newUser.getSurname());
            statement.setString(4, newUser.getUniversity());
            statement.setString(5, newUser.getPassword());


            int result = statement.executeUpdate();

            return result == 1;
        } finally {
            if (res != null)
                res.close();

            closeConnection();
        }

    }

    @Override
    public boolean changePassword(User user, String newPassword) throws UserDoesNotExistException, InvalidAuthenticationException, IncompleteInformationException, SQLException {
        if (newPassword.equals(""))
            throw new IncompleteInformationException();

        if (!userExists(user))
            throw new UserDoesNotExistException(user.getEmail());


        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "UPDATE Users SET password = ? WHERE\n" +
                    "    email = ?\n" +
                    "\tAND\n" +
                    "\tpassword = ?";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, newPassword);
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());


            int result = statement.executeUpdate();

            if (result > 1)
                throw new SQLException();

            if(result == 0)
                throw new InvalidAuthenticationException();

            return result == 1;
        } finally {
            if (res != null)
                res.close();

            closeConnection();
        }
    }

    @Override
    public boolean validCredentials(User user) throws SQLException, UserDoesNotExistException {

        if (!userExists(user))
            throw new UserDoesNotExistException(user.getEmail());

        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT EXISTS(SELECT * FROM Users WHERE email=? AND password=?)";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());

            res = statement.executeQuery();
            res.next();
            int result = res.getInt(1);

            return result == 1;
        } finally {
            if (res != null)
                res.close();

            closeConnection();
        }
    }

    @Override
    public boolean createJournal(Journal newJournal, User chiefEditor) throws UserDoesNotExistException,
            InvalidAuthenticationException, UniqueColumnValueAlreadyExists, SQLException {

        if (!validCredentials(chiefEditor))
            throw new InvalidAuthenticationException();

        if (!isUniqueJournal(newJournal))
            throw new UniqueColumnValueAlreadyExists();


        PreparedStatement statementTwo = null;
        try {
            openConnection();
            connection.setAutoCommit(false);

            // Insert the new journal into the table
            String sqlQuery = "INSERT INTO Journals (ISSN, name) \n" +
                    "\tVALUES (?, ?)";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, newJournal.getISSN());
            statement.setString(2, newJournal.getName());
            int res1 = statement.executeUpdate();

            if (res1 != 1) {
                connection.rollback();
                throw new SQLException();
            }

            // Insert the new editorship into the table with the new journal
            String sqlQueryTwo = "INSERT INTO JournalEditors (ISSN, email, isChief) \n" +
                    "\tVALUES (?, ?, true)";
            statementTwo = connection.prepareStatement(sqlQueryTwo);
            statementTwo.setString(1, newJournal.getISSN());
            statementTwo.setString(2, chiefEditor.getEmail());
            int res2 = statementTwo.executeUpdate();


            connection.commit();

            if(res1 == 1 && res2 == 1)
                return true;
            else{
                connection.rollback();
                return false;
            }

        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {

            closeConnection();

            if (statementTwo != null)
                statementTwo.close();
        }
    }

    /*
    Checks whether a journal with the given issn or name exists in the database
     */
    private boolean isUniqueJournal(Journal journal) throws SQLException {
        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT EXISTS(\n" +
                    "\tSELECT * FROM Journals WHERE \n" +
                    "\t\tISSN = ? OR\n" +
                    "\t\tname = ?\n" +
                    "\t)";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, journal.getISSN());
            statement.setString(2, journal.getName());

            res = statement.executeQuery();
            res.next();
            int result = res.getInt(1);

            return result == 0;

        } finally {
            if (res != null)
                res.close();

            closeConnection();
        }
    }

    @Override
    public boolean promoteUserToEditor(JournalEditor newEditor, JournalEditor journalChief)
            throws UserDoesNotExistException, InvalidAuthenticationException, SQLException, IncompleteInformationException {

        try {
            if (!newEditor.getIssn().equals(journalChief.getIssn())) {
                throw new InvalidAuthenticationException();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new IncompleteInformationException();
        }

        if (!userExists(newEditor)) {
            throw new UserDoesNotExistException(newEditor.getEmail());
        }

        journalChief = getEditorship(journalChief);

        if (journalChief == null || !journalChief.isChief()) {
            throw new InvalidAuthenticationException();
        }


        try {
            openConnection();

            String sqlQuery = "INSERT INTO JournalEditors (ISSN, email, isChief) VALUES\n" +
                    "\t(?,?,?)";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, newEditor.getIssn());
            statement.setString(2, newEditor.getEmail());
            statement.setBoolean(3, newEditor.isChief());

            int res = statement.executeUpdate();

            return res == 1;

        } finally {
            closeConnection();
        }
    }

    private JournalEditor getEditorship(JournalEditor editor) throws UserDoesNotExistException, SQLException {
        if (!userExists(editor))
            throw new UserDoesNotExistException(editor.getEmail());

        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT isChief FROM JournalEditors WHERE\n" +
                    "\tISSN = ? AND\n" +
                    "\temail = ?";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, editor.getIssn());
            statement.setString(2, editor.getEmail());

            res = statement.executeQuery();
            if (!res.next()) {
                // editorship does not exist
                return null;
            }

            int isChief = res.getInt(1);
            editor.setChief(isChief == 1);

            return editor;

        } finally {
            if (res != null)
                res.close();

            closeConnection();
        }
    }

    @Override
    public Submission submitArticle(Submission submission, User author)
            throws UserDoesNotExistException, InvalidAuthenticationException, SQLException, IncompleteInformationException {

        if (!validCredentials(author))
            throw new InvalidAuthenticationException();

        try{
            if(submission.getTitle().isEmpty() || submission.getSummary().isEmpty() || submission.getArticleContent().isEmpty()){
                throw new IncompleteInformationException();
            }
        } catch(NullPointerException e){
            e.printStackTrace();
            throw new IncompleteInformationException();
        }

        PreparedStatement statementTwo = null;
        try {
            openConnection();
            connection.setAutoCommit(false);

            // Insert the new submission into the table
            String sqlQuery = "INSERT INTO Submissions (title, abstract, draftLink) VALUES\n" +
                    "\t(?, ?, ?)";
            statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, submission.getTitle());
            statement.setString(2, submission.getSummary());
            statement.setString(3, submission.getArticleContent());
            int res1 = statement.executeUpdate();

            if (res1 != 1) {
                connection.rollback();
                throw new SQLException();
            }

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int newId = rs.getInt(1);
                submission.setSubmissionID(newId);
            }else{
                connection.rollback();
                throw new SQLException();
            }

            // Insert the new authorship into the table with the new submission
            String sqlQueryTwo = "INSERT INTO Authorships (email, submissionID, isMain) VALUES\n" +
                    "\t(?,?,?)";
            statementTwo = connection.prepareStatement(sqlQueryTwo);
            statementTwo.setString(1, author.getEmail());
            statementTwo.setInt(2, submission.getSubmissionID());
            statementTwo.setBoolean(3,true);
            int res2 = statementTwo.executeUpdate();

            connection.commit();

            if(res1 == 1 && res2 == 1){
                return submission;
            } else {
                connection.rollback();
                return null;
            }

        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {

            closeConnection();

            if (statementTwo != null)
                statementTwo.close();
        }
    }

    @Override
    public boolean addCoAuthor(Submission submission, Authorship newAuthor, Authorship mainAuthor)
            throws UserDoesNotExistException, ObjectDoesNotExistException, InvalidAuthenticationException, SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Volume createNextVolume(Journal journal, JournalEditor editor, int publicationYear)
            throws ObjectDoesNotExistException, InvalidAuthenticationException, SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Edition createNextEdition(Volume volume, JournalEditor editor, String publicationMonth)
            throws ObjectDoesNotExistException, InvalidAuthenticationException, SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<Journal> getAllJournals() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<Volume> getAllJournalVolumes(Journal journal) throws ObjectDoesNotExistException, SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<Edition> getAllVolumeEditions(Volume volume) throws ObjectDoesNotExistException, SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<AcceptedArticle> getallEditionArticles(Edition edition)
            throws ObjectDoesNotExistException, SQLException {
        // TODO Auto-generated method stub
        return null;
    }

}
