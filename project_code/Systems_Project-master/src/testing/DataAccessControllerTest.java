package testing;

import database_interface.DataAccessController;
import database_interface.DatabaseConstants;
import exceptions.*;
import models.*;
import org.junit.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runners.MethodSorters;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataAccessControllerTest {

    static DatabaseTestingSetup dbSetup;
    static DataAccessController db;

    static User chiefEditor;
    static User user;
    static User nonexistentUser;
    static User incompleteInfo;
    static User badPasswordUser;

    static User thirdUser;

    static Journal journal;

    static JournalEditor chief;
    static JournalEditor newEditor;


    static Article submission;


    @BeforeClass
    public static void setUp() throws Exception {
        dbSetup = new DatabaseTestingSetup();
        dbSetup.createTables();

        db = new DataAccessController(new DatabaseConstants(true));

        chiefEditor = new User();
        user = new User();

        chiefEditor.setEmail("chief@gmail.com");
        chiefEditor.setPassword("pswd");
        chiefEditor.setSurname("Chief");
        chiefEditor.setForenames("Chiefo");
        chiefEditor.setUniversity("Sheffield");
        chiefEditor.setTitle("DR");

        user.setEmail("user@gmail.com");
        user.setPassword("pswd");
        user.setSurname("user");
        user.setForenames("usero");
        user.setUniversity("Sheffield");
        user.setTitle("Mr");

        journal = new Journal();
        journal.setName("Computer Science Journal");
        journal.setISSN("CSJ");

        incompleteInfo = new User();
        incompleteInfo.setEmail("incomplete@gmail.com");

        nonexistentUser = new User();
        nonexistentUser.setEmail("nonexisting@gmail.com");
        nonexistentUser.setPassword("blabla");

        badPasswordUser = new User();
        badPasswordUser.setEmail("user@gmail.com");
        badPasswordUser.setPassword("wrong");

        submission = new Article();
        submission.setContent("www.url.com");
        submission.setSummary("summary");
        submission.setTitle("Title Paper");
        submission.setIssn(journal.getISSN());

        newEditor = new JournalEditor(user);
        chief = new JournalEditor(chiefEditor);
        newEditor.setChief(false);
        newEditor.setIssn(journal.getISSN());

        thirdUser = new User();
        thirdUser.setEmail("third@gmail.com");
        thirdUser.setPassword("pswd");
        thirdUser.setSurname("user");
        thirdUser.setForenames("usero");
        thirdUser.setUniversity("Sheffield");
        thirdUser.setTitle("Mr");

    }

    @AfterClass
    public static void tearDown() throws Exception {
        dbSetup.dropTables();
    }

    @Test
    public void test1RegisterBaseUser() throws SQLException, UserAlreadyExistsException, IncompleteInformationException {

        assertTrue(db.registerBaseUser(chiefEditor));
        assertTrue(db.registerBaseUser(user));
        assertTrue(db.registerBaseUser(thirdUser));

        assertThrows(UserAlreadyExistsException.class, () -> {
            db.registerBaseUser(user);
        });

        assertThrows(IncompleteInformationException.class, () -> {
            db.registerBaseUser(incompleteInfo);
        });

        incompleteInfo.setPassword("Hi");
        assertThrows(IncompleteInformationException.class, () -> {
            db.registerBaseUser(incompleteInfo);
        });

        incompleteInfo.setPassword("");

        assertThrows(IncompleteInformationException.class, () -> {
            db.registerBaseUser(incompleteInfo);
        });


    }

    @Test
    public void test2ChangePassword() throws IncompleteInformationException, SQLException, UserDoesNotExistException, InvalidAuthenticationException {

        assertTrue(db.changePassword(user, "pswd"));

        assertThrows(IncompleteInformationException.class, () -> {
            db.changePassword(incompleteInfo, "");
        });

        assertThrows(UserDoesNotExistException.class, () -> {
            db.changePassword(nonexistentUser, "new");
        });

        assertThrows(InvalidAuthenticationException.class, () -> {
            db.changePassword(badPasswordUser, "new");
        });

    }

    @Test
    public void test3ValidCredentials() throws SQLException, UserDoesNotExistException {

        assertTrue(db.validCredentials(user));

        assertThrows(UserDoesNotExistException.class, () -> {
            db.validCredentials(nonexistentUser);
        });

        assertFalse(db.validCredentials(badPasswordUser));


    }

    @Test
    public void test4CreateJournalAndGetJournals() throws SQLException, UniqueColumnValueAlreadyExists, UserDoesNotExistException, InvalidAuthenticationException {

        assertTrue(db.createJournal(journal, chiefEditor));

        assertThrows(UniqueColumnValueAlreadyExists.class, () -> {
            db.createJournal(journal, chiefEditor);
        });

        assertThrows(UserDoesNotExistException.class, () -> {
            db.createJournal(journal, nonexistentUser);
        });

        assertThrows(InvalidAuthenticationException.class, () -> {
            db.createJournal(journal, badPasswordUser);
        });

    }

    @Test
    public void test5PromoteUserToEditor() throws SQLException, UserDoesNotExistException, InvalidAuthenticationException, IncompleteInformationException {


        assertThrows(InvalidAuthenticationException.class, () -> {
            db.promoteUserToEditor(newEditor, chief);
        });

        chief.setIssn(journal.getISSN());

        assertTrue(db.promoteUserToEditor(newEditor, chief));

        JournalEditor nonexistentChief = new JournalEditor(nonexistentUser);
        nonexistentChief.setIssn(journal.getISSN());

        assertThrows(InvalidAuthenticationException.class, () -> {
            db.promoteUserToEditor(newEditor, nonexistentChief);
        });

        assertThrows(UserDoesNotExistException.class, () -> {
            db.promoteUserToEditor(nonexistentChief, chief);
        });

        JournalEditor badpasswordChief = new JournalEditor(badPasswordUser);
        badpasswordChief.setIssn(journal.getISSN());
        assertThrows(InvalidAuthenticationException.class, () -> {
            db.promoteUserToEditor(newEditor, badpasswordChief);
        });

    }

    @Test
    public void test6SubmitArticle() throws SQLException, IncompleteInformationException, UserDoesNotExistException, InvalidAuthenticationException {

        assertTrue(1 == db.submitArticle(submission, user).getArticleID());
        assertTrue(2 == db.submitArticle(submission, user).getArticleID());

        assertThrows(InvalidAuthenticationException.class, () -> {
            db.submitArticle(submission, badPasswordUser);
        });

        submission.setIssn("");
        assertThrows(IncompleteInformationException.class, () -> {
            db.submitArticle(submission, user);
        });
        submission.setIssn("CSJ");

        assertThrows(UserDoesNotExistException.class, () -> {
            db.submitArticle(submission, nonexistentUser);
        });

        // too long title
        submission.setContent("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        assertThrows(SQLException.class, () -> {
            db.submitArticle(submission, user);
        });

    }

    @Test
    public void test7GetAllJournals() throws SQLException {
        ArrayList<Journal> journals = db.getAllJournals();
        assertEquals(1, journals.size());


    }

    @Test
    public void test8CreateNextVolume() throws SQLException, ObjectDoesNotExistException, InvalidAuthenticationException {

        boolean success = db.createNextVolume(journal, chief, 2012);

        assertTrue(success);

        success = db.createNextVolume(journal, chief, 2012);

        assertTrue(success);

        assertThrows(InvalidAuthenticationException.class, () -> {
            db.createNextVolume(journal, newEditor, 2012);
        });

    }

    @Test
    public void test9GetVolumesOfJournal() throws SQLException {

        ArrayList<Volume> volumes = db.getAllJournalVolumes(journal);

        assertEquals(2, volumes.size());

        assertEquals(1, volumes.get(0).getVolNum());
        assertEquals(2, volumes.get(1).getVolNum());

    }

    @Test
    public void test9test1CreateNextVolumeEdition() throws SQLException, VolumeFullException, ObjectDoesNotExistException, InvalidAuthenticationException, NoMoreEditionsAllowedInVolumeException, LastEditionNotFinishedException {

        ArrayList<Volume> volumes = db.getAllJournalVolumes(journal);

        db.createNextEdition(volumes.get(1), chief, "JANUARY");

        assertThrows(LastEditionNotFinishedException.class, () -> {
            db.createNextEdition(volumes.get(1), chief, "JANUARY");
        });

        /*
        db.createNextEdition(volumes.get(1), chief, "JANUARY");
        db.createNextEdition(volumes.get(1), chief, "JANUARY");
        db.createNextEdition(volumes.get(1), chief, "JANUARY");
        db.createNextEdition(volumes.get(1), chief, "JANUARY");

        assertThrows(VolumeFullException.class, () -> {
            db.createNextEdition(volumes.get(1), chief, "JANUARY");
        });
        */

        assertThrows(InvalidAuthenticationException.class, () -> {
            db.createNextEdition(volumes.get(1), newEditor, "JANUARY");
        });

        db.createNextVolume(journal, chief, 2012);

        assertThrows(NoMoreEditionsAllowedInVolumeException.class, () -> {
            db.createNextEdition(volumes.get(1), chief, "JANUARY");
        });
    }

    @Test
    public void test9test2GetAllVolumeEditions() throws SQLException {
        Volume volume = new Volume();
        volume.setISSN(journal.getISSN());
        volume.setVolNum(2);

        ArrayList<Edition> editions = db.getAllVolumeEditions(volume);

        assertEquals(1, editions.size());

        volume.setVolNum(5);
        editions = db.getAllVolumeEditions(volume);
        assertEquals(0, editions.size());

        volume.setISSN("Nonexistent");
        editions = db.getAllVolumeEditions(volume);
        assertEquals(0, editions.size());
    }


    @Test
    public void test9test3makeEditorChief() throws SQLException, IncompleteInformationException, UserDoesNotExistException, InvalidAuthenticationException {
        boolean success = db.makeChiefEditor(newEditor, chief);
        assertTrue(success);

        success = db.makeChiefEditor(newEditor, chief);
        assertTrue(success);

        assertTrue(success);

        newEditor.setIssn("hi");
        assertThrows(InvalidAuthenticationException.class, () -> {
            db.makeChiefEditor(newEditor, chief);
        });

        newEditor.setIssn(chief.getIssn());

        JournalEditor notAnEditor = new JournalEditor(thirdUser);
        notAnEditor.setIssn(chief.getIssn());

        assertFalse(db.makeChiefEditor(notAnEditor, chief));
    }

    @Test
    public void test9test4articlesNeedingContributions() throws SQLException, UserDoesNotExistException, InvalidAuthenticationException {

        assertTrue(db.articlesNeedingContributions(user).size() == 2);

        /*
        TEST LATER
         */

        // TODO
    }

    @Test
    public void test9test5emptyReviews() throws SQLException, UserDoesNotExistException, InvalidAuthenticationException {

        assertTrue(db.emptyReviews(user).size() == 0);

        /*
        TEST LATER
         */

        // TODO
    }

    @Test
    public void test9test6addCoAuthor() throws ObjectDoesNotExistException, SQLException, UserDoesNotExistException, InvalidAuthenticationException {

        assertTrue(db.addCoAuthor(submission, chief, user));

        assertThrows(SQLException.class, () -> {
            db.addCoAuthor(submission, chief, user);
        });

        assertThrows(InvalidAuthenticationException.class, () -> {
            assertFalse(db.addCoAuthor(submission, user, chief));
        });

        /*
        TEST LATER
         */

        // TODO
    }

    @Test
    public void test9test7getOwnArticles() throws ObjectDoesNotExistException, SQLException, UserDoesNotExistException, InvalidAuthenticationException {

        assertTrue(db.getOwnArticles(user).size() == 2);
        assertTrue(db.getOwnArticles(chiefEditor).size() == 1);


        assertThrows(InvalidAuthenticationException.class, () -> {
            db.getOwnArticles(badPasswordUser);
        });

        /*
        TEST LATER
         */

        // TODO
    }

    @Test
    public void test9test8getArticlesToReviewUnaffiliated() throws SQLException, UserDoesNotExistException, InvalidAuthenticationException, IncompleteInformationException {

        assertThrows(UserDoesNotExistException.class, () -> {
            db.getUnaffiliatedArticlesToReview(nonexistentUser);
        });

        // user does not have articles he needs to contribute to
        assertThrows(InvalidAuthenticationException.class, () -> {
            db.getUnaffiliatedArticlesToReview(thirdUser);
        });

        assertEquals(0, db.getUnaffiliatedArticlesToReview(user).size());
        assertEquals(0, db.getUnaffiliatedArticlesToReview(chiefEditor).size());

        // change the content to something normal again as it was made huge in an earlier test
        submission.setContent("HI");
        db.submitArticle(submission, thirdUser);

        assertEquals(1, db.getUnaffiliatedArticlesToReview(chiefEditor).size());

    }

    @Test
    public void test9test9reserveReview() throws SQLException, UserDoesNotExistException, InvalidAuthenticationException {

        Article article = db.getUnaffiliatedArticlesToReview(user).get(0);

        Review newReview = new Review();
        newReview.setSubmissionArticleID(article.getArticleID());

        Article contributeTo = db.getOwnArticles(user).get(0);

        newReview.setArticleOfReviwerID(contributeTo.getArticleID());

        assertTrue(db.reserverReview(newReview, user));
        assertTrue(db.reserverReview(newReview, user));
        assertTrue(db.reserverReview(newReview, user));

        assertThrows(SQLException.class, () -> {
            db.reserverReview(newReview, user);
        });

    }

    @Test
    public void test9test9test1submitReviewAndCritiques() throws SQLException, UserDoesNotExistException, InvalidAuthenticationException {
        assertTrue(db.emptyReviews(user).size() == 3);

        Critique critique = new Critique();
        critique.setDescription("TOO LONG BECAUSE OVER 250 CHARACTERS TOO LONG BECAUSE OVER 250 CHARACTERS" +
                " TOO LONG BECAUSE OVER 250 CHARACTERS TOO LONG BECAUSE OVER 250 CHARACTERS TOO LONG BECAUSE OVER 250" +
                " CHARACTERS TOO LONG BECAUSE OVER 250 CHARACTERS TOO LONG BECAUSE OVER 250 CHARACTERS" +
                " TOO LONG BECAUSE OVER 250 CHARACTERS ");

        Review review = db.emptyReviews(user).get(0);
        review.setVerdict("Champion");
        review.setSummary("My summary understanding");
        review.addCritique(critique);

        assertThrows(SQLException.class, () -> {
            db.submitReview(review, user);
        });

        assertTrue(db.emptyReviews(user).size() == 3);

        critique.setDescription("Now it is shorter and has the correct length");

        assertTrue(db.submitReview(review, user));

        assertTrue(db.emptyReviews(user).size() == 2);

        assertThrows(SQLException.class, () -> {
            db.submitReview(review, chiefEditor);
        });

    }

    @Test
    public void test9test9test2getArticlesAndStatus() throws SQLException, UserDoesNotExistException, InvalidAuthenticationException {
        List<Article> articles = db.getOwnArticleWithStatus(thirdUser);
        Article article = articles.get(0);
        assertEquals(3, article.getReviewsReceived());
        assertEquals(0,article.getReviewsContributed());

        assertEquals(1, articles.size());

        assertEquals(2, db.getOwnArticleWithStatus(user).size());

    }

    @Test
    public void test9test9test3getReviewsToRespondTo() throws SQLException, UserDoesNotExistException, InvalidAuthenticationException {
        List<Article> articles = db.getOwnArticleWithStatus(thirdUser);
        Article article = articles.get(0);

        List<Review> reviews = db.getInitialReviewsOfArticle(article, thirdUser);

        assertEquals(1, reviews.size());

        assertThrows(InvalidAuthenticationException.class, () -> {
            db.getInitialReviewsOfArticle(article, user);
        });

        articles = db.getOwnArticleWithStatus(user);
        Article article2 = articles.get(0);

        reviews = db.getInitialReviewsOfArticle(article2, user);

        assertEquals(0, reviews.size());

    }

    @Test
    public void test9test9test4getReviewsCritiques() throws SQLException, UserDoesNotExistException, InvalidAuthenticationException, ObjectDoesNotExistException {

        List<Article> articles = db.getOwnArticleWithStatus(thirdUser);
        Article article = articles.get(0);

        List<Review> reviews = db.getInitialReviewsOfArticle(article, thirdUser);

        reviews.get(0);

        assertEquals(1, db.getReviewCritiques(reviews.get(0), thirdUser).size());

        assertEquals("Now it is shorter and has the correct length", db.getReviewCritiques(reviews.get(0), thirdUser).get(0).getDescription());
    }

    @Test
    public void test9test9test5getArticlesNeedingFinalVerdicts() throws SQLException, UserDoesNotExistException, InvalidAuthenticationException, ObjectDoesNotExistException {

        db.articlesNeedingContributions(user);

        /*

        TODO

         */
    }

    @Test
    public void test9test9test6giveFinalVerdict() throws SQLException, UserDoesNotExistException, InvalidAuthenticationException, ObjectDoesNotExistException {
        Review review = new Review();
        review.setReviewID(1);
        review.setVerdict("TESTVERDICTFINAL");

        assertTrue(db.giveFinalVerdict(review,user));

        assertThrows(ObjectDoesNotExistException.class, () -> {
            db.giveFinalVerdict(review, thirdUser);
        });
    }

    @Test
    public void test9test9test7getArticlesNeedingRevision() throws SQLException, UserDoesNotExistException, InvalidAuthenticationException, ObjectDoesNotExistException {
        db.getArticlesNeedingRevision(user);
    }
}