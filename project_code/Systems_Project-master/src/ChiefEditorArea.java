import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

import exceptions.IncompleteInformationException;
import exceptions.InvalidAuthenticationException;
import exceptions.ObjectDoesNotExistException;
import exceptions.UserDoesNotExistException;
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

        promote.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JournalEditor target = new JournalEditor();
                JournalEditor user = new JournalEditor(SessionData.currentUser);
                String e_mail = JOptionPane.showInputDialog("Enter this person e-mail");
                target.setEmail(e_mail);

                ArrayList<Journal> list = new ArrayList<Journal>();
                ArrayList<String> lister = new ArrayList<String>();

                try {
                    list = SessionData.db.getAllJournals();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                for (int i = 0; i<list.size(); i++){
                    lister.add(list.get(i).getName());
                }
                System.out.println(lister);

                int n =  JOptionPane.showOptionDialog(null,
                        "Choose your Journal",
                        "Journal",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,     //do not use a custom Icon
                        lister.toArray(),  //the titles of buttons
                        lister.get(0)); //default button title

                Journal jour = null;

                for (Journal journal : list){
                    if (journal.getName().equals(lister.get(n))){
                        jour = journal;
                        break;
                    }
                }

                target.setIssn(jour.getISSN());
                user.setIssn((jour.getISSN()));

                try {
                    boolean success = SessionData.db.makeChiefEditor(target,user);
                    if (success){
                        JOptionPane.showMessageDialog(null,"Action successful");
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Action not successful");

                    }
                } catch (UserDoesNotExistException e1) {
                    JOptionPane.showMessageDialog(null,"Sorry the editor does not exist");
                    e1.printStackTrace();
                } catch (IncompleteInformationException e1) {
                    JOptionPane.showMessageDialog(null,"Make sure you correctly fill out the boxes");
                    e1.printStackTrace();
                } catch (InvalidAuthenticationException e1) {
                    JOptionPane.showMessageDialog(null,"Sorry something went wrong");
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null,"Sorry something went wrong");
                    e1.printStackTrace();
                }
            }
        });

        createNextEditionOfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateNextEdition per = new CreateNextEdition();
                per.setVisible(true);
                dispose();
            }
        });




        createNextVolumeOfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Journal> list = new ArrayList<Journal>();
                ArrayList<String> lister = new ArrayList<String>();

                try {
                    list = SessionData.db.getAllJournals();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                for (int i = 0; i<list.size(); i++){
                    lister.add(list.get(i).getName());
                }

                for(int i = 0; i < 100; i++){
                    lister.add(String.valueOf(i));
                }
                System.out.println(lister);

                int n =  JOptionPane.showOptionDialog(null,
                        "Choose your Journal",
                        "Journal",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,     //do not use a custom Icon
                        lister.toArray(),  //the titles of buttons
                        lister.get(0)); //default button title

                String year = JOptionPane.showInputDialog("What is the publication Year?");

                Journal jour = null;

                for (Journal journal : list){
                    if (journal.getName().equals(lister.get(n))){
                        jour = journal;
                        break;
                    }
                }
                JournalEditor chief = new JournalEditor(SessionData.currentUser);
                chief.setIssn(jour.getISSN());

                try{
                    boolean success = SessionData.db.createNextVolume(jour, chief, Integer.valueOf(year));
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (ObjectDoesNotExistException e1) {
                    e1.printStackTrace();
                }

            }
        });



}


}
