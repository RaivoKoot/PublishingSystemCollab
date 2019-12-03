package database_interface;

import java.sql.*;
import java.time.Month;
import java.util.ArrayList;

import exceptions.*;
import models.*;

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

        if (!isChiefEditor(journalChief))
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

        if (!isChiefEditor(chiefAuthentication))
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

    public boolean isMainAuthor(User user, int articleID) throws SQLException, InvalidAuthenticationException {
        PreparedStatement statementTwo = null;
        ResultSet res = null;
        try {
            openConnection();
            String sqlQuery = "SELECT isMain FROM Authorships WHERE\n" +
                    "\tarticleID = ? AND\n" +
                    "\temail = ?";
            statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, articleID);
            statement.setString(2, user.getEmail());


            res = statement.executeQuery();
            if (!res.next() || res.getInt(1) == 0) {
                // main authorship does not exist
                throw new InvalidAuthenticationException();
            }

            return true;

        } finally {

            closeConnection();

            if (statementTwo != null)
                statementTwo.close();

            if (res != null)
                res.close();
        }
    }

    @Override
    public boolean addCoAuthor(Article article, User newAuthor, User mainAuthor) throws UserDoesNotExistException, ObjectDoesNotExistException, InvalidAuthenticationException, SQLException {
        if (!userExists(newAuthor))
            throw new UserDoesNotExistException("the new author does not exist");

        if (!userExists(mainAuthor)) {
            throw new UserDoesNotExistException("your validation credentials do not exist");
        }

        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT isMain FROM Authorships WHERE\n" +
                    "\tarticleID = ? AND\n" +
                    "\temail = ?";
            statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, article.getArticleID());
            statement.setString(2, mainAuthor.getEmail());


            res = statement.executeQuery();
            if (!res.next() || res.getInt(1) == 0) {
                // main authorship does not exist
                throw new InvalidAuthenticationException();
            }

            // Insert the new authorship into the table with the new submission
            String sqlQueryTwo = "INSERT INTO Authorships (email, articleID, isMain) VALUES\n" +
                    "\t(?,?,?)";
            statement = connection.prepareStatement(sqlQueryTwo);
            statement.setString(1, newAuthor.getEmail());
            statement.setInt(2, article.getArticleID());
            statement.setBoolean(3, false);
            int res2 = statement.executeUpdate();

            if (res2 == 1)
                return true;

            return false;

        } finally {
            if (res != null)
                res.close();

            closeConnection();
        }

    }


    @Override
    public boolean createNextVolume(Journal journal, JournalEditor editor, int publicationYear)
            throws ObjectDoesNotExistException, InvalidAuthenticationException, SQLException {

        if (!isChiefEditor(editor))
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

        if (!isChiefEditor(editor))
            throw new InvalidAuthenticationException();

        ResultSet rs = null;
        try {
            openConnection();

            String sqlQuery = "SELECT IFNULL(MAX(editionNum),0) + 1 FROM Editions WHERE issn = ? AND volumeNum = ?;";

            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, volume.getISSN());
            statement.setInt(2, volume.getVolNum());
            rs = statement.executeQuery();

            if (!rs.next())
                throw new SQLException();

            int nextEditionNumber = rs.getInt(1);

            if (nextEditionNumber > 6) {
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

        if (!validCredentials(user))
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
            statement.setString(1, user.getEmail());

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
        if (!validCredentials(user))
            throw new InvalidAuthenticationException();


        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT reviewID, summary, verdict, Reviews.isFinal, Articles.title, submissionID FROM Reviews, Articles WHERE\n" +
                    "Articles.articleID = Reviews.submissionID AND\n" +
                    "\n" +
                    "reviewerEmail = ? AND\n" +
                    "summary is null AND verdict is null and Reviews.isFinal = 0";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, user.getEmail());

            res = statement.executeQuery();

            ArrayList<Review> reviews = new ArrayList<>();

            while (res.next()) {
                Review review = new Review();
                review.setReviewID(res.getInt(1));
                review.setSummary(res.getString(2));
                review.setVerdict(res.getString(3));
                review.setFinal(res.getBoolean(4));
                review.setArticleName(res.getString(5));
                review.setSubmissionArticleID(res.getInt(6));

                reviews.add(review);
            }

            return reviews;

        } finally {
            if (res != null)
                res.close();

            closeConnection();
        }
    }

    @Override
    public Article getArticleInfo(int articleID) throws ObjectDoesNotExistException, SQLException {
        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT * FROM Articles WHERE articleID=?";
            statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, articleID);

            res = statement.executeQuery();

            Article article = new Article();

            if (res.next()) {
                article.setTitle(res.getString(2));
                article.setSummary(res.getString(3));
                article.setContent(res.getString(4));
            } else {
                throw new ObjectDoesNotExistException("An article with that ID does not exist");
            }

            return article;


        } finally {
            if (res != null)
                res.close();

            closeConnection();
        }
    }

    @Override
    public ArrayList<Article> getOwnArticles(User user) throws UserDoesNotExistException, InvalidAuthenticationException, SQLException {
        if (!validCredentials(user))
            throw new InvalidAuthenticationException();


        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT Articles.* FROM Articles, Authorships WHERE\n" +
                    "Articles.articleID = Authorships.articleID AND\n" +
                    "Authorships.email = ?";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, user.getEmail());

            res = statement.executeQuery();

            ArrayList<Article> articles = new ArrayList<>();

            while (res.next()) {
                Article article = new Article();
                article.setArticleID(res.getInt(1));
                article.setTitle(res.getString(2));
                article.setSummary(res.getString(3));
                article.setContent(res.getString(4));
                article.setFinal(res.getBoolean(5));
                article.setAccepted(res.getBoolean(6));
                article.setIssn(res.getString(7));

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
    public ArrayList<Article> getUnaffiliatedArticlesToReview(User user) throws UserDoesNotExistException, InvalidAuthenticationException, SQLException {
        if (!validCredentials(user) || articlesNeedingContributions(user).size() == 0)
            throw new InvalidAuthenticationException();


        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT Articles.articleID, title, abstract, content, ISSN, COUNT(Reviews.submissionID) as reviews FROM (Articles)\n" +
                    "LEFT JOIN Reviews on Reviews.submissionID = Articles.articleID\n" +
                    "INNER JOIN Authorships on Authorships.articleID = Articles.articleID\n" +
                    "\n" +
                    "WHERE Authorships.email not in\n" +
                    "\t(SELECT DISTINCT email FROM Authorships WHERE articleID in (SELECT articleID FROM Authorships WHERE email=?)\n" +
                    "\tunion\n" +
                    "\tSELECT DISTINCT email FROM JournalEditors WHERE issn in (SELECT issn FROM JournalEditors WHERE email=?))\n" +
                    "\n" +
                    "GROUP BY Articles.articleID\n" +
                    "HAVING reviews < 3;";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getEmail());

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
    public boolean reserverReview(Review review, User user) throws InvalidAuthenticationException, SQLException, UserDoesNotExistException {
        if (!validCredentials(user))
            throw new InvalidAuthenticationException();

        PreparedStatement statementTwo = null;
        ResultSet rs = null;
        try {
            openConnection();
            connection.setAutoCommit(false);

            // Insert the new submission into the table
            String sqlQuery = "INSERT INTO Reviews (submissionID, articleOfReviewerID, reviewerEmail) VALUES\n" +
                    "\t(?,?,?)";
            statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, review.getSubmissionArticleID());
            statement.setInt(2, review.getArticleOfReviwerID());
            statement.setString(3, user.getEmail());
            int res1 = statement.executeUpdate();

            if (res1 != 1) {
                connection.rollback();
                throw new SQLException();
            }

            // Make sure that someone else did not also create a review for this article at the same time
            // and the article now has 4 reviews.

            // get the number of reviews for this article
            String sqlQueryTwo = "SELECT COUNT(r1.submissionID) as reviewCount FROM (Articles)\n" +
                    "LEFT JOIN Reviews r1 on r1.submissionID = Articles.articleID\n" +
                    "WHERE Articles.articleID = ?";
            statementTwo = connection.prepareStatement(sqlQueryTwo);
            statementTwo.setInt(1, review.getSubmissionArticleID());
            rs = statementTwo.executeQuery();

            if (!rs.next() || rs.getInt(1) > 3) {
                connection.rollback();
                throw new SQLException();
            }

            connection.commit();

            return true;

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
    public boolean submitReview(Review review, User user) throws InvalidAuthenticationException, UserDoesNotExistException, SQLException {
        if (!validCredentials(user))
            throw new InvalidAuthenticationException();

        System.out.println("Starting submit");
        PreparedStatement statementTwo = null;

        try {
            openConnection();
            connection.setAutoCommit(false);

            // Insert the new submission into the table
            String sqlQuery = "UPDATE Reviews SET summary=?, verdict=? WHERE\n" +
                    "\treviewID = ? AND reviewerEmail=?";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, review.getSummary());
            statement.setString(2, review.getVerdict());
            statement.setInt(3, review.getReviewID());
            statement.setString(4, user.getEmail());
            int res1 = statement.executeUpdate();

            if (res1 != 1) {
                connection.rollback();
                throw new SQLException();
            }

            // Insert the critiques

            // get the number of reviews for this article
            String sqlQueryTwo = "INSERT INTO Critiques (reviewID, description) VALUES\n" +
                    "\t(?, ?)";
            statementTwo = connection.prepareStatement(sqlQueryTwo);

            for (Critique critique : review.getCritiques()) {

                statementTwo.setInt(1, review.getReviewID());
                statementTwo.setString(2, critique.getDescription());
                statementTwo.addBatch();

            }

            statementTwo.executeBatch();

            connection.commit();

            return true;

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
    public ArrayList<Article> getOwnArticleWithStatus(User user) throws InvalidAuthenticationException, UserDoesNotExistException, SQLException {
        if (!validCredentials(user))
            throw new InvalidAuthenticationException();


        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT Articles.articleID, title, isAccepted, Articles.isFinal, COUNT(r1.submissionID) as reviewCount, \n" +
                    "\tCOUNT(r2.articleOfReviewerID) as contributionCount, SUM(r1.isFinal) as finalReviews, SUM(r1.hasResponse) as reviewResponses \n" +
                    "\tFROM (Articles)\n" +
                    "\t\n" +
                    "LEFT JOIN Reviews r1 on r1.submissionID = Articles.articleID\n" +
                    "LEFT JOIN Reviews r2 on r2.articleOfReviewerID = Articles.articleID\n" +
                    "INNER JOIN Authorships on Authorships.articleID = Articles.articleID\n" +
                    "\n" +
                    "WHERE Authorships.email = ?\n" +
                    "GROUP BY Articles.articleID;";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, user.getEmail());

            res = statement.executeQuery();

            ArrayList<Article> articles = new ArrayList<>();

            while (res.next()) {
                Article article = new Article();
                article.setArticleID(res.getInt(1));
                article.setTitle(res.getString(2));
                article.setAccepted(res.getBoolean(3));
                article.setFinal(res.getBoolean(4));
                article.setReviewsReceived(res.getInt(5));
                article.setReviewsContributed(res.getInt(6));
                article.setFinalReviewsReceived(res.getInt(7));
                article.setResponesToReviewsGiven(res.getInt(8));

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
    public ArrayList<Journal> getJournalsByUser(User user) throws InvalidAuthenticationException, UserDoesNotExistException, SQLException {
        if (!validCredentials(user))
            throw new InvalidAuthenticationException();

        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT Journals.issn, Journals.name FROM Journals, JournalEditors WHERE\n"
                    + "Journals.issn = JournalEditors.issn AND\n"
                    + "JournalEditors.email = ?";

            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, user.getEmail());

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
    public boolean deleteEditor(JournalEditor journalEditor) throws InvalidAuthenticationException, UserDoesNotExistException, SQLException, ObjectDoesNotExistException, CantRemoveLastChiefEditorException {

        if (!validCredentials(journalEditor))
            throw new InvalidAuthenticationException();

        try {
            //checking whether there are other chief editors - cant delete the only chief editor of a journal
            if (isChiefEditor(journalEditor)) {
                ResultSet rs = null;
                try {
                    openConnection();

                    String sqlQuery = "SELECT COUNT(*) FROM JournalEditors WHERE JournalEditors.issn = ?";

                    statement = connection.prepareStatement(sqlQuery);
                    statement.setString(1, journalEditor.getIssn());

                    rs = statement.executeQuery();
                    if (rs.next()) {
                        if (rs.getInt(1) < 2) throw new CantRemoveLastChiefEditorException(journalEditor.getIssn());
                    } else throw new CantRemoveLastChiefEditorException(journalEditor.getIssn());

                } finally {
                    closeConnection();
                }

            }
        } catch (InvalidAuthenticationException e) {
        }


        try {
            openConnection();

            String sqlQuery = "DELETE FROM JournalEditors WHERE JournalEditors.email = ? AND JournalEditors.issn = ?";

            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, journalEditor.getEmail());
            statement.setString(2, journalEditor.getIssn());

            int result = statement.executeUpdate();
            if (result != 1) throw new ObjectDoesNotExistException("Editorship could not be deleted");

            return true;
        } finally {
            closeConnection();
        }
    }

    @Override
    public ArrayList<Review> getInitialReviewsOfArticle(Article article, User authentication) throws InvalidAuthenticationException, UserDoesNotExistException, SQLException {

        if (!validCredentials(authentication)) {
            throw new InvalidAuthenticationException();
        }

        if (!isMainAuthor(authentication, article.getArticleID())) {
            throw new InvalidAuthenticationException();
        }

        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT reviewID, summary, verdict, submissionID FROM Reviews WHERE \n" +
                    "\tsubmissionID = ? AND verdict is not null AND isFinal = false and hasResponse=false";
            statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, article.getArticleID());

            res = statement.executeQuery();

            ArrayList<Review> reviews = new ArrayList<>();

            // counter for giving  the review a pseudonym name
            int counter = 0;
            while (res.next()) {
                counter++;

                Review review = new Review();
                review.setReviewID(res.getInt(1));
                review.setSummary(res.getString(2));
                review.setVerdict(res.getString(3));
                review.setSubmissionArticleID(res.getInt(4));
                review.setReviewerName(counter);


                reviews.add(review);
            }

            return reviews;

        } finally {
            if (res != null)
                res.close();

            closeConnection();
        }

    }


  
  public ArrayList<Critique> getReviewCritiques(Review review, User user) throws InvalidAuthenticationException, ObjectDoesNotExistException, SQLException, UserDoesNotExistException {
      if(!validCredentials(user)){
          throw new InvalidAuthenticationException();
      }


      ResultSet res = null;
      try {
          openConnection();

          String sqlQuery = "SELECT critiqueID, description, reply FROM Critiques WHERE reviewID = ?";
          statement = connection.prepareStatement(sqlQuery);
          statement.setInt(1, review.getReviewID());

          res = statement.executeQuery();

          ArrayList<Critique> critiques = new ArrayList<>();


          while (res.next()) {

              Critique critique = new Critique();
              critique.setCritiqueID(res.getInt(1));
              critique.setDescription(res.getString(2));
              critique.setReply(res.getString(3));


              critiques.add(critique);
          }

          return critiques;

      } finally {
          if (res != null)
              res.close();

          closeConnection();
      }
  }


    public boolean submitReviewResponse(Review review, User user) throws InvalidAuthenticationException, SQLException, UserDoesNotExistException {

      if (!validCredentials(user))
            throw new InvalidAuthenticationException();

        if (!isMainAuthor(user, review.getSubmissionArticleID())) {
            throw new InvalidAuthenticationException();
        }

        PreparedStatement statement2 = null;

        try {
            openConnection();
            connection.setAutoCommit(false);

            String sqlQuery = "UPDATE Reviews SET hasResponse = 1 WHERE Reviews.reviewID =?";
            statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, review.getReviewID());

            int res1 = statement.executeUpdate();

            if (res1 != 1) {
                connection.rollback();
                throw new SQLException();
            }


            String sqlQuery2 = "UPDATE Critiques SET reply = ? WHERE Critiques.critiqueID = ?";
            statement2 = connection.prepareStatement(sqlQuery2);


            for (Critique critique : review.getCritiques()) {
                statement2.setString(1, critique.getReply());
                statement2.setInt(2, critique.getCritiqueID());
                statement2.addBatch();
            }

            statement2.executeBatch();

            connection.commit();

            return true;
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {

            closeConnection();

            if (statement2 != null)
                statement2.close();
        }


    }


    public boolean submitFinalArticleVersion(Article article, User mainAuthor) throws InvalidAuthenticationException, ObjectDoesNotExistException, SQLException, UserDoesNotExistException {
        if (!validCredentials(mainAuthor))
            throw new InvalidAuthenticationException();

        if (!isMainAuthor(mainAuthor, article.getArticleID())) {
            throw new InvalidAuthenticationException();
        }

        try {
            openConnection();
            String sqlQuery = "UPDATE Articles SET title = ?, abstract = ?, content = ?, isFinal = true WHERE Articles.articleID = ? AND Articles.isFinal = false";

            statement = connection.prepareStatement(sqlQuery);

            statement.setString(1, article.getTitle());
            statement.setString(2, article.getSummary());
            statement.setString(3, article.getContent());
            statement.setInt(4, article.getArticleID());

            int result = statement.executeUpdate();
            if (result != 1) throw new ObjectDoesNotExistException("Final article could not be submitted");

            return true;
        } finally {
            closeConnection();
        }
    }

    @Override
    public void setIsAccepted(Article article, User editor) throws InvalidAuthenticationException, ObjectDoesNotExistException, SQLException, UserDoesNotExistException {

    }

    @Override
    public void deleteUser(User user) throws InvalidAuthenticationException, ObjectDoesNotExistException, SQLException, UserDoesNotExistException {

    }

    @Override
    public void publishEdition(Edition edition, User mainEditor) throws InvalidAuthenticationException, ObjectDoesNotExistException, SQLException, UserDoesNotExistException {

    }

    @Override
    public ArrayList<Article> getAcceptedArticlesByJournal(Journal journal, User editor) throws InvalidAuthenticationException, ObjectDoesNotExistException, SQLException, UserDoesNotExistException {
        return null;
    }

    @Override
    public Edition getLatestEdition(Journal journal, User editor) {
        return null;
    }

    @Override
    public EditionArticle assignArticleToEdition(Article article, User editor) {
        return null;
    }

    public ArrayList<Article> getArticlesNeedingFinalVerdicts(User user) throws SQLException, UserDoesNotExistException, InvalidAuthenticationException {
        if (!validCredentials(user))
            throw new InvalidAuthenticationException();


        ResultSet res = null;
        try {
            openConnection();

            String sqlQuery = "SELECT Articles.articleID, Reviews.reviewID, title, Articles.abstract, content FROM Articles, Reviews, Critiques WHERE\n" +
                    "\tArticles.articleID = Reviews.submissionID AND Reviews.reviewerEmail = ?\n" +
                    "\tAND Articles.isFinal = true\n" +
                    "\tAND Critiques.reviewID = Reviews.reviewID AND Critiques.reply is not null\n" +
                    "\t\n" +
                    "\t\n" +
                    "\tGROUP BY Reviews.reviewID";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, user.getEmail());

            res = statement.executeQuery();

            ArrayList<Article> articles = new ArrayList<>();

            while (res.next()) {
                Article article = new Article();
                article.setArticleID(res.getInt(1));
                article.setReviewID(res.getInt(2));
                article.setTitle(res.getString(3));
                article.setSummary(res.getString(4));

                article.setContent(res.getString(5));

                articles.add(article);
            }

            return articles;

        } finally {
            if (res != null)
                res.close();

            closeConnection();
        }
    }


    //to be tested
    public ArrayList<Article> getJournalArticlesNeedingEditorDecision(Journal journal, User editor) throws InvalidAuthenticationException, ObjectDoesNotExistException, SQLException, UserDoesNotExistException {

        if (!validCredentials(editor))
            throw new InvalidAuthenticationException();

        ResultSet rs = null;

        try{
            openConnection();
            String sqlQuery = "SELECT Articles.articleID, Articles.title, SUM(Reviews.isFinal) AS numberOfFinals FROM Articles, Reviews, Authorships\n" +
                    "WHERE Reviews.submissionID = Articles.articleID\n" +
                    "AND Articles.articleID = Authorships.articleID\n" +
                    "AND Articles.issn = ?" +
                    "\n" +  "AND Authorships.email not in\n" +
                    "(SELECT DISTINCT email FROM Authorships WHERE articleID in (SELECT articleID FROM Authorships WHERE email= ?)\n" +
                    "union\n" +
                    "SELECT DISTINCT email FROM JournalEditors WHERE issn in (SELECT issn FROM JournalEditors WHERE email= ?))\n" +
                    "\n" +
                    "AND Reviews.isFinal = true\n" +
                    "GROUP BY Reviews.submissionID\n" +
                    "HAVING numberOfFinals = 3;\n";

            statement = connection.prepareStatement(sqlQuery);

            statement.setString(1, journal.getISSN());
            statement.setString(2, editor.getEmail());
            statement.setString(3, editor.getEmail());

            rs = statement.executeQuery();

            ArrayList<Article> list = new ArrayList<>();

            while (rs.next()) {
                Article article = new Article();
                article.setArticleID(rs.getInt(1));
                article.setTitle(rs.getString(2));
                article.setSummary(rs.getString(3));
                article.setContent(rs.getString(4));
                article.setFinal(rs.getBoolean(5));
                article.setAccepted(rs.getBoolean(6));
                article.setIssn(rs.getString(7));
                article.setReviewsReceived(rs.getInt(8));
                article.setReviewsContributed(rs.getInt(9));
                article.setResponesToReviewsGiven(rs.getInt(10));
                article.setFinalReviewsReceived(rs.getInt(11));

                list.add(article);
            }
            return list;

        }
        finally {
            if(rs != null)
                rs.close();

            closeConnection();
        }

    }

    @Override
    //to be tested
    public ArrayList<String> getAllVerdictsForArticle(Article article, User editor) throws InvalidAuthenticationException, ObjectDoesNotExistException, SQLException, UserDoesNotExistException {

        if (!validCredentials(editor))
            throw new InvalidAuthenticationException();

        JournalEditor editorship = new JournalEditor(editor);
        editorship.setIssn(article.getIssn());

        if (getEditorship(editorship) == null)
            throw new InvalidAuthenticationException();


        ResultSet rs = null;

        try {
            openConnection();
            String sqlQuery = "SELECT Reviews.verdict FROM Reviews WHERE Reviews.submissionID = ?";
            statement = connection.prepareStatement(sqlQuery);

            statement.setInt(1, article.getArticleID());

            rs = statement.executeQuery();

            ArrayList<String> list = new ArrayList<>();

            while (rs.next()) {
                list.add(rs.getString(1));
            }

            return list;
        }
        finally {
            if(rs != null)
                rs.close();

            closeConnection();
        }
    }

}