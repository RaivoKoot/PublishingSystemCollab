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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.lang.Object;

public class JournalForm extends JFrame{
    private JPanel JournalPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JButton backward;
    private JButton createButton;
    private JComboBox comboBox1;
    private JLabel validornot;
    //private boolean value = false;

    public JournalForm(){
        add(JournalPanel);
        setTitle("Journal Form Screen");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        /*textField2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                    int i = Integer.parseInt(textField2.getText());
                    if (String.valueOf(i).length() != 8){
                        value = true;
                    }
                    else {
                        validornot.setText("Wrong input for ISSN");
                    }
            }
        });*/

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
                    JOptionPane.showMessageDialog(null, "Your user details are incorrect");
                } catch (UniqueColumnValueAlreadyExists uniqueColumnValueAlreadyExists) {
                    JOptionPane.showMessageDialog(null, "A Journal with that name or ISSN already exists!");
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Your user details do not exist");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Something went wrong!");
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
