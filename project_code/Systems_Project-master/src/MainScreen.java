//import com.mysql.cj.Session;
import database_interface.DataAccessController;
import database_interface.DatabaseConstants;
import exceptions.IncompleteInformationException;
import exceptions.InvalidAuthenticationException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;
import models.User;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        updatePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String correct = JOptionPane.showInputDialog("Enter your current password");
                String new_password = JOptionPane.showInputDialog("Enter your new password");
                String confirmation = JOptionPane.showInputDialog("Confirm your new password");

                User input = new User();

                input.setEmail(SessionData.currentUser.getEmail());
                input.setPassword(correct);

                if (new_password.equals(confirmation)) {
                    try {
                         boolean success = SessionData.db.changePassword(input, new_password);

                         if (success){
                             JOptionPane.showMessageDialog(null,"Password correctly changed!");
                         }
                         else {
                             JOptionPane.showMessageDialog(null,"Password has not been changed");
                         }

                    } catch (UserDoesNotExistException e1) {
                        e1.printStackTrace();
                    } catch (InvalidAuthenticationException e1) {
                        e1.printStackTrace();
                    } catch (IncompleteInformationException e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(null,"Make sure you fill in the form correctly");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
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


}
}