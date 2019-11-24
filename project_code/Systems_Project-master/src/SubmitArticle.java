import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SubmitArticle extends JFrame {
    private JPanel SubmitArticle;
    private JTextField abstracte;
    private JTextField pdflink;
    private JTextField e_maile;
    private JTextField forenameI;
    private JButton backward;
    private JTextField chosen_password;
    private JTextField surnameI;
    private JLabel surname;
    private JLabel forename;
    private JButton submitButton;
    private JTextField Titre;
    Connection con = null; // a Connection object
    Statement stmt = null;
    Statement stmt2 = null;
    Statement stmt3 = null;
    int valueweneed = 3;
    int n = 5;


    public SubmitArticle() {
        add(SubmitArticle);
        setTitle("Subscription Form");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainScreen main_screen = new MainScreen();
                main_screen.setVisible(true);
                dispose();
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = Titre.getText();
                String fname = forenameI.getText();
                String sname = surnameI.getText();
                String pdflinke = pdflink.getText();
                String password = chosen_password.getText();
                String e_mail = e_maile.getText();
                String abstracter = abstracte.getText();
                n++;


                try {
                    String DB = "jdbc:mysql://stusql.dcs.shef.ac.uk/team035?user=team035&password=5455d7fb";
                    con = DriverManager.getConnection(DB);

                    try {
                        stmt = con.createStatement();
                        int inUsers = stmt.executeUpdate("INSERT INTO Users " + "VALUES (" + "'" + e_mail + "'" + ", " + "'" + fname + "'" + ", " + "'" +
                                sname + "'" + ", " + "'" + "Unkwown" + "'" + ", " + "'" + password + "'" + "," + "'author'" +
                                "," + "'" + valueweneed + "'" + ")");

                        stmt3 = con.createStatement();
                        int inSubmissions = stmt3.executeUpdate("INSERT INTO Submissions " + "VALUES (" + "'" + n + "'" + ", '" + title + "'" + "," +
                                "'" + abstracter + "'" + ", '" + pdflinke +  "')");

                        stmt2 = con.createStatement();
                        int inAuthors = stmt2.executeUpdate("INSERT INTO Authorships " + "VALUES (" + "'" + e_mail + "'" + ", '" + n + "'" + "," +
                                "'" + 1 + "'" + ")");


                        //int [] registerSomeone = stmt.executeBatch();

                    } catch (SQLException ex) {
                        ex.printStackTrace();

                    } finally {
                        if (stmt != null)
                            stmt.close();
                    }


                } catch (SQLException ex) {
                    ex.printStackTrace();

                } finally {
                    if (con != null) {
                        try {
                            con.close();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });


    }

    ;
}




