package database_interface;

public class DatabaseConstants {

    private String databaseURL = "jdbc:mysql://stusql.dcs.shef.ac.uk/team035";
    private String databaseUser = "team035";
    private String databaseUserPassword = "5455d7fb";

    public DatabaseConstants(Boolean isLocalhost){
        if(isLocalhost){
            databaseURL = "jdbc:mysql://localhost/publishing_system";
            databaseUser = "root";
            databaseUserPassword = "";
        }
    }
}
