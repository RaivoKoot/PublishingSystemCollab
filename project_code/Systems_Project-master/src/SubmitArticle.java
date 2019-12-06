import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;

import exceptions.IncompleteInformationException;
import exceptions.InvalidAuthenticationException;
import exceptions.UserDoesNotExistException;
import helpers.ChoosePDF;
import models.Article;
import database_interface.DataAccessController;
import main.SessionData;
import models.Journal;
import database_interface.DataAccessController;

public class SubmitArticle extends JFrame {
    private JPanel SubmitArticle;
    private JTextField abstracte;
    private JButton backward;
    private JButton submitButton;
    private JTextField Titre;
    private JComboBox comboBox1;


    public SubmitArticle() {
        add(SubmitArticle);
        setTitle("Submit Article Form");
        setSize(800, 500);
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

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainScreen main_screen = new MainScreen();
                main_screen.setVisible(true);
                dispose();
            }
        });

        Journal[] finalJournal_list = journal_list;
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               String title = Titre.getText();
               String abs = abstracte.getText();
               String pdfLink = "placeholder";
               Article sub = new Article();
               sub.setTitle(title);
               sub.setSummary(abs);
               sub.setContent(pdfLink);
               Journal jour = null;

                for (Journal journal : finalJournal_list){
                    if (journal.getName() == comboBox1.getSelectedItem()){
                        jour = journal;
                        break;
                    }
                }

                if (jour == null){
                        MainScreen main_screen = new MainScreen();
                        main_screen.setVisible(true);
                        dispose();
                } else
                    sub.setIssn(jour.getISSN());


                File pdf = ChoosePDF.choosePDF();

                if(pdf == null){
                    return;
                }
                sub.setPdf(pdf);

               try{
                    Article success = SessionData.db.submitArticle(sub, SessionData.currentUser/*, jour */);
                    if (success != null){
                        JOptionPane.showMessageDialog(null,"Submission successful");
                        Titre.setText("");
                        abstracte.setText("");
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Submission not successfull");
                    }
               } catch (UserDoesNotExistException e1) {
                   JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
                   e1.printStackTrace();
               } catch (InvalidAuthenticationException e1) {
                   JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
                   e1.printStackTrace();
               } catch (IncompleteInformationException e1) {
                   JOptionPane.showMessageDialog(null,"Make sure you fill in the form correctly");
                   e1.printStackTrace();
               } catch (SQLException e1) {
                   JOptionPane.showMessageDialog(null,"Sorry, something went wrong. Please try again or contact an admin");
                   e1.printStackTrace();
               } catch (FileNotFoundException e1) {
                   JOptionPane.showMessageDialog(null,"Sorry, something went wrong submitting the pdf file.");
                   e1.printStackTrace();
               }
            }
        });




    }

    ;
}




