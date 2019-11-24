package main;

import database_interface.DataAccessController;
import database_interface.DatabaseConstants;
import models.User;

public class SessionData {
    public static User currentUser = null;
    public static DataAccessController db = new DataAccessController(new DatabaseConstants(false));

}
