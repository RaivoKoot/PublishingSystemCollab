import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegisterCoAuthors extends JFrame{
    private JTextField forename;
    private JTextField surname;
    private JTextField e_mail_address;
    private JTextField t_password;
    private JButton registerButton;
    private JButton doneButton;
    private JLabel Success;
    private JButton backward;
    private JPanel RegisterCo;
    private JPanel RegisterCoAuthors;
    Connection con = null;
    Statement stmt = null;
    Statement stmt2 = null;
    int n;

    public RegisterCoAuthors(){
        add(RegisterCo);
        setTitle("Register Author Page");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fname = forename.getText();
                String sname = surname.getText();
                String e_address = e_mail_address.getText();
                String te_password = t_password.getText();
                int valueweneed = 0;
                n = 4;

                try {
                    String DB="jdbc:mysql://stusql.dcs.shef.ac.uk/team035?user=team035&password=5455d7fb";
                    con = DriverManager.getConnection(DB);

                    try{
                        stmt = con.createStatement();
                        int inUsers = stmt.executeUpdate("INSERT INTO Users " + "VALUES (" + "'" + e_address + "'" + ", " + "'" + fname + "'" + ", " + "'" +
                                sname + "'" + ", " + "'" + "Unkwown" + "'" + ", " + "'" + te_password + "'" + "," + "'author'" +
                                "," + "'" + valueweneed + "'" + ")");

                        stmt2 = con.createStatement();
                        int inAuthorships = stmt2.executeUpdate("INSERT INTO Authorships " + "VALUES (" + "'" + e_address + "'" + ", '" + n + "'" + "," +
                                "'" + valueweneed + "'" + ")");

                        Success.setText("Author Appointed");


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
}
