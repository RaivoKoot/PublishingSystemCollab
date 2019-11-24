package database_interface;

import exceptions.IncompleteInformationException;
import exceptions.UserAlreadyExistsException;
import main.SessionData;
import models.User;

import java.sql.SQLException;

public class Testing {

    public static void main(String[] args){
        User user = new User();
        user.setPassword("passworddd");
        user.setEmail("raiovo@yahoo.com");
        user.setForenames("Raivo Raaivo");
        user.setSurname("Koot");
        user.setUniversity("Sheffield");

        try {
            SessionData.db.registerBaseUser(user);
        } catch (UserAlreadyExistsException e) {
            e.printStackTrace();
        } catch (IncompleteInformationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
