import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import database_interface.*;
import com.mysql.cj.Session;
import exceptions.IncompleteInformationException;
import exceptions.InvalidAuthenticationException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;
import models.*;
import main.*;

public class Appoint extends JFrame {
    private JPanel AppointPanel;
    private JTextField forename;
    private JTextField surname;
    private JTextField e_mail_address;
    private JTextField t_password;
    private JButton appointButton;
    private JButton doneButton;
    private JButton backward;
    private JLabel Success;
    private JTextField university;
    private JComboBox comboBox1;
    private JPanel first_view;
    private JPanel second_view;
    Connection con = null; // a Connection object
    Statement stmt = null;
    Statement stmt2 = null;

    public Appoint() {
        add(AppointPanel);
        setTitle("Appointing Form");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Journal[] journal_list = new Journal[0];
        try {
            journal_list = SessionData.db.getAllJournals().toArray(new Journal[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(int i = 0; i<= journal_list.length-1 ; i++) {
            comboBox1.addItem(journal_list[i].getName());
        }

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String selection = (String)cb.getSelectedItem();
            }
        });

        appointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String e_address = e_mail_address.getText();
                String fname = forename.getText();
                String sname = surname.getText();
                String te_password = t_password.getText();
                String uni = university.getText();

                User target = new User();
                target.setEmail(e_address);

                JournalEditor target2 = new JournalEditor();
                target2.setIssn((String) comboBox1.getSelectedItem());
                target2.setEmail(target.getEmail());
                target2.setChief(false);

                try {
                    boolean success = SessionData.db.userExists(target);
                    if (success){
                        boolean success2 = SessionData.db.promoteUserToEditor(target2, (JournalEditor) SessionData.currentUser);
                        Success.setText("Success");
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Fill in the form, user does not exist");
                        target.setUniversity(uni);
                        target.setForenames(fname);
                        target.setPassword(te_password);
                        target.setSurname(sname);
                        boolean success3 = SessionData.db.registerBaseUser(target);
                        if (success3){
                            boolean success4 = SessionData.db.promoteUserToEditor(target2, (JournalEditor) SessionData.currentUser);
                        }
                    }
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (IncompleteInformationException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (UserAlreadyExistsException e1) {
                    e1.printStackTrace();
                }

                /*try {
                    String DB="jdbc:mysql://stusql.dcs.shef.ac.uk/team035?user=team035&password=5455d7fb";
                    con = DriverManager.getConnection(DB);

                    try{
                        stmt = con.createStatement();
                        int inUsers = stmt.executeUpdate("INSERT INTO Users " + "VALUES (" + "'" + e_address + "'" + ", " + "'" + fname + "'" + ", " + "'" +
                                sname + "'" + ", " + "'" + "Unkwown" + "'" + ", " + "'" + te_password + "'" + "," + "'editor'" +
                                "," + "'" + valueweneed + "'" + ")");

                        stmt2 = con.createStatement();
                        int inJournalEditors = stmt2.executeUpdate("INSERT INTO JournalEditors " + "VALUES (" + "'" + ISSN_value + "'" + ", " + "'" + e_address + "'" + "," + "'"
                                + valueweneed + "'" + ")");

                        Success.setText("Editor Appointed");


                    } catch (SQLException ex) {
                        ex.printStackTrace();

                    } finally {
                        if (stmt != null)
                            stmt.close();
                    }


                } catch (SQLException ex) {
                    ex.printStackTrace();

                } finally {
                    if (con != null) {
                        try {
                            con.close();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                }*/


            }
        });

        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChiefEditorArea area = new ChiefEditorArea();
                area.setVisible(true);
                dispose();
            }
        });

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChiefEditorArea chiefearea = new ChiefEditorArea();
                chiefearea.setVisible(true);
                dispose();
                // How to keep this kind of session which reminds of the username on the welcomeLabel in ChiefEditorArea named "LoggedAs" ? However not immportant Feature
            }
        });
    };
}