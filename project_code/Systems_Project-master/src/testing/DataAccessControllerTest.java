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
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DataAccessControllerTest {

    static DatabaseTestingSetup dbSetup;
    static DataAccessController db;

    static User chiefEditor;
    static User user;
    static User nonexistentUser;
    static User incompleteInfo;
    static User badPasswordUser;
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

        incompleteInfo = new User();
        incompleteInfo.setEmail("incomplete@gmail.com");

        nonexistentUser = new User();
        nonexistentUser.setEmail("nonexisting@gmail.com");
        nonexistentUser.setPassword("blabla");

        badPasswordUser = new User();
        badPasswordUser.setEmail("user@gmail.com");
        badPasswordUser.setPassword("wrong");

    }

    @AfterClass
    public static void tearDown() throws Exception {
        dbSetup.dropTables();
    }

    @Test
    public void registerBaseUser() throws SQLException, UserAlreadyExistsException, IncompleteInformationException {

        assertTrue(db.registerBaseUser(chiefEditor));
        assertTrue(db.registerBaseUser(user));

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
    public void changePassword() throws IncompleteInformationException, SQLException, UserDoesNotExistException, InvalidAuthenticationException {
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
    public void validCredentials() throws SQLException, UserDoesNotExistException {
        assertTrue(db.validCredentials(user));

        assertThrows(UserDoesNotExistException.class, () -> {
            db.validCredentials(nonexistentUser);
        });

        assertFalse(db.validCredentials(badPasswordUser));
    }

    @Test
    public void createJournal() throws SQLException, UniqueColumnValueAlreadyExists, UserDoesNotExistException, InvalidAuthenticationException {
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
    public void promoteUserToEditor() throws SQLException, UserDoesNotExistException, InvalidAuthenticationException, IncompleteInformationException {
        JournalEditor newEditor = new JournalEditor(user);
        JournalEditor chief = new JournalEditor(chiefEditor);
        newEditor.setChief(false);
        newEditor.setIssn(journal.getISSN());

        assertThrows(InvalidAuthenticationException.class, () -> {
            db.promoteUserToEditor(newEditor, chief);
        });

        chief.setIssn(journal.getISSN());

        assertTrue(db.promoteUserToEditor(newEditor, chief));

        JournalEditor nonexistentChief = new JournalEditor(nonexistentUser);
        nonexistentChief.setIssn(journal.getISSN());

        assertThrows(UserDoesNotExistException.class, () -> {
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
}