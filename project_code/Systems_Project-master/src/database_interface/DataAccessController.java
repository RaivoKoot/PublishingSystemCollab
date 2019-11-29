package database_interface;

import java.sql.*;
import java.time.Month;
import java.util.ArrayList;

import exceptions.*;
import models.*;
import org.omg.CORBA.DynAnyPackage.Invalid;

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

    public boolean userExists(User user) throws SQLException {
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
                || newUser.getUniversity() == null || newUser.getTitle() == null
                || newUser.getPassword() == null
                || newUser.getEmail().equals("") || newUser.getForenames().equals("") || newUser.getSurname().equals("")
                || newUser.getUniversity().equals("") || newUser.getTitle().equals("")
                || newUser.getPassword().equals(""))
            throw new IncompleteInformationException();

        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "INSERT INTO Users (email, title, forenames, surname, university, password) \n" +
                    "\tVALUES (?, ?, ?, ?, ?, ?)\n";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, newUser.getEmail());
            statement.setString(2, newUser.getTitle());
            statement.setString(3, newUser.getForenames());
            statement.setString(4, newUser.getSurname());
            statement.setString(5, newUser.getUniversity());
            statement.setString(6, newUser.getPassword());


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

            if (result == 0)
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

            if (res1 == 1 && res2 == 1)
                return true;
            else {
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

        if(!isChiefEditor(journalChief))
            throw new InvalidAuthenticationException();


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

    @Override
    public boolean makeChiefEditor(JournalEditor editor, JournalEditor chiefAuthentication) throws UserDoesNotExistException, IncompleteInformationException, InvalidAuthenticationException, SQLException {
        try {
            if (!editor.getIssn().equals(chiefAuthentication.getIssn())) {
                throw new InvalidAuthenticationException();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new IncompleteInformationException();
        }

        if (!userExists(editor)) {
            throw new UserDoesNotExistException(editor.getEmail());
        }

        if(!isChiefEditor(chiefAuthentication))
            throw new InvalidAuthenticationException();


        try {
            openConnection();

            String sqlQuery = "UPDATE JournalEditors SET isChief=1 WHERE\n" +
                    "\temail = ? AND ISSN = ?";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, editor.getEmail());
            statement.setString(2, editor.getIssn());

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
    public Article submitArticle(Article submission, User author)
            throws UserDoesNotExistException, InvalidAuthenticationException, SQLException, IncompleteInformationException {

        if (!validCredentials(author))
            throw new InvalidAuthenticationException();

        try {
            if (submission.getTitle().isEmpty() || submission.getSummary().isEmpty() || submission.getContent().isEmpty()
                    || submission.getIssn().isEmpty()) {
                throw new IncompleteInformationException();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new IncompleteInformationException();
        }

        PreparedStatement statementTwo = null;
        ResultSet rs = null;
        try {
            openConnection();
            connection.setAutoCommit(false);

            // Insert the new submission into the table
            String sqlQuery = "INSERT INTO Articles (title, abstract, content, ISSN) VALUES\n" +
                    "\t(?,?,?,?);";
            statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, submission.getTitle());
            statement.setString(2, submission.getSummary());
            statement.setString(3, submission.getContent());
            statement.setString(4, submission.getIssn());
            int res1 = statement.executeUpdate();

            if (res1 != 1) {
                connection.rollback();
                throw new SQLException();
            }

            rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int newId = rs.getInt(1);
                submission.setArticleID(newId);
            } else {
                connection.rollback();
                throw new SQLException();
            }

            // Insert the new authorship into the table with the new submission
            String sqlQueryTwo = "INSERT INTO Authorships (email, articleID, isMain) VALUES\n" +
                    "\t(?,?,?)";
            statementTwo = connection.prepareStatement(sqlQueryTwo);
            statementTwo.setString(1, author.getEmail());
            statementTwo.setInt(2, submission.getArticleID());
            statementTwo.setBoolean(3, true);
            int res2 = statementTwo.executeUpdate();

            connection.commit();

            if (res1 == 1 && res2 == 1) {
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

            if (rs != null)
                rs.close();
        }
    }

    @Override
    public boolean addCoAuthor(Article article, User newAuthor, User mainAuthor) throws UserDoesNotExistException, ObjectDoesNotExistException, InvalidAuthenticationException, SQLException {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean createNextVolume(Journal journal, JournalEditor editor, int publicationYear)
            throws ObjectDoesNotExistException, InvalidAuthenticationException, SQLException {

        if(!isChiefEditor(editor))
            throw new InvalidAuthenticationException();

        try {
            openConnection();

            String sqlQuery = "BEGIN;\n" +
                    "SELECT @id := IFNULL(MAX(volumeNum),0) + 1 FROM Volumes WHERE issn = ? FOR UPDATE;\n" +
                    "INSERT INTO Volumes\n" +
                    "     (volumeNum, issn, publicationYear)\n" +
                    "     VALUES\n" +
                    "     (@id, ?, ?);\n" +
                    "COMMIT;";

            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, journal.getISSN());
            statement.setString(2, journal.getISSN());
            statement.setInt(3, publicationYear);
            statement.execute();


            return true;

        } finally {

            closeConnection();
        }
    }

    private boolean isChiefEditor(JournalEditor editor) throws SQLException, InvalidAuthenticationException {
        JournalEditor editorCheck;
        try {
            editorCheck = getEditorship(editor);
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
            throw new InvalidAuthenticationException();
        }

        if (editorCheck == null || !editorCheck.isChief()) {
            throw new InvalidAuthenticationException();
        }

        return true;
    }

    @Override
    public Edition createNextEdition(Volume volume, JournalEditor editor, String publicationMonth)
            throws ObjectDoesNotExistException, InvalidAuthenticationException, VolumeFullException, SQLException {

        if(!isChiefEditor(editor))
            throw new InvalidAuthenticationException();

        ResultSet rs = null;
        try {
            openConnection();

            String sqlQuery = "SELECT IFNULL(MAX(editionNum),0) + 1 FROM Editions WHERE issn = ? AND volumeNum = ?;";

            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, volume.getISSN());
            statement.setInt(2, volume.getVolNum());
            rs = statement.executeQuery();

            if(!rs.next())
                throw new SQLException();

            int nextEditionNumber = rs.getInt(1);

            if(nextEditionNumber > 6){
                throw new VolumeFullException();
            }

            statement.close();

            String sqlQuery2 = "INSERT INTO Editions (editionNum, publicationMonth, volumeNum, ISSN) VALUES\n" +
                    "\t(?, ?, ?, ?)";


            statement = connection.prepareStatement(sqlQuery2);
            statement.setInt(1, nextEditionNumber);
            statement.setInt(2, Month.valueOf(publicationMonth).ordinal());
            statement.setInt(3, volume.getVolNum());
            statement.setString(4, volume.getISSN());

            statement.executeUpdate();

            Edition edition = new Edition();
            edition.setEditionNum(nextEditionNumber);
            edition.setPublicationMonth(Month.valueOf(publicationMonth).ordinal());
            edition.setVolumeNum(volume.getVolNum());
            return edition;

        } finally {

            closeConnection();
        }
    }

    @Override
    public ArrayList<Journal> getAllJournals() throws SQLException {
        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT * FROM Journals";
            statement = connection.prepareStatement(sqlQuery);

            res = statement.executeQuery();

            ArrayList<Journal> journals = new ArrayList<>();

            while (res.next()) {
                Journal journal = new Journal();
                journal.setISSN(res.getString(1));
                journal.setName(res.getString(2));

                journals.add(journal);
            }

            return journals;

        } finally {
            if (res != null)
                res.close();

            closeConnection();
        }
    }

    @Override
    public ArrayList<Volume> getAllJournalVolumes(Journal journal) throws SQLException {
        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT * FROM Volumes WHERE ISSN=?";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, journal.getISSN());

            res = statement.executeQuery();

            ArrayList<Volume> volumes = new ArrayList<>();

            while (res.next()) {
                Volume volume = new Volume();
                volume.setVolNum(res.getInt(1));
                volume.setISSN(res.getString(2));
                volume.setPublicationYear(res.getInt(3));

                volumes.add(volume);
            }

            return volumes;

        } finally {
            if (res != null)
                res.close();

            closeConnection();
        }
    }

    @Override
    public ArrayList<Edition> getAllVolumeEditions(Volume volume) throws SQLException {

        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT * FROM Editions WHERE ISSN=? AND volumeNum=?";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, volume.getISSN());
            statement.setInt(2, volume.getVolNum());

            res = statement.executeQuery();

            ArrayList<Edition> editions = new ArrayList<>();

            while (res.next()) {
                Edition edition = new Edition();
                edition.setEditionID(res.getInt(1));
                edition.setEditionNum(res.getInt(2));
                edition.setPublicationMonth(res.getInt(3));
                edition.setVolumeNum(res.getInt(4));
                edition.setPublic(res.getBoolean(5));
                edition.setIssn(res.getString(6));

                editions.add(edition);
            }

            return editions;

        } finally {
            if (res != null)
                res.close();

            closeConnection();
        }
    }

    @Override
    public ArrayList<EditionArticle> getallEditionArticles(Edition edition)
            throws ObjectDoesNotExistException, SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<Article> articlesNeedingContributions(User user) throws UserDoesNotExistException, InvalidAuthenticationException, SQLException {

        if(!validCredentials(user))
            throw new InvalidAuthenticationException();


        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT Articles.articleID, title, abstract, content, ISSN, COUNT(Reviews.articleOfReviewerID) as contributions FROM (Articles, Authorships)\n" +
                    "LEFT JOIN Reviews on Reviews.articleOfReviewerID = Articles.articleID\n" +
                    "WHERE \n" +
                    "\tArticles.articleID = Authorships.articleID AND \n" +
                    "\tAuthorships.email = ?\n" +
                    "\t\n" +
                    "GROUP BY Articles.articleID\n" +
                    "HAVING contributions < 3;";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1,user.getEmail());

            res = statement.executeQuery();

            ArrayList<Article> articles = new ArrayList<>();

            while (res.next()) {
                Article article = new Article();
                article.setArticleID(res.getInt(1));
                article.setTitle(res.getString(2));
                article.setSummary(res.getString(3));
                article.setContent(res.getString(4));
                article.setIssn(res.getString(5));

                articles.add(article);
            }

            return articles;

        } finally {
            if (res != null)
                res.close();

            closeConnection();
        }
    }

    @Override
    public ArrayList<Review> emptyReviews(User user) throws UserDoesNotExistException, InvalidAuthenticationException, SQLException {
        if(!validCredentials(user))
            throw new InvalidAuthenticationException();


        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT * FROM Reviews WHERE\n" +
                    "reviewerEmail = ? AND\n" +
                    "summary is null AND verdict is null and isFinal = 0";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1,user.getEmail());

            res = statement.executeQuery();

            ArrayList<Review> reviews = new ArrayList<>();

            while (res.next()) {
                Review review = new Review();
                review.setReviewID(res.getInt(1));
                review.setSummary(res.getString(2));
                review.setVerdict(res.getString(3));
                review.setFinal(res.getBoolean(4));

                reviews.add(review);
            }

            return reviews;

        } finally {
            if (res != null)
                res.close();

            closeConnection();
        }
    }

}
