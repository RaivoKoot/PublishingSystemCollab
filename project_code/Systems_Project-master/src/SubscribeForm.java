import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SubscribeForm extends JFrame {
    private JPanel SubscribePanel;
    private JLabel forename;
    private JLabel surname;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton registerButton;
    private JButton backward;
    private JLabel e_mail;
    private JTextField textField6;
    private JTextField textField7;
    private JPasswordField passwordField1;
    Connection con = null; // a Connection object
    Statement stmt = null;
    Statement stmt2 = null;

    public SubscribeForm() {

        add(SubscribePanel);
        setTitle("Subscription Form");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fname = textField1.getText();
                String sname = textField2.getText();
                String srole =  textField3.getText();
                String susername = textField4.getText();
                String spassword = textField5.getText();
                String e_mail = textField6.getText();
                int valueweneed = 1;

               // User newUser = ...;

               // registerUser(newUser);
                   try {
                        String DB="jdbc:mysql://stusql.dcs.shef.ac.uk/team035?user=team035&password=5455d7fb";
                        con = DriverManager.getConnection(DB);

                    try {
                        if (srole.equals("editor")) {
                            stmt = con.createStatement();
                            int inUsers = stmt.executeUpdate("INSERT INTO Users " + "VALUES (" + "'" + e_mail + "'" + ", " + "'" + fname + "'" + ", " + "'" +
                                    sname + "'" + ", " + "'" + "Unkwown" + "'" + ", " + "'" + spassword + "'" + "," + "'" + srole + "'" +
                                    "," + "'" + valueweneed + "'" + ")");

                            /*stmt2 = con.createStatement();
                            int inJournalEditors = stmt2.executeUpdate("INSERT INTO JournalEditors " + "VALUES (" + "'" + ISSN + "'" + ", " + "'" + e_mail + "'" + "," + "'"
                                    + valueweneed + "'" + ")"); */

                            //int [] registerSomeone = stmt.executeBatch();

                        }
                        else
                            {
                            /*stmt = con.createStatement();
                            int create = stmt.executeUpdate("INSERT INTO authors " + "VALUES (" + "'" + fname + "'" + ", " + "'" + sname + "'" + ", " + "'" +
                                    srole + "'" + ", " + "'" + susername + "'" + ", " + "'" + spassword + "'" + ")"); */
                        }


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

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WelcomeGUI theGUI = new WelcomeGUI();
                theGUI.setVisible(true);
                dispose();
            }
        });
    }


}
