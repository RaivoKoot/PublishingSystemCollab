import exceptions.UserDoesNotExistException;
import models.User;
import main.SessionData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LogIn extends JFrame{
    private JPanel LogInPanel;
    private JTextField usernameInput;
    private JButton backward;
    private JButton loginButton;
    private JLabel JLabel4;
    private JPasswordField passwordField1;


    public LogIn() {
        add(LogInPanel);
        setTitle("LoginPage");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = usernameInput.getText();
                char[] password = passwordField1.getPassword();


                    User input = new User();
                    input.setEmail(email);
                    input.setPassword(password);

                    try {
                        boolean success = SessionData.db.validCredentials(input);

                        if (success) {
                            SessionData.currentUser = input;

                            MainScreen themainscreen = new MainScreen();
                            themainscreen.setVisible(true);
                            dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "The password you have entered is incorrect");
                        }

                    } catch(UserDoesNotExistException exception) {
                        exception.printStackTrace();
                        JOptionPane.showMessageDialog(null, "A user with that email does not exist");

                    } catch(SQLException exception) {
                        exception.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Something went wrong. Please try again or contact an administrator");

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
