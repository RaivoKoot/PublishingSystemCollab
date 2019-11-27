import exceptions.IncompleteInformationException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;
import models.User;
import main.SessionData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SubscribeForm extends JFrame {
    private JPanel SubscribePanel;

    private JLabel forename;
    private JLabel surname;
    private JLabel e_mail;
    private JLabel university;
    private JLabel chosen_password;

    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField6;
    private JTextField textField7;
    private JPasswordField passwordField1;

    private JButton registerButton;
    private JButton backward;
    private JLabel successlabel;
    private JComboBox comboBox1;

    Connection con = null; // a Connection object
    Statement stmt = null;
    Statement stmt2 = null;

    public SubscribeForm() {

        add(SubscribePanel);
        setTitle("Subscription Form");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        String[] title_list = {"Dr.", "Prof.", "Mr.", "Mrs"};
        for(int i = 0; i<= title_list.length-1 ; i++) {
            comboBox1.addItem(title_list[i]);
        }

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fname = textField1.getText();
                String sname = textField2.getText();
                char[] spassword = passwordField1.getPassword();
                String e_mail = textField6.getText();
                String uni = textField7.getText();
                String title = (String) comboBox1.getSelectedItem();


                User input = new User();
                input.setEmail(e_mail);
                input.setPassword(spassword);
                input.setUniversity(uni);
                input.setForenames(fname);
                input.setSurname(sname);
                input.setTitle(title);

                try {
                    boolean success = SessionData.db.registerBaseUser(input);

                    if (success) {
                        // TODO: Do some UI stuff
                        successlabel.setText("Correctly Registered!");
                        textField1.setText("");
                        textField2.setText("");
                        textField6.setText("");
                        textField7.setText("");
                        passwordField1.setText("");

                    } else {
                        // TODO: Display some 'Fail' box to the user
                        JOptionPane.showMessageDialog(null, "Registering failed");
                    }


                } catch (UserAlreadyExistsException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "That email is already in use");
                } catch (IncompleteInformationException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Please fill out the boxes correctly");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Something went wrong. " +
                            "Please try again or contact and administrator");
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



