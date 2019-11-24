import javax.swing.*;
import exceptions.UserDoesNotExistException;
import models.User;
import main.SessionData;
import models.Journal;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class JournalForm extends JFrame{
    private JPanel JournalPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JButton backward;

    public JournalForm(){
        add(JournalPanel);
        setTitle("Journal Form Screen");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String name = textField1.getText();
        String ISSN = textField2.getText();

        Journal target = new Journal();
        target.setName(name);
        target.setISSN(ISSN);

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainScreen main_screen = new MainScreen();
                main_screen.setVisible(true);
                dispose();
            }
        });

    }
}
