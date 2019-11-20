import database_interface.DataAccessController;
import database_interface.DatabaseConstants;
import exceptions.IncompleteInformationException;
import exceptions.UserAlreadyExistsException;
import models.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LogIn extends JFrame{
    private JPanel LogInPanel;
    private JTextField usernameInput;
    private JTextField psInput;
    private JButton backward;
    private JButton loginButton;
    private JLabel JLabel4;
    Connection con = null; // a Connection object
    Statement stmt = null;


    public LogIn() {
        add(LogInPanel);
        setTitle("LoginPage");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = usernameInput.getText();
                String password = psInput.getText();

                int correctness = 1;
                int validity = 1;

                try {

                    /*
                    DataAccessController db = new DataAccessController(new DatabaseConstants(false));

                    User input = new User();
                    input.setEmail(email);
                    input.setPassword(password);

                    try {
                        boolean success = db.validCredentials(input);
                    } catch (UserAlreadyExistsException exception){

                    } catch(IncompleteInformationException){

                    } catch(SQLException){
                    */


                    String DB="jdbc:mysql://stusql.dcs.shef.ac.uk/team035?user=team035&password=5455d7fb";
                    con = DriverManager.getConnection(DB);

                    try{
                        //stmt = con.createStatement();
                        PreparedStatement pstm = con.prepareStatement("SELECT * FROM `Users` WHERE `email` =? AND `password` =? AND isSuperuser = '1' AND role = 'editor'");
                        pstm.setString(1, email);
                        pstm.setString(2, password);
                        PreparedStatement pstmt2 = con.prepareStatement("SELECT * FROM `Users` WHERE `email` =? AND `password` =? AND isSuperuser = '1' AND role = 'author'");
                        pstmt2.setString(1, email);
                        pstmt2.setString(2, password);
                        /*PreparedStatement pstmt3 = con.prepareStatement("\"SELECT * FROM `Users` WHERE `email` =? AND `password` =? AND isSuperuser = '0' AND role = 'author'");
                        pstmt3.setString(1, email);
                        pstmt3.setString(2, password);
                        PreparedStatement pstmt4 = con.prepareStatement("\"SELECT * FROM `Users` WHERE `email` =? AND `password` =? AND isSuperuser = '0' AND role = 'editor'");
                        pstmt4.setString(1, email);
                        pstmt4.setString(2, password);

                        ResultSet rse4 = pstmt3.executeQuery();
                        ResultSet rse3 = pstmt3.executeQuery(); */
                        ResultSet rse2 = pstmt2.executeQuery();
                        ResultSet rse = pstm.executeQuery();
                        if(rse.next()){
                                correctness = 0;
                        }
                        if(rse2.next()){
                                validity = 0;
                        }

                        if(correctness == 0){
                            ChiefEditorArea area = new ChiefEditorArea();
                            area.setVisible(true);
                            dispose();
                            area.LoggedAsLabel.setText("Welcome");}
                        else if(validity == 0){
                            MainAuthorArea area2 = new MainAuthorArea();
                            area2.setVisible(true);
                            dispose();
                        }
                        else
                            JLabel4.setText("Failure");

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
