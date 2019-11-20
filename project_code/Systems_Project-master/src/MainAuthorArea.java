import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MainAuthorArea extends JFrame{
    private JButton registerCoAuthorsButton;
    private JButton checkArticleStatusButton;
    private JButton respondToCriticismsButton;
    private JButton checkReviewsButton;
    private JButton updatePasswordButton;
    private JPanel MAPanel;
    Connection con = null;
    Statement stmt = null;

    public MainAuthorArea(){
        add(MAPanel);
        setTitle("Main Author Area");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        registerCoAuthorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterCoAuthors registerArea = new RegisterCoAuthors();
                registerArea.setVisible(true);
                dispose();
            }
        });

        updatePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String e_mail_address = JOptionPane.showInputDialog("What is your e-mail address?");
                String new_password = JOptionPane.showInputDialog("Enter your new password");
                String confirmation = JOptionPane.showInputDialog("Confirm your new password");

                try {
                    String DB="jdbc:mysql://stusql.dcs.shef.ac.uk/team035?user=team035&password=5455d7fb";
                    con = DriverManager.getConnection(DB);

                    try{
                        if (new_password.equals(confirmation)){
                            String query = "UPDATE Users SET password =? WHERE email =?";
                            PreparedStatement preparedStmt = con.prepareStatement(query);
                                /*int update = stmt.executeUpdate("UPDATE Users SET password = " + "'" + new_password + "'" + " WHERE " +
                                        "email = " + "'" + e_mail_address + "'"); */

                            preparedStmt.setString(1, new_password);
                            preparedStmt.setString(2, e_mail_address);
                            preparedStmt.executeUpdate();

                            ChiefEditorArea goback = new ChiefEditorArea();
                            goback.setVisible(true);
                            dispose();
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
    }




}
