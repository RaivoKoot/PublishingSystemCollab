import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import main.*;
import models.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.*;

public class ChiefEditorArea extends JFrame{
    private JButton appointEditorsButton;
    private JButton publishEditionButton;
    public JLabel LoggedAsLabel;
    private JPanel ChiefEditorPanel;
    private JButton updateOwnRoleButton;
    private JButton backward;
    private JButton retireFromAJournalButton;
    private JButton createNextEditionOfButton;
    private JButton createNextVolumeOfButton;
    private JButton takeDecisionForArticlesButton;
    Connection con = null; // a Connection object
    Statement stmt = null;
    private Journal[] list;


    public ChiefEditorArea(){
        add(ChiefEditorPanel);
        setTitle("Chief Editor Area");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



        appointEditorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*String e_mail = JOptionPane.showInputDialog(null,"Please enter the e-mail of the person you want to register as an editor");
                User target = new User();
                target.setEmail(e_mail);
                try {
                    boolean doesitExist = SessionData.db.userExists(target);
                    if (doesitExist){
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }*/
                Appoint appointform = new Appoint();
                if (appointform.journal_list.length == 0){
                    JOptionPane.showMessageDialog(null,"Sorry you cannot access this page, you are not an editor.");
                }
                else {
                    appointform.setVisible(true);
                    dispose();
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

        publishEditionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PublishEdition pe = new PublishEdition();
                pe.setVisible(true);
                dispose();
            }
        });

        updateOwnRoleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //String answer = JOptionPane.showInputDialog("Are you sure you wanna pass your role as Chief Editor onto another person?");
                String own_e_mail =  JOptionPane.showInputDialog("What is your e_mail address?");
                String target_e_mail = JOptionPane.showInputDialog("What is the e_mail of that person?");

                try {
                    String DB="jdbc:mysql://stusql.dcs.shef.ac.uk/team035?user=team035&password=5455d7fb";
                    con = DriverManager.getConnection(DB);

                    try{

                            String query2 = "UPDATE JournalEditors SET isChief =? WHERE email =?";
                            String query3 = "UPDATE Users SET isSuperuser =? WHERE email=?";
                            PreparedStatement preparedStmt2 = con.prepareStatement(query2);
                            PreparedStatement preparedStmt3 = con.prepareStatement(query3);
                            PreparedStatement preparedStmt4 = con.prepareStatement(query2);
                            PreparedStatement preparedStmt5 = con.prepareStatement(query3);


                                /*int update = stmt.executeUpdate("UPDATE Users SET password = " + "'" + new_password + "'" + " WHERE " +
                                        "email = " + "'" + e_mail_address + "'"); */

                            preparedStmt2.setInt(1, 1);
                            preparedStmt2.setString(2, target_e_mail);
                            preparedStmt3.setInt(1, 1);
                            preparedStmt3.setString(2, target_e_mail);
                            preparedStmt4.setInt(1, 0);
                            preparedStmt4.setString(2, own_e_mail);
                            preparedStmt5.setInt(1, 0);
                            preparedStmt5.setString(2, own_e_mail);
                            preparedStmt2.executeUpdate();
                            preparedStmt3.executeUpdate();
                            preparedStmt4.executeUpdate();
                            preparedStmt5.executeUpdate();



                        ChiefEditorArea gobacke = new ChiefEditorArea();
                            gobacke.setVisible(true);
                            dispose();

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
                }

            }
        });

        createNextVolumeOfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                list = new Journal[0];

                try {
                    list = SessionData.db.getAllJournals().toArray(new Journal[0]);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }


                for(int i = 0; i<= list.length-1 ; i++) {
                    String s = (String)JOptionPane.showInputDialog(
                            null,
                            "Choose your ISSN",
                            "Journal choice",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            list,
                            new String[]{""});
                }


            }
        });



}


}
