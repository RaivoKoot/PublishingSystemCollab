import javax.swing.*;

import exceptions.InvalidAuthenticationException;
import exceptions.UniqueColumnValueAlreadyExists;
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
    private JButton createButton;

    public JournalForm(){
        add(JournalPanel);
        setTitle("Journal Form Screen");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = textField1.getText();
                String ISSN = textField2.getText();

                Journal target = new Journal();
                target.setName(name);
                target.setISSN(ISSN);

                try {
                    boolean success = SessionData.db.createJournal(target, SessionData.currentUser);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Journal created!");
                        textField1.setText("");
                        textField2.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Journal not created!");
                    }
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (UniqueColumnValueAlreadyExists uniqueColumnValueAlreadyExists) {
                    uniqueColumnValueAlreadyExists.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            });

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
