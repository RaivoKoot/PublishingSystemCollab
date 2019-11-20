import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Appoint extends JFrame {
    private JPanel AppointPanel;
    private JTextField forename;
    private JTextField surname;
    private JTextField e_mail_address;
    private JTextField t_ISSN;
    private JTextField t_password;
    private JButton appointButton;
    private JButton doneButton;
    private JButton backward;
    private JLabel Success;
    Connection con = null; // a Connection object
    Statement stmt = null;
    Statement stmt2 = null;

    public Appoint() {
        add(AppointPanel);
        setTitle("Appointing Form");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        appointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fname = forename.getText();
                String sname = surname.getText();
                String e_address = e_mail_address.getText();
                String ISSN_value = t_ISSN.getText();
                String te_password = t_password.getText();
                int valueweneed = 0;

                try {
                    String DB="jdbc:mysql://stusql.dcs.shef.ac.uk/team035?user=team035&password=5455d7fb";
                    con = DriverManager.getConnection(DB);

                    try{
                        stmt = con.createStatement();
                        int inUsers = stmt.executeUpdate("INSERT INTO Users " + "VALUES (" + "'" + e_address + "'" + ", " + "'" + fname + "'" + ", " + "'" +
                                sname + "'" + ", " + "'" + "Unkwown" + "'" + ", " + "'" + te_password + "'" + "," + "'editor'" +
                                "," + "'" + valueweneed + "'" + ")");

                        stmt2 = con.createStatement();
                        int inJournalEditors = stmt2.executeUpdate("INSERT INTO JournalEditors " + "VALUES (" + "'" + ISSN_value + "'" + ", " + "'" + e_address + "'" + "," + "'"
                                + valueweneed + "'" + ")");

                        Success.setText("Editor Appointed");


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

        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChiefEditorArea area = new ChiefEditorArea();
                area.setVisible(true);
                dispose();
            }
        });

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChiefEditorArea chiefearea = new ChiefEditorArea();
                chiefearea.setVisible(true);
                dispose();
                // How to keep this kind of session which reminds of the username on the welcomeLabel in ChiefEditorArea named "LoggedAs" ? However not immportant Feature
            }
        });
    };
}