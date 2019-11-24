import javax.swing.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class checkDB {
    public static <stmt> void main(String[] args) throws Exception, ClassNotFoundException, UnsupportedLookAndFeelException {
        Connection con = null; // a Connection object
        Statement stmt = null;
        Statement stmt2 = null;
        Statement stmt3 = null;
        Statement stmt4 = null;
        Statement stmt5 = null;
        Statement stmt6 = null;

        try {
            String DB="jdbc:mysql://stusql.dcs.shef.ac.uk/team035?user=team035&password=5455d7fb";
            con = DriverManager.getConnection(DB);

            try{

                stmt = con.createStatement();
                stmt2 = con.createStatement();
                stmt3 = con.createStatement();
                stmt4 = con.createStatement();
                stmt5 = con.createStatement();
                stmt6 = con.createStatement();



                //int dropit = stmt.executeUpdate("DROP TABLE editors");

                ResultSet Journals = stmt.executeQuery("SELECT * FROM Journals");
               // ResultSet authors = stmt2.executeQuery("SELECT * FROM authors");
                ResultSet Users = stmt3.executeQuery("SELECT * FROM Users");
                ResultSet Editors = stmt4.executeQuery("SELECT * FROM JournalEditors");
                ResultSet Submissions = stmt5.executeQuery("SELECT * FROM Submissions");
                ResultSet theauthors = stmt6.executeQuery("SELECT * FROM Authorships");


                while (Journals.next()) {
                    String ISSN = Journals.getString(1);
                    String JournalName = Journals.getString(2);

                    System.out.format("%s, %s\n", ISSN, JournalName);
                }
                Journals.close();
                System.out.println();

                /*while (authors.next()) {
                    String forename2 = authors.getString(1);
                    String surname2 = authors.getString(2);
                    String role2  = authors.getString(3);
                    String username2 = authors.getString(4);
                    String password2 = authors.getString(5);

                    System.out.format("%s, %s, %s, %s, %s\n", forename2, surname2, role2, username2, password2);
                }
                authors.close();
                System.out.println();*/

                while (Users.next()) {
                    String the_email = Users.getString(1);
                    String forename3 = Users.getString(2);
                    String surname3  = Users.getString(3);
                    String uni = Users.getString(4);
                    String password3 = Users.getString(5);
                    System.out.format("%s, %s, %s, %s, %s\n", the_email, forename3, surname3, uni, password3);
                }
                ResultSetMetaData rsmd = Users.getMetaData();
                String name5 = rsmd.getColumnLabel(5);
                //System.out.println(name5);

                Users.close();
                System.out.println();

                while (Editors.next()) {
                    String ISSN = Editors.getString(1);
                    String the_email2 = Editors.getString(2);
                    String isreallyChief = Editors.getString(3);
                    System.out.format("%s, , %s, %s\n", ISSN, the_email2, isreallyChief);
                }
                Editors.close();
                System.out.println();

                while(Submissions.next()){
                    String ID = Submissions.getString(1);
                    String title = Submissions.getString(2);
                    String abstractre = Submissions.getString(3);
                    String link = Submissions.getString(4);
                    System.out.format("%s, , %s, , %s, %s\n", ID, title, abstractre, link);
                }
                Submissions.close();
                System.out.println();


                while(theauthors.next()){
                    String the_email = theauthors.getString(1);
                    String submissionID = theauthors.getString(2);
                    String ismaine = theauthors.getString(3);
                    System.out.format("%s, , %s, %s\n", the_email, submissionID, ismaine);
                }
                theauthors.close();
                System.out.println();


            } catch (SQLException ex) {
                ex.printStackTrace();

            } finally {
                if (stmt != null)
                    stmt.close();
                if (stmt2 != null)
                    stmt2.close();
            }


        } catch (SQLException ex) {
            ex.printStackTrace();

        } finally {
            if (con != null) con.close();
        }
    }
}