//import com.mysql.cj.Session;
import database_interface.DataAccessController;
import database_interface.DatabaseConstants;
import exceptions.*;
import models.User;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import main.SessionData;

public class MainScreen extends JFrame{
    private JPanel MainScreenPanel;
    private JButton authorSectionButton;
    private JButton reviewerSectionButton;
    private JButton editorAreaButton;
    private JButton createAJournalButton;
    private JButton submitAnArticleButton;
    private JButton updatePasswordButton;
    private JButton backward;


    public MainScreen() {
        add(MainScreenPanel);
        setTitle("Main Screen");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        updatePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String correct = JOptionPane.showInputDialog("Enter your current password");
                String new_password = JOptionPane.showInputDialog("Enter your new password");
                String confirmation = JOptionPane.showInputDialog("Confirm your new password");

                User input = new User();

                input.setEmail(SessionData.currentUser.getEmail());
                try {
                    input.setPassword(correct);
                } catch (PasswordToLongException e1) {
                    JOptionPane.showMessageDialog(null,"The password you entered is too long!");
                    e1.printStackTrace();
                    return;
                } catch (PasswordTooShortException e1) {
                    JOptionPane.showMessageDialog(null,"The password you entered is too short!");
                    e1.printStackTrace();
                    return;
                } catch (NoSuchAlgorithmException e1) {
                    JOptionPane.showMessageDialog(null,"Something went wrong. Please contact an administrator.");
                    e1.printStackTrace();
                    return;
                } catch (NoDigitInPasswordException e1) {
                    JOptionPane.showMessageDialog(null,"The password you entered needs to contain at least one digit!");
                    e1.printStackTrace();
                    return;
                }

                if (new_password.equals(confirmation)) {
                    try {
                         boolean success;

                        try {
                            success = SessionData.db.changePassword(input, new_password);
                        } catch (PasswordToLongException e1) {
                            JOptionPane.showMessageDialog(null,"The password you entered is too long!");
                            e1.printStackTrace();
                            return;
                        } catch (PasswordTooShortException e1) {
                            JOptionPane.showMessageDialog(null,"The password you entered is too short!");
                            e1.printStackTrace();
                            return;
                        } catch (NoSuchAlgorithmException e1) {
                            JOptionPane.showMessageDialog(null,"Something went wrong. Please contact an administrator.");
                            e1.printStackTrace();
                            return;
                        } catch (NoDigitInPasswordException e1) {
                            JOptionPane.showMessageDialog(null,"The password you entered needs to contain at least one digit!");
                            e1.printStackTrace();
                            return;
                        }

                         if (success){
                             JOptionPane.showMessageDialog(null,"Password correctly changed!");
                         }
                         else {
                             JOptionPane.showMessageDialog(null,"Password has not been changed");
                         }

                    } catch (UserDoesNotExistException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(null,"A user with your email does not exist anymore");
                    } catch (InvalidAuthenticationException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(null,"The password you have entered is incorrect");
                    } catch (IncompleteInformationException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(null,"Make sure you fill in the form correctly");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(null,"Something went wrong. Please try again or contact an administrator");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null,"The new passwords you entered do not match");
                }


            }});

        submitAnArticleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                MainScreenPanel.setVisible(false);
                dispose();
                SubmitArticle submitform = new SubmitArticle();
                submitform.setVisible(true);
            }});

        authorSectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                MainScreenPanel.setVisible(false);
                dispose();
                MainAuthorArea authorArea = new MainAuthorArea();
                authorArea.setVisible(true);
            }});

        editorAreaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                MainScreenPanel.setVisible(false);
                dispose();
                ChiefEditorArea editorArea = new ChiefEditorArea();
                editorArea.setVisible(true);
            }});

        createAJournalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                MainScreenPanel.setVisible(false);
                dispose();
                JournalForm thejournalform = new JournalForm();
                thejournalform.setVisible(true);
            }});

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WelcomeGUI welcome_screen = new WelcomeGUI();
                welcome_screen.setVisible(true);
                dispose();
            }
        });

        reviewerSectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReviewerArea rarea = new ReviewerArea();
                rarea.setVisible(true);
                dispose();
            }
        });




}
}