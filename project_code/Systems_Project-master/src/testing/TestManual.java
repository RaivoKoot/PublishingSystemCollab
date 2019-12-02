package testing;

import database_interface.DataAccessController;
import exceptions.*;
import main.SessionData;
import models.JournalEditor;

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


    }
}
