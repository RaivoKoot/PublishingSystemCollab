package database_interface;

import com.mysql.cj.Session;
import exceptions.IncompleteInformationException;
import exceptions.InvalidAuthenticationException;
import exceptions.UniqueColumnValueAlreadyExists;
import exceptions.UserDoesNotExistException;
import main.SessionData;
import models.Journal;
import models.JournalEditor;
import models.User;

import java.sql.SQLException;

public class Test {

    public static void main(String[] args)  {
        User user = new User();
        user.setEmail("maxime.fontana@orange.fr");
        char[] psw = {'h','a','c','k','e','d'};
        user.setPassword(psw);

        /*
        boolean result = false;
        try {
            result = SessionData.db.changePassword(user, "hacked");
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        } catch (InvalidAuthenticationException e) {
            e.printStackTrace();
        } catch (IncompleteInformationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(result);


        char[] psw2 = {'p','a','p', 'a'};
        user.setPassword(psw2);

        Journal journal = new Journal();
        journal.setISSN("newwwwww");
        journal.setName("newwwwwww journal");

        try {
            System.out.println(SessionData.db.createJournal(journal, user));
        } catch (UniqueColumnValueAlreadyExists uniqueColumnValueAlreadyExists) {
            uniqueColumnValueAlreadyExists.printStackTrace();
        } catch (InvalidAuthenticationException e) {
            e.printStackTrace();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */

        // PROMOTING SOMEONE TO EDITOR
        JournalEditor user2 = new JournalEditor();
        user2.setEmail("toot@gmail.com");
        user2.setIssn("ABCDHSJKSD");
        user2.setChief(false);

        JournalEditor chief = new JournalEditor(user);
        chief.setIssn("ABCDHSJKSD");

        try {
            System.out.println(SessionData.db.promoteUserToEditor(user2, chief));
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        } catch (InvalidAuthenticationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
