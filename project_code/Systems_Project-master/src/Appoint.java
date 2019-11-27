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
    private JComboBox comboBox2;
    public Journal[] journal_list;
    Connection con = null; // a Connection object
    Statement stmt = null;
    Statement stmt2 = null;

    public Appoint() {
        add(AppointPanel);
        setTitle("Appointing Form");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] title_list = {"Dr.", "Prof.", "Mr.", "Mrs"};
        for(int i = 0; i<= title_list.length-1 ; i++) {
            comboBox2.addItem(title_list[i]);
        }

        journal_list = new Journal[0];
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
                String title = (String) comboBox2.getSelectedItem();

                User target = new User();
                target.setEmail(e_address);

                JournalEditor target2 = new JournalEditor();

                target2.setEmail(target.getEmail());
                target2.setChief(false);

                JournalEditor chief = new JournalEditor(SessionData.currentUser);

                chief.setChief(true);

                Journal jour = null;
                for (Journal journal : journal_list){
                    if (journal.getName() == comboBox1.getSelectedItem()){
                        jour = journal;
                        break;
                    }
                }

                if (jour == null){
                    MainScreen main_screen = new MainScreen();
                    main_screen.setVisible(true);
                    dispose();
                } else {
                    chief.setIssn((jour.getISSN()));
                    target2.setIssn(jour.getISSN());
                }

                try {
                    boolean success = SessionData.db.userExists(target);
                    if (success){
                        boolean success2 = SessionData.db.promoteUserToEditor(target2, chief);
                        Success.setText("Success");
                    }
                    else {
                        target.setUniversity(uni);
                        target.setForenames(fname);
                        target.setPassword(te_password);
                        target.setSurname(sname);
                        target.setTitle((String) comboBox2.getSelectedItem());
                        boolean success3 = SessionData.db.registerBaseUser(target);
                        if (success3){
                            boolean success4 = SessionData.db.promoteUserToEditor(target2, chief);
                        }
                    }
                } catch (UserDoesNotExistException e1) {
                    JOptionPane.showMessageDialog(null, "Sorry, something went wrong");
                    e1.printStackTrace();
                } catch (InvalidAuthenticationException e1) {
                    JOptionPane.showMessageDialog(null, "You are not a chief editor of that journal");
                    e1.printStackTrace();
                } catch (IncompleteInformationException e1) {
                    JOptionPane.showMessageDialog(null, "User does not exist, fill in the entire form");
                    e1.printStackTrace();
                } catch (SQLIntegrityConstraintViolationException alreadyEditor){
                    JOptionPane.showMessageDialog(null, "User already an editor of that journal");
                }
                catch (SQLException e1) {
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