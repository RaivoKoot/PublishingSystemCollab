package testing;

import database_interface.DatabaseConstants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseTestingSetup {

    private Connection connection;
    private PreparedStatement statement;
    private String databaseURL;
    private String databaseUser;
    private String databaseUserPassword;


    public DatabaseTestingSetup() {
        DatabaseConstants connectionInfo = new DatabaseConstants(true);
        this.databaseURL = connectionInfo.getDatabaseURL();
        this.databaseUser = connectionInfo.getDatabaseUser();
        this.databaseUserPassword = connectionInfo.getDatabaseUserPassword();
    }

    private void openConnection() throws SQLException {
        connection = DriverManager.getConnection(databaseURL, databaseUser, databaseUserPassword);
    }

    private void closeConnection() throws SQLException {
        if (connection != null)
            connection.close();
        if (statement != null)
            statement.close();
    }

    // Used for testing
    public void dropTables() throws SQLException {
        if (!databaseUserPassword.equals(""))
            return;

        String[] drops = {
                "DROP TABLE Critiques",
                "DROP TABLE Reviews",
                "DROP TABLE Authorships",
                "DROP TABLE EditionArticles",
                "DROP TABLE Articles",
                "DROP TABLE Pdfs;",
                "DROP TABLE Editions",
                "DROP TABLE Volumes",
                "DROP TABLE JournalEditors",
                "DROP TABLE Journals",
                "DROP TABLE Users",
        };

        try {
            openConnection();


            for (String drop : drops) {
                String sqlQuery = drop;
                statement = connection.prepareStatement(sqlQuery);

                statement.executeUpdate();
            }


        } finally {
            closeConnection();
        }
    }

    // Used for testing
    public void createTables() throws SQLException {
        if (!databaseUserPassword.equals(""))
            return;

        try {
            openConnection();

            String[] creates = {
                    "CREATE TABLE Journals (\n" +
                            "\tISSN VARCHAR(20) PRIMARY KEY,\n" +
                            "\tname VARCHAR(100) UNIQUE NOT NULL\n" +
                            "\t\n" +
                            ");",
                    "CREATE TABLE Volumes (\n" +
                            "\tvolumeNum INT,\n" +
                            "\tISSN VARCHAR(20),\n" +
                            "\tpublicationYear SMALLINT,\n" +
                            "\tPRIMARY KEY (volumeNum, ISSN),\n" +
                            "\tFOREIGN KEY (ISSN) REFERENCES Journals(ISSN)\n" +
                            "\t\tON DELETE CASCADE\n" +
                            ");",
                    "CREATE TABLE Editions (\n" +
                            "\teditionID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                            "\teditionNum TINYINT,\n" +
                            "\tpublicationMonth TINYINT,\n" +
                            "\tvolumeNum INT,\n" +
                            "\tisPublic BOOLEAN DEFAULT FALSE,\n" +
                            "\tISSN VARCHAR(20),\n" +
                            "\tFOREIGN KEY (volumeNum, ISSN) REFERENCES Volumes(volumeNum, ISSN)\n" +
                            "\t\tON DELETE CASCADE,\n" +
                            "\t\t\n" +
                            "\tCHECK (editionNum < 7)\n" +
                            ");",
                    "CREATE TABLE Users (\n" +
                            "\temail VARCHAR(80) PRIMARY KEY,\n" +
                            "\ttitle VARCHAR(5), \n" +
                            "\tforenames VARCHAR(150),\n" +
                            "\tsurname VARCHAR(120),\n" +
                            "\tuniversity VARCHAR(100),\n" +
                            "\tpassword VARCHAR(200) NOT NULL\n" +
                            ");",
                    "CREATE TABLE JournalEditors (\n" +
                            "\tISSN VARCHAR(20),\n" +
                            "\temail VARCHAR(80),\n" +
                            "\tisChief BOOL NOT NULL DEFAULT FALSE,\n" +
                            "\tPRIMARY KEY(ISSN, email),\n" +
                            "\tFOREIGN KEY(email) REFERENCES Users(email)\n" +
                            "\t\tON DELETE NO ACTION,\n" +
                            "\tFOREIGN KEY(ISSN) REFERENCES Journals(ISSN)\n" +
                            "\t\tON DELETE CASCADE\n" +
                            ");",
                    "CREATE TABLE Pdfs (\n" +
                            "\tpdfID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                            "\tpdf MEDIUMBLOB NOT NULL\n" +
                            ");",
                    "CREATE TABLE Articles (\n" +
                            "\tarticleID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                            "\ttitle VARCHAR(1000) NOT NULL,\n" +
                            "\tabstract VARCHAR(2000) NOT NULL,\n" +
                            "\tcontent VARCHAR(400) NOT NULL,\n" +
                            "\tisFinal BOOL DEFAULT FALSE,\n" +
                            "\tisAccepted BOOL DEFAULT FALSE,\n" +
                            "\tISSN VARCHAR(20),\n" +
                            "\tpdfID INT NOT NULL,\n" +
                            "\t\n" +
                            "\tFOREIGN KEY(ISSN) REFERENCES Journals(ISSN)\n" +
                            "\t\tON DELETE CASCADE,\n" +
                            "\tFOREIGN KEY(pdfID) REFERENCES Pdfs(pdfID)\n" +
                            "\t\tON DELETE NO ACTION\n" +
                            ");",
                    "CREATE TABLE Authorships (\n" +
                            "\temail VARCHAR(80),\n" +
                            "\tarticleID INT,\n" +
                            "\tisMain BOOL NOT NULL DEFAULT FALSE,\n" +
                            "\tPRIMARY KEY(email, articleID),\n" +
                            "\tFOREIGN KEY(email) REFERENCES Users(email)\n" +
                            "\t\tON DELETE NO ACTION,\n" +
                            "\tFOREIGN KEY(articleID) REFERENCES Articles(articleID)\n" +
                            "\t\tON DELETE CASCADE\n" +
                            ");",
                    "CREATE TABLE EditionArticles (\n" +
                            "\teditionArticleID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                            "\tarticleID INT NOT NULL,\n" +
                            "\teditionID INT NOT NULL,\n" +
                            "\tstartingPage INT UNSIGNED,\n" +
                            "\tendingPage INT UNSIGNED,\n" +
                            "\tFOREIGN KEY(articleID) REFERENCES Articles(articleID)\n" +
                            "\t\tON DELETE CASCADE,\n" +
                            "\tFOREIGN KEY(editionID) REFERENCES Editions(editionID)\n" +
                            "\t\tON DELETE CASCADE\n" +
                            ");",
                    "CREATE TABLE Reviews (\n" +
                            "\treviewID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                            "\t\n" +
                            "\tsummary VARCHAR(2000),\n" +
                            "\tverdict VARCHAR(25),\n" +
                            "\tisFinal BOOL DEFAULT FALSE,\n" +
                            "\t\n" +
                            "\tsubmissionID INT,\n" +
                            "\tarticleOfReviewerID INT,\n" +
                            "\treviewerEmail VARCHAR(80) NOT NULL,\n" +
                            "\thasResponse BOOL DEFAULT FALSE,\n" +
                            "\t\n" +
                            "\tFOREIGN KEY(submissionID) REFERENCES Articles(articleID)\n" +
                            "\t\tON DELETE SET NULL,\n" +
                            "\tFOREIGN KEY(reviewerEmail) REFERENCES Users(email)\n" +
                            "\t\tON DELETE NO ACTION,\n" +
                            "\tFOREIGN KEY(articleOfReviewerID) REFERENCES Articles(articleID)\n" +
                            "\t\tON DELETE SET NULL\n" +
                            ");",
                    "CREATE TABLE Critiques (\n" +
                            "\tcritiqueID INT PRIMARY KEY AUTO_INCREMENT,\n" +
                            "\treviewID INT NOT NULL,\n" +
                            "\tdescription VARCHAR(250) NOT NULL,\n" +
                            "\treply VARCHAR(250) DEFAULT NULL,\n" +
                            "\tFOREIGN KEY(reviewID) REFERENCES Reviews(reviewID)\n" +
                            "\t\tON DELETE CASCADE\n" +
                            ");",
                    "ALTER TABLE `Editions` ADD UNIQUE `unique_index`(`editionNum`, `volumeNum`, `ISSN`);",
                    "ALTER TABLE `Volumes` ADD UNIQUE `unique_index2`(`ISSN`, `publicationYear`);"
            };

            for (String create : creates) {
                statement = connection.prepareStatement(create);

                statement.executeUpdate();

            }


        } finally {
            closeConnection();
        }
    }
}
