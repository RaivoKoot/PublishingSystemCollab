import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import com.mysql.cj.Session;
import exceptions.*;
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
    private JButton promote;
    private JButton backward;
    private JButton retireFromAJournalButton;
    private JButton createNextEditionOfButton;
    private JButton createNextVolumeOfButton;
    private JButton takeDecisionForArticlesButton;
    private JButton assignEditionButton;
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

        promote.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JournalEditor target = new JournalEditor();
                JournalEditor user = new JournalEditor(SessionData.currentUser);


                ArrayList<Journal> list = new ArrayList<Journal>();
                ArrayList<String> lister = new ArrayList<String>();

                try {
                    list = SessionData.db.getJournalsByUser(SessionData.currentUser);
                } catch (Exception ee){
                    JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
                }

                for (int i = 0; i<list.size(); i++){
                    lister.add(list.get(i).getName());
                }
                System.out.println(lister);

                if(!list.isEmpty()) {
                    String e_mail = JOptionPane.showInputDialog("Enter this person e-mail");
                    target.setEmail(e_mail);

                    int n = JOptionPane.showOptionDialog(null,
                            "Choose your Journal",
                            "Journal",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,     //do not use a custom Icon
                            lister.toArray(),  //the titles of buttons
                            null); //default button title

                    Journal jour = null;

                    for (Journal journal : list) {
                        if (journal.getName().equals(lister.get(n))) {
                            jour = journal;
                            break;
                        }
                    }

                    target.setIssn(jour.getISSN());
                    user.setIssn((jour.getISSN()));

                    try {
                        boolean success = SessionData.db.makeChiefEditor(target, user);
                        if (success) {
                            JOptionPane.showMessageDialog(null, "Action successful");
                        } else {
                            JOptionPane.showMessageDialog(null, "Action not successful");

                        }
                    } catch (UserDoesNotExistException e1) {
                        JOptionPane.showMessageDialog(null, "Sorry the editor does not exist");
                        e1.printStackTrace();
                    } catch (IncompleteInformationException e1) {
                        JOptionPane.showMessageDialog(null, "Make sure you correctly fill out the boxes");
                        e1.printStackTrace();
                    } catch (InvalidAuthenticationException e1) {
                        JOptionPane.showMessageDialog(null, "Sorry something went wrong");
                        e1.printStackTrace();
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "Sorry something went wrong");
                        e1.printStackTrace();
                    }
                } else{JOptionPane.showMessageDialog(null,"Sorry you are not an editor!");}
            }
        });

        createNextEditionOfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateNextEdition per = new CreateNextEdition();
                if(per.JournalCB.getItemCount() == 0){
                    JOptionPane.showMessageDialog(null,"Sorry, you are not an editor!");
                }else{
                per.setVisible(true);
                dispose();}
            }
        });



        assignEditionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AssignArticle assart = new AssignArticle();
                if (assart.journals.getItemCount() == 0){
                    JOptionPane.showMessageDialog(null,"Sorry you cannot access this page, you are not an editor.");
                }
                else{
                assart.setVisible(true);
                dispose();}
            }
        });

        retireFromAJournalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JournalEditor user = new JournalEditor(SessionData.currentUser);

                ArrayList<Journal> list = new ArrayList<Journal>();
                ArrayList<String> lister = new ArrayList<String>();


                try {
                    list = SessionData.db.getJournalsByUser(SessionData.currentUser);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                }

                for (int i = 0; i<list.size(); i++){
                    lister.add(list.get(i).getName());
                }

                if(!list.isEmpty()) {

                    int n = JOptionPane.showOptionDialog(null,
                            "Choose your Journal",
                            "Journal",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,     //do not use a custom Icon
                            lister.toArray(),  //the titles of buttons
                            null); //default button title

                    Journal jour = null;

                    for (Journal journal : list) {
                        if (journal.getName().equals(lister.get(n))) {
                            jour = journal;
                            break;
                        }
                    }
                    user.setIssn(jour.getISSN());

                    if (n == 0) {
                        try {

                            boolean success = SessionData.db.deleteEditor(user);
                            if (success) {
                                JOptionPane.showMessageDialog(null, "You have been deleted from the board of that journal.");
                                MainScreen back = new MainScreen();
                                back.setVisible(true);
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(null, "Sorry something went wrong");
                            }
                        } catch (InvalidAuthenticationException e1) {
                            JOptionPane.showMessageDialog(null, "Sorry something went wrong");
                            e1.printStackTrace();
                        } catch (UserDoesNotExistException e1) {
                            JOptionPane.showMessageDialog(null, "Sorry something went wrong");
                            e1.printStackTrace();
                        } catch (SQLException e1) {
                            JOptionPane.showMessageDialog(null, "Sorry something went wrong");
                            e1.printStackTrace();
                        } catch (ObjectDoesNotExistException e1) {
                            JOptionPane.showMessageDialog(null, "Sorry something went wrong");
                            e1.printStackTrace();
                        } catch (CantRemoveLastChiefEditorException e1) {
                            JOptionPane.showMessageDialog(null, "Sorry it appears you are the last editor of that journal");
                            e1.printStackTrace();
                        }
                    } else if (n == 1) {
                        JOptionPane.showMessageDialog(null, "Process cancelled");
                    }
                } else{JOptionPane.showMessageDialog(null,"Sorry you are not an editor...");}

            }
        });

        createNextVolumeOfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Journal> list = new ArrayList<Journal>();
                ArrayList<String> lister = new ArrayList<String>();

                try {
                    list = SessionData.db.getJournalsByUser(SessionData.currentUser);
                } catch (Exception ee){
                    JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
                }

                for (int i = 0; i<list.size(); i++){
                    lister.add(list.get(i).getName());
                }

                if(!list.isEmpty()) {

                    int n = JOptionPane.showOptionDialog(null,
                            "Choose your Journal",
                            "Journal",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,     //do not use a custom Icon
                            lister.toArray(),  //the titles of buttons
                            null); //default button title

                    String year = "";
                    while (year.isEmpty()) {
                        try {
                            year = JOptionPane.showInputDialog("What is the publication Year?");
                            int intValue = Integer.parseInt(year);
                        } catch (NumberFormatException e1) {
                            year = JOptionPane.showInputDialog(null, "Input is not a valid integer");
                        }

                    }
                    Journal jour = null;

                    for (Journal journal : list) {
                        if (journal.getName().equals(lister.get(n))) {
                            jour = journal;
                            break;
                        }
                    }
                    JournalEditor chief = new JournalEditor(SessionData.currentUser);
                    chief.setIssn(jour.getISSN());


                    try {
                        boolean success = SessionData.db.createNextVolume(jour, chief, Integer.valueOf(year));
                        if (success) {
                            JOptionPane.showMessageDialog(null, "Volume succesfully created!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Sorry, Volume could not be created!");
                        }
                    } catch (InvalidAuthenticationException e1) {
                        JOptionPane.showMessageDialog(null, "Sorry, something went wrong!");
                        e1.printStackTrace();
                    } catch (ObjectDoesNotExistException e1) {
                        JOptionPane.showMessageDialog(null, "Sorry, something went wrong!");
                        e1.printStackTrace();
                    } catch (SQLIntegrityConstraintViolationException e1) {
                        JOptionPane.showMessageDialog(null, "Sorry a volume with that year already exists in your journal");
                        e1.printStackTrace();
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "Sorry, something went wrong!");
                        e1.printStackTrace();
                    } catch (LastVolumeNotEnoughEditionsExceptions lastVolumeNotEnoughEditionsExceptions) {
                        lastVolumeNotEnoughEditionsExceptions.printStackTrace();
                        JOptionPane.showMessageDialog(null, "You are not allowed to create a new volume as long as the current volume does not have at least 4 editions.");
                    }

                }else{JOptionPane.showMessageDialog(null,"Sorry you are not an editor");}
            }
        });

        takeDecisionForArticlesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FinalDecision finalde = new FinalDecision();
                if (finalde.comboBox1.getItemCount() == 0){
                    JOptionPane.showMessageDialog(null,"Sorry you are not an editor");
                }else{
                finalde.setVisible(true);
                dispose();}
            }
        });



}


}
