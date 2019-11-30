import javax.swing.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class main {
    public static <stmt> void main(String[] args) throws Exception, ClassNotFoundException, UnsupportedLookAndFeelException {

        Connection con = null; // a Connection object
        Statement stmt = null;

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                WelcomeGUI theGUI = new WelcomeGUI();
                theGUI.setVisible(true);
            }
        });

        try {
            String DB="jdbc:mysql://stusql.dcs.shef.ac.uk/team035?user=team035&password=5455d7fb";
            con = DriverManager.getConnection(DB);

            try{
                con.setAutoCommit(false);
                stmt = con.createStatement();

                //stmt.addBatch("DROP TABLE IF EXISTS Journals, Volumes, Editions, Users, JournalEditors, Authorships, AcceptedArticles,Reviews, Critiques, Submissions");

                //stmt.addBatch("DELETE FROM Users");
/*
                stmt.addBatch("CREATE TABLE editors ("
                        + "FORENAME VARCHAR(45) NOT NULL,"
                        + "SURNAME VARCHAR(45) NOT NULL,"
                        + "ROLE VARCHAR(56) NOT NULL,"
                        + "USERNAME VARCHAR(45) NOT NULL,"
                        + "PASSWORD VARCHAR(45) NOT NULL);");

                stmt.addBatch("CREATE TABLE authors("
                        + "FORENAME VARCHAR(45) NOT NULL,"
                        + "SURNAME VARCHAR(45) NOT NULL,"
                        + "ROLE VARCHAR(56) NOT NULL,"
                        + "USERNAME VARCHAR(45) NOT NULL,"
                        + "PASSWORD VARCHAR(45) NOT NULL);");
*/
                // from this point on, real DB

                stmt.addBatch("CREATE TABLE Journals (\n" +
                        "\tISSN VARCHAR(20) PRIMARY KEY,\n" +
                        "\tname VARCHAR(100) UNIQUE NOT NULL\n" +
                        "\t\n" +
                        ");");

                stmt.addBatch("CREATE TABLE Volumes (\n" +
                        "\tvolumeNum INT,\n" +
                        "\tISSN VARCHAR(20),\n" +
                        "\tpublicationYear SMALLINT,\n" +
                        "\tPRIMARY KEY (volumeNum, ISSN),\n" +
                        "\tFOREIGN KEY (ISSN) REFERENCES Journals(ISSN)\n" +
                        "\t\tON DELETE CASCADE\n" +
                        ");");

                stmt.addBatch("CREATE TABLE Editions (\n" +
                        "\teditionID INT PRIMARY KEY,\n" +
                        "\teditionNum TINYINT,\n" +
                        "\tpublicationMonth TINYINT,\n" +
                        "\tvolumeNum INT,\n" +
                        "\tISSN VARCHAR(20),\n" +
                        "\tFOREIGN KEY (volumeNum, ISSN) REFERENCES Volumes(volumeNum, ISSN)\n" +
                        "\t\tON DELETE CASCADE\n" +
                        ");");

                stmt.addBatch("CREATE TABLE Users(\n" +
                        "\temail VARCHAR(80) PRIMARY KEY,\n" +
                                "\tforenames VARCHAR(150),\n" +
                                "\tsurname VARCHAR(120),\n" +
                                "\tuniversity VARCHAR(100),\n" +
                                "\tpassword VARCHAR(200) NOT NULL,\n" +
                                "\trole VARCHAR(200) NOT NULL,\n" +
                                "\tisSuperuser BOOLEAN DEFAULT FALSE NOT NULL\n" +
                                ");");

                stmt.addBatch("CREATE TABLE JournalEditors (\n" +
                        "\tISSN VARCHAR(20),\n" +
                        "\temail VARCHAR(80),\n" +
                        "\tisChief BOOL NOT NULL DEFAULT FALSE,\n" +
                        "\tPRIMARY KEY(ISSN, email),\n" +
                        "\tFOREIGN KEY(email) REFERENCES Users(email)\n" +
                        "\t\tON DELETE CASCADE,\n" +
                        "\tFOREIGN KEY(ISSN) REFERENCES Journals(ISSN)\n" +
                        "\t\tON DELETE CASCADE\n" +
                        ");");

                stmt.addBatch("CREATE TABLE Submissions (\n" +
                        "\tsubmissionID INT PRIMARY KEY,\n" +
                        "\ttitle VARCHAR(1000) NOT NULL,\n" +
                        "\tabstract VARCHAR(2000) NOT NULL,\n" +
                        "\tdraftLink VARCHAR(400) NOT NULL\n" +
                        ");");

                stmt.addBatch("CREATE TABLE Authorships (\n" +
                        "\temail VARCHAR(80),\n" +
                        "\tsubmissionID INT,\n" +
                        "\tisMain BOOL NOT NULL DEFAULT FALSE,\n" +
                        "\tPRIMARY KEY(email, submissionID),\n" +
                        "\tFOREIGN KEY(email) REFERENCES Users(email)\n" +
                        "\t\tON DELETE NO ACTION,\n" +
                        "\tFOREIGN KEY(submissionID) REFERENCES Submissions(submissionID)\n" +
                        "\t\tON DELETE CASCADE\n" +
                        ");");

                stmt.addBatch("CREATE TABLE AcceptedArticles (\n" +
                        "\tarticleID INT PRIMARY KEY,\n" +
                        "\tstartingPage INT UNSIGNED,\n" +
                        "\tendingPage INT UNSIGNED,\n" +
                        "\tarticleFile MEDIUMBLOB NOT NULL,\n" +
                        "\teditionID INT,\n" +
                        "\tsubmissionID INT NOT NULL,\n" +
                        "\tFOREIGN KEY(submissionID) REFERENCES Submissions(submissionID)\n" +
                        "\t\tON DELETE CASCADE,\n" +
                        "\tFOREIGN KEY(editionID) REFERENCES Editions(editionID)\n" +
                        "\t\tON DELETE SET NULL\n" +
                        ");");

                stmt.addBatch("CREATE TABLE Reviews (\n" +
                        "\treviewID INT PRIMARY KEY,\n" +
                        "\tsummary VARCHAR(2000) NOT NULL,\n" +
                        "\tverdict VARCHAR(20) NOT NULL,\n" +
                        "\tsubmissionID INT NOT NULL,\n" +
                        "\treviewerEmail VARCHAR(80) NOT NULL,\n" +
                        "\tarticleOfReviewer INT,\n" +
                        "\t\n" +
                        "\tFOREIGN KEY(submissionID) REFERENCES Submissions(submissionID)\n" +
                        "\t\tON DELETE CASCADE,\n" +
                        "\tFOREIGN KEY(reviewerEmail) REFERENCES Users(email)\n" +
                        "\t\tON DELETE CASCADE,\n" +
                        "\tFOREIGN KEY(articleOfReviewer) REFERENCES Submissions(submissionID)\n" +
                        "\t\tON DELETE SET NULL\n" +
                        ");");

                stmt.addBatch("CREATE TABLE Critiques (\n" +
                        "\tcritiqueID INT PRIMARY KEY,\n" +
                        "\treviewID INT NOT NULL,\n" +
                        "\tdescription VARCHAR(250) NOT NULL,\n" +
                        "\treply VARCHAR(250) DEFAULT NULL,\n" +
                        "\tFOREIGN KEY(reviewID) REFERENCES Reviews(reviewID)\n" +
                        "\t\tON DELETE CASCADE\n" +
                        ");\n");

                int [] createDB = stmt.executeBatch();
                con.commit();


            } catch (SQLException ex) {
                ex.printStackTrace();

            } finally {
                if (stmt != null)
                    stmt.close();
                    con.setAutoCommit(true);
            }


        } catch (SQLException ex) {
            ex.printStackTrace();

        } finally {
            if (con != null) con.close();
        }


    }
}
