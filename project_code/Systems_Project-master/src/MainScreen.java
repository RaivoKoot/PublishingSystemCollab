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

public class MainScreen extends JFrame{
    private JPanel MainScreenPanel;
    private JButton authorSectionButton;
    private JButton reviewerSectionButton;
    private JButton editorAreaButton;
    private JButton createAJournalButton;
    private JButton submitAnArticleButton;
    private JButton updatePasswordButton;


    public MainScreen() {
        add(MainScreenPanel);
        setTitle("Main Screen");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


    }
}