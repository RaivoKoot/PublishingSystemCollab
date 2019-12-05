import com.mysql.cj.Session;
import exceptions.InvalidAuthenticationException;
import exceptions.NotEnoughArticlesInEditionException;
import exceptions.ObjectDoesNotExistException;
import exceptions.UserDoesNotExistException;
import models.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

import main.*;

public class PublishEdition extends JFrame {

    private JPanel editionPanel;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JButton publishEditionButton;
    private JButton generateVolumesButton;
    private JButton generateEditionsButton;
    private JButton backward;

    public PublishEdition() {
        add(editionPanel);
        setTitle("Publish Edition Area");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ArrayList<Journal> journals_list =null;

        try {
            journals_list = SessionData.db.getJournalsByUser(SessionData.currentUser);
        } catch (InvalidAuthenticationException e) {
            e.printStackTrace();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i< journals_list.size() ; i++) {
            comboBox1.addItem(journals_list.get(i).getName());
        }

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChiefEditorArea main_screen = new ChiefEditorArea();
                main_screen.setVisible(true);
                dispose();
            }
        });

        generateVolumesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Journal> journals_list =null;
                ArrayList<Volume> volume_list = new ArrayList<>();
                comboBox2.removeAllItems();
                comboBox3.removeAllItems();

                try {
                    journals_list = SessionData.db.getJournalsByUser(SessionData.currentUser);
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                Journal own_journal = null;

                for (Journal tar : journals_list){
                    if (tar.getName().equals(comboBox1.getSelectedItem())){
                        own_journal = tar;
                        break;
                    }
                }

                try {
                    volume_list = SessionData.db.getAllJournalVolumes(own_journal);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                for(int i = 0; i< volume_list.size() ; i++) {
                    comboBox2.addItem(volume_list.get(i).getName());
                }

            }
        });

        generateEditionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Journal> journals_list =null;
                ArrayList<Volume> volume_list = new ArrayList<>();
                ArrayList<Edition> edition_list = new ArrayList<>();
                comboBox3.removeAllItems();


                try {
                    journals_list = SessionData.db.getJournalsByUser(SessionData.currentUser);
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                Journal own_journal = null;

                for (Journal tar : journals_list){
                    if (tar.getName().equals(comboBox1.getSelectedItem())){
                        own_journal = tar;
                        break;
                    }
                }

                try {
                    volume_list = SessionData.db.getAllJournalVolumes(own_journal);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                Volume own_volume = null;

                for (Volume vol : volume_list){
                    if (vol.getName().equals(comboBox2.getSelectedItem())){
                        own_volume = vol;
                        break;
                    }
                }

                try {
                    edition_list = SessionData.db.getAllVolumeEditions(own_volume);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                for(int i = 0; i< edition_list.size() ; i++) {
                    comboBox3.addItem(edition_list.get(i).getName());
                }
            }
        });

        publishEditionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Journal> journals_list =null;
                ArrayList<Volume> volume_list = new ArrayList<>();
                ArrayList<Edition> edition_list = new ArrayList<>();


                try {
                    journals_list = SessionData.db.getJournalsByUser(SessionData.currentUser);
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                Journal own_journal = null;

                for (Journal tar : journals_list){
                    if (tar.getName().equals(comboBox1.getSelectedItem())){
                        own_journal = tar;
                        break;
                    }
                }

                try {
                    volume_list = SessionData.db.getAllJournalVolumes(own_journal);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                Volume own_volume = null;

                for (Volume vol : volume_list){
                    if (vol.getName().equals(comboBox2.getSelectedItem())){
                        own_volume = vol;
                        break;
                    }
                }

                try {
                    edition_list = SessionData.db.getAllVolumeEditions(own_volume);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                Edition own_edition = null;

                for(Edition edi : edition_list){
                    if (edi.getName().equals(comboBox3.getSelectedItem())){
                        own_edition = edi;
                        break;
                    }
                }

                try {
                    boolean success = SessionData.db.publishEdition(own_edition , SessionData.currentUser);
                    if(success){
                        JOptionPane.showMessageDialog(null, "Edition successfully published!");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "A Sorry, something went wrong!");
                    }
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "B Sorry, something went wrong!");
                } catch (ObjectDoesNotExistException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "C Sorry, something went wrong!");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "D Sorry, something went wrong!");
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "E Sorry, something went wrong!");
                } catch (NotEnoughArticlesInEditionException e1){
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Not enough Articles in Edition");
                }
            }
        });

    }

}

