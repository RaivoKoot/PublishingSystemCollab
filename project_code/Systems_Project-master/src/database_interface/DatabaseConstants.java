package database_interface;

public class DatabaseConstants {

    private String databaseURL = "jdbc:mysql://stusql.dcs.shef.ac.uk/team035?allowMultiQueries=true";
    private String databaseUser = "team035";
    private String databaseUserPassword = "5455d7fb";

    public DatabaseConstants(Boolean isLocalhost){
        if(isLocalhost){
            databaseURL = "jdbc:mysql://localhost/publishing_system?allowMultiQueries=true";
            databaseUser = "root";
            databaseUserPassword = "";
        }
    }

    public String getDatabaseURL() {
        return databaseURL;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabaseUserPassword() {
        return databaseUserPassword;
    }
}
