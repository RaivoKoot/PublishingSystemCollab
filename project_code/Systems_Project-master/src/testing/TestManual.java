package testing;

import database_interface.DataAccessController;
import exceptions.*;
import main.SessionData;
import models.*;

import java.sql.SQLException;

public class TestManual {

    public static void main(String[] args) throws SQLException, UserDoesNotExistException, InvalidAuthenticationException, ObjectDoesNotExistException, CantRemoveLastChiefEditorException, IncompleteInformationException {
        DataAccessController db = SessionData.db;
        Article art = new Article();
        art.setArticleID(2);
        art.setTitle("Raivos article");
        art.setSummary("frgr");
        art.setContent("Don't be weird");
        User raivo = new User();
        raivo.setPassword("123");
        raivo.setEmail("raivo");
        System.out.println(db.submitFinalArticleVersion(art, raivo));
    }
}
