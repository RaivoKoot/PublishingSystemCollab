package testing;

import database_interface.DataAccessController;
import exceptions.*;
import main.SessionData;
import models.Critique;
import models.JournalEditor;
import models.Review;

import java.sql.SQLException;

public class TestManual {

    public static void main(String[] args) throws SQLException, UserDoesNotExistException, InvalidAuthenticationException, ObjectDoesNotExistException, CantRemoveLastChiefEditorException, IncompleteInformationException {
        DataAccessController db = SessionData.db;
        JournalEditor alex = new JournalEditor();
        alex.setEmail("alex@");
        alex.setPassword("123");

        System.out.println(db.getJournalsByUser(alex).size());

        alex.setIssn("123");
        //System.out.println(db.deleteEditor(alex));

        JournalEditor raivo = new JournalEditor();
        raivo.setIssn("123");
        raivo.setEmail("raivo@gmail.com");
        raivo.setPassword("password");
        db.promoteUserToEditor(raivo, alex);
        db.makeChiefEditor(raivo, alex);
        System.out.println(db.deleteEditor(raivo));

        db.promoteUserToEditor(raivo, alex);
        System.out.println(db.deleteEditor(raivo));


        Review review = new Review();
        review.setReviewID(19);
        Critique critique1 = new Critique();
        Critique critique2 = new Critique();
        Critique critique3 = new Critique();
        critique1.setCritiqueID(40);
        critique2.setCritiqueID(41);
        critique3.setCritiqueID(42);
        critique1.setReply("frgreE");
        critique2.setReply("frgrh");
        critique3.setReply("aaaaaa");
        review.addCritique(critique1);
        review.addCritique(critique2);
        review.addCritique(critique3);

        review.setSubmissionArticleID(10);

        System.out.println(db.submitReviewResponse(review, alex));




    }
}
