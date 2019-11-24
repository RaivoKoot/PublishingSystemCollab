import database_interface.DataAccessController;
import database_interface.DatabaseConstants;
import exceptions.IncompleteInformationException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;
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


                    User input = new User();
                    input.setEmail(email);
                    input.setPassword(password);

                    try {
                        boolean success = Session.db.validCredentials(input);

                        if (success) {
                            Session.currentUser = input;
                            // TODO: Do some UI stuff
                            MainScreen themainscreen = new MainScreen();
                            themainscreen.setVisible(true);
                            dispose();

                        } else {
                            // TODO: Display some 'invalid password or login' box to the user
                        }

                    } catch(UserDoesNotExistException exception) {
                        exception.printStackTrace();

                        // TODO: Display some 'invalid login' box to the user
                    } catch(SQLException exception) {
                        exception.printStackTrace();

                        // TODO: Display some 'invalid login' box to the user
                    }




        }});

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
