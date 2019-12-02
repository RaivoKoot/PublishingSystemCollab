import com.mysql.cj.Session;
import exceptions.InvalidAuthenticationException;
import exceptions.ObjectDoesNotExistException;
import exceptions.UserDoesNotExistException;
import models.*;
import main.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class Reviewer_responses extends JFrame{

    private JPanel Responses;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JButton getInfoButton;
    private JTextField textField2;
    private JTextArea content;
    private JButton viewCritiquesAndGiveButton;

    public Reviewer_responses(){
        add(Responses);
        setTitle("Reviewer Responses Area");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);




    }
}
