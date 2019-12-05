package database_interface;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

import exceptions.*;
import models.*;

public interface DatabaseInterface {

    /**
     * Registers the given user as a base user in the system
     *
     * @param newUser must include email and password
     * @return success or not
     * @throws UserAlreadyExistsException
     * @throws IncompleteInformationException
     * @precondition a user with that email must not exist yet
     */
    public boolean registerBaseUser(User newUser) throws UserAlreadyExistsException, IncompleteInformationException, SQLException;

    /**
     * attempts to change the password of the given user
     *
     * @param user        must include email and old password
     * @param newPassword
     * @return success or not
     * @precondition the user exists in the system
     */
    public boolean changePassword(User user, String newPassword) throws UserDoesNotExistException, IncompleteInformationException, InvalidAuthenticationException, SQLException;

    /**
     * Checks whether a given user with that email and password exist
     *
     * @param user must include email and password
     * @return success or not
     * @precondition the user exists in the system
     */
    public boolean validCredentials(User user) throws SQLException, UserDoesNotExistException;

    /**
     * @return a list of all registered journals
     */
    public ArrayList<Journal> getAllJournals() throws SQLException;

    /**
     * @param journal must include ISSN
     * @return a list of all volumes belonging to that journal
     * @throws ObjectDoesNotExistException
     * @precondition the given journal exists
     */
    public ArrayList<Volume> getAllJournalVolumes(Journal journal) throws ObjectDoesNotExistException, SQLException;

    /**
     * @param volume must include volNum and ISSN
     * @return a list of all editions belonging to that volume
     * @throws ObjectDoesNotExistException
     * @precondition the given volume exists
     */
    public ArrayList<Edition> getAllVolumeEditions(Volume volume) throws SQLException;

    /**
     * @param edition must include volumeNum and editionNum
     * @return a list of all articles in this edition
     * @throws ObjectDoesNotExistException
     * @precondition the given edition exists
     */
    public ArrayList<EditionArticle> getallEditionArticles(Edition edition) throws ObjectDoesNotExistException, SQLException;

    /**
     * Creates a new journal and appoints chiefEditor as the main and first editor
     *
     * @param newJournal  must include UNIQUE ISSN and UNIQUE name
     * @param chiefEditor must include ISSN, email and password
     * @return success or not
     * @throws UserDoesNotExistException
     * @throws InvalidAuthenticationException
     * @throws UniqueColumnValueAlreadyExists if the ISSN or Journal name is already
     *                                        in use
     * @precondition the user chiefEditor exists
     */
    public boolean createJournal(Journal newJournal, User chiefEditor)
            throws UserDoesNotExistException, InvalidAuthenticationException, UniqueColumnValueAlreadyExists, SQLException;

    /**
     * Using a chief editor's credentials, appoints a user as an editor for a
     * journal
     *
     * @param newEditor    must include journal.ISSN and email
     * @param journalChief must include email and password
     * @return success or not
     * @throws UserDoesNotExistException
     * @throws InvalidAuthenticationException
     * @precondition the user newEditor must exist and journalChief must exist and
     * be a chief editor of the journal in newEditor
     */
    public boolean promoteUserToEditor(JournalEditor newEditor, JournalEditor journalChief)
            throws UserDoesNotExistException, InvalidAuthenticationException, SQLException, IncompleteInformationException;

    /**
     * Using the credentials of editor, creates a new volume with the next in line
     * volume number for that journal
     *
     * @param journal         must include ISSN
     * @param editor          must include email and password
     * @param publicationYear
     * @return the newlyu created volume
     * @throws ObjectDoesNotExistException
     * @throws InvalidAuthenticationException
     */
    public boolean createNextVolume(Journal journal, JournalEditor editor, int publicationYear)
            throws ObjectDoesNotExistException, InvalidAuthenticationException, SQLException;

    /**
     * Using the credentials of editor, creates a new edition with the next in line
     * edition number for that volume.
     *
     * @param volume           must include volNum and ISSN
     * @param editor           must include email and journal.ISSN
     * @param publicationMonth
     * @return the newly created edition
     * @throws ObjectDoesNotExistException
     * @throws InvalidAuthenticationException
     * @precondition editor and volume must exist
     */
    public Edition createNextEdition(Volume volume, JournalEditor editor, String publicationMonth)
            throws ObjectDoesNotExistException, InvalidAuthenticationException, VolumeFullException, SQLException, NoMoreEditionsAllowedInVolumeException, LastEditionNotFinishedException;

    /**
     * Using the credentials of author, creates a new submission and appoints thi
     * user as the main author for that submission
     *
     * @param submission must include title, summary and articleContent
     * @param author     must include email and password
     * @return returns the submission object with a value for submissionID of the
     * newly created submission
     * @throws UserDoesNotExistException
     * @throws InvalidAuthenticationException
     * @precondition the user passed exists in the database
     * @postcondition a new submission with the given values has been inserted into
     * the database
     */
    public Article submitArticle(Article submission, User author)
            throws UserDoesNotExistException, InvalidAuthenticationException, SQLException, IncompleteInformationException;

    /**
     * Using the credentials of mainAuthor, appoints newAuthor as a co-author to the
     * submission
     *
     * @param article    only needs to include articleID
     * @param newAuthor  only needs to include email
     * @param mainAuthor only needs to include email and password
     * @return successful or not
     * @throws UserDoesNotExistException
     * @throws ObjectDoesNotExistException
     * @throws InvalidAuthenticationException
     * @precondition all three objects exist in the database
     * @postcondition the newAuthor user has been registered as a co author for that
     */
    public boolean addCoAuthor(Article article, User newAuthor, User mainAuthor)
            throws UserDoesNotExistException, ObjectDoesNotExistException, InvalidAuthenticationException, SQLException;

    /**
     * Using the credentials of chiefAuthentication, promotes editor to a chief editor
     *
     * @param editor              must include email and issn
     * @param chiefAuthentication must include email, password and issn
     * @return successful or not
     * @throws UserDoesNotExistException
     * @throws SQLException
     * @throws InvalidAuthenticationException
     * @precondition editor must be an editor of the journal
     * @postcondition editor has been promoted to a chief editor of the journal
     */
    public boolean makeChiefEditor(JournalEditor editor, JournalEditor chiefAuthentication) throws IncompleteInformationException, UserDoesNotExistException, InvalidAuthenticationException, SQLException;

    /**
     * @param user must include email and password
     * @return the list of articles this user is an author of that need to still contribute some reviews
     * @throws UserDoesNotExistException
     * @throws InvalidAuthenticationException
     * @throws SQLException
     */
    public ArrayList<Article> articlesNeedingContributions(User user) throws UserDoesNotExistException, InvalidAuthenticationException, SQLException;

    /**
     * @param user must include email and password
     * @return a list of all reviews by the given user that still require future action
     * @throws UserDoesNotExistException
     * @throws InvalidAuthenticationException
     * @throws SQLException
     */
    public ArrayList<Review> emptyReviews(User user) throws UserDoesNotExistException, InvalidAuthenticationException, SQLException;

    /**
     * @param articleID
     * @return
     * @throws ObjectDoesNotExistException
     * @throws SQLException
     */
    public Article getArticleInfo(int articleID) throws ObjectDoesNotExistException, SQLException;


    /**
     * @param user
     * @return the list of articles you are an
     * @throws UserDoesNotExistException
     * @throws InvalidAuthenticationException
     * @throws SQLException
     */
    public ArrayList<Article> getOwnArticles(User user) throws UserDoesNotExistException, InvalidAuthenticationException, SQLException;

    /**
     * @param user
     * @return A list of articles that still need to be reviewed that have not been authored by people you are
     * affiliated with
     * @throws UserDoesNotExistException
     * @throws InvalidAuthenticationException
     * @throws SQLException
     */
    public ArrayList<Article> getUnaffiliatedArticlesToReview(User user) throws UserDoesNotExistException, InvalidAuthenticationException, SQLException;

    /**
     * Reserves one of an article's three open review spots for the given user
     *
     * @param review must include submissionID, articleOfReviewerID
     * @return
     * @throws InvalidAuthenticationException
     * @throws SQLException
     */
    public boolean reserverReview(Review review, User user) throws InvalidAuthenticationException, SQLException, UserDoesNotExistException;

    /**
     * submits the given review and its critiques
     *
     * @param review must include verdict, summary and the list of critiques
     * @param user   must be the user that owns the empty review with that reviewID
     * @return
     * @throws InvalidAuthenticationException
     * @throws UserDoesNotExistException
     * @throws SQLException
     */
    public boolean submitReview(Review review, User user) throws InvalidAuthenticationException, UserDoesNotExistException, SQLException;

    /**
     * For the given user gets their articles along with the following info:
     * publishers decision, review received, contributions, finalReviews received,
     * responses to received reviews given, and whether the article is final/revised by the author
     *
     * @return
     * @throws InvalidAuthenticationException
     * @throws UserDoesNotExistException
     * @throws SQLException
     */
    public ArrayList<Article> getOwnArticleWithStatus(User currentUser) throws InvalidAuthenticationException, UserDoesNotExistException, SQLException;

    /**
     * For the given user gets the journals they are an editor of
     *
     * @param user
     * @return
     * @throws InvalidAuthenticationException
     * @throws UserDoesNotExistException
     * @throws SQLException
     */

    public ArrayList<Journal> getJournalsByUser(User user) throws InvalidAuthenticationException, UserDoesNotExistException, SQLException;

    /**
     * Deletes an editorship from the editor-journal linker table
     *
     * @param journalEditor
     * @return
     * @throws InvalidAuthenticationException
     * @throws UserDoesNotExistException
     * @throws SQLException
     * @throws ObjectDoesNotExistException
     */
    public boolean deleteEditor(JournalEditor journalEditor) throws InvalidAuthenticationException, UserDoesNotExistException, SQLException, ObjectDoesNotExistException, CantRemoveLastChiefEditorException;

    /**
     * Given an article, returns the reviews that can be responded to. User must be the main author
     *
     * @param article
     * @return
     * @throws InvalidAuthenticationException
     * @throws UserDoesNotExistException
     * @throws SQLException
     */
    public ArrayList<Review> getInitialReviewsOfArticle(Article article, User authentication) throws InvalidAuthenticationException, UserDoesNotExistException, SQLException;

    /**
     * @param review
     * @param user
     * @return
     * @throws InvalidAuthenticationException
     * @throws ObjectDoesNotExistException
     * @throws SQLException
     */
    public boolean submitReviewResponse(Review review, User user) throws InvalidAuthenticationException, ObjectDoesNotExistException, SQLException, UserDoesNotExistException;

    /**
     * Updates the title, summary and content of a review and sets isFinal to true
     *
     * @param article
     * @param mainAuthor
     * @return
     * @throws InvalidAuthenticationException
     * @throws ObjectDoesNotExistException
     * @throws SQLException
     */
    public boolean submitFinalArticleVersion(Article article, User mainAuthor) throws InvalidAuthenticationException, ObjectDoesNotExistException, SQLException, UserDoesNotExistException;

    /**
     * Gets a list of articles submitted to the specified journal that need to be accepted or rejected by an editor afer they have been fully reviewed by reviewers
     * @param journal
     * @return
     * @throws InvalidAuthenticationException
     * @throws ObjectDoesNotExistException
     * @throws SQLException
     * @throws UserDoesNotExistException
     */
    public ArrayList<Article> getJournalArticlesNeedingEditorDecision(Journal journal, User editor) throws InvalidAuthenticationException, ObjectDoesNotExistException, SQLException, UserDoesNotExistException;

    /**
     * gets the three verdicts from the reviews of an article
     * @param article
     * @param editor
     * @return
     * @throws InvalidAuthenticationException
     * @throws ObjectDoesNotExistException
     * @throws SQLException
     * @throws UserDoesNotExistException
     */
    public ArrayList<String> getAllVerdictsForArticle(Article article, User editor) throws InvalidAuthenticationException, ObjectDoesNotExistException, SQLException, UserDoesNotExistException;

    /**
     * sets isAcceptedOrRejected to either true or false depending on the Editor's choice
     * if accepted or rejected, delete reviews, critiques(should be deleted automatically when reviews deleted) and autorships
     * if rejected also delete article (deleting article should automatically delete authorship, reviews, crritiques)
     * @param article
     * @param editor
     * @throws InvalidAuthenticationException
     * @throws ObjectDoesNotExistException
     * @throws SQLException
     * @throws UserDoesNotExistException
     */
    public void setIsAcceptedOrRejected(Article article, User editor) throws InvalidAuthenticationException, ObjectDoesNotExistException, SQLException, UserDoesNotExistException;

    /**
     * delete user if they dont have an authorship, an editorship and no non-final reviews
     * @param user
     * @throws InvalidAuthenticationException
     * @throws ObjectDoesNotExistException
     * @throws SQLException
     * @throws UserDoesNotExistException
     */
    public void deleteUser(User user) throws InvalidAuthenticationException, ObjectDoesNotExistException, SQLException, UserDoesNotExistException;

    /**
     * checks if edition has at least 3 articles, checks if user is main editor for journal
     * and then set isPublished to true
     * @param edition
     * @param mainEditor
     */
    public void publishEdition(Edition edition, User mainEditor) throws InvalidAuthenticationException, ObjectDoesNotExistException, SQLException, UserDoesNotExistException;


    /**
     * gets the accepted articles for that journal that are not yet assigned to an edition
     * aka EditionArticle does not have the articleID in there
     * @param journal
     * @param editor
     * @return
     */
    public ArrayList<Article> getAcceptedArticlesByJournal(Journal journal, User editor) throws InvalidAuthenticationException, ObjectDoesNotExistException, SQLException, UserDoesNotExistException;

    /**
     * gets the latest Edition of the specified journal
     * use max function to return the biggest page number of all the EditionArticles of that journal
     * set currentLastPage of the edition object to the max
     *
     * @param journal
     * @param editor
     * @return
     */
    public Edition getLatestEdition(Journal journal, User editor);

    /**
     * creates an EditionArticle object
     * check whether the current edition is full (throw exception saying that the chief editor needs to create new edition)
     */
    public EditionArticle assignArticleToEdition(Article article, User editor);

}

