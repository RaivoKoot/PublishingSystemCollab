package testing;

import database_interface.DataAccessController;
import database_interface.DatabaseConstants;
import exceptions.*;
import models.Journal;
import models.JournalEditor;
import models.User;
import org.junit.*;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class DataAccessControllerTest {

    static DatabaseTestingSetup dbSetup;
    static DataAccessController db;

    static User chiefEditor;
    static User user;
    static Journal journal;


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

        user.setEmail("user@gmail.com");
        user.setPassword("pswd");
        user.setSurname("user");
        user.setForenames("usero");
        user.setUniversity("Sheffield");

        journal = new Journal();
        journal.setName("Computer Science Journal");
        journal.setISSN("CSJ");

    }

    @AfterClass
    public static void tearDown() throws Exception {
        dbSetup.dropTables();
    }

    @Test
    public void registerBaseUser() throws SQLException, UserAlreadyExistsException, IncompleteInformationException {

            assertTrue(db.registerBaseUser(chiefEditor));
            assertTrue(db.registerBaseUser(user));

    }

    @Test
    public void changePassword() {
    }

    @Test
    public void validCredentials() {
    }

    @Test
    public void createJournal() throws SQLException, UniqueColumnValueAlreadyExists, UserDoesNotExistException, InvalidAuthenticationException {
        db.createJournal(journal, chiefEditor);
    }

    @Test
    public void promoteUserToEditor() throws SQLException, UserDoesNotExistException, InvalidAuthenticationException, IncompleteInformationException {
        JournalEditor newEditor = new JournalEditor(user);
        JournalEditor chief = new JournalEditor(chiefEditor);
        newEditor.setChief(false);
        newEditor.setIssn(journal.getISSN());

        chief.setIssn(journal.getISSN());

        db.promoteUserToEditor(newEditor, chief);
    }
}