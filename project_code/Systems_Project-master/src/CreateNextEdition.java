import exceptions.*;
import models.Journal;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import main.*;
import database_interface.*;
import models.*;

public class CreateNextEdition extends JFrame{
    private JPanel createNextEdition;
    public JComboBox JournalCB;
    private JComboBox VolumeCB;
    private JButton backward;
    private JButton generateVolumeButton;
    private JButton createEditionButton;
    private JComboBox monthss;
    private Journal[] journal_list;
    private Volume[] volume_list;

    public CreateNextEdition() {
        // TODO: place custom component creation code here
        add(createNextEdition);
        setTitle("Create Next Edition Page");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        journal_list = new Journal[0];

        try {
            journal_list = SessionData.db.getJournalsByUser(SessionData.currentUser).toArray(new Journal[0]);
        } catch (Exception ee){
            JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
        }

        for(int i = 0; i<= journal_list.length-1 ; i++) {
            JournalCB.addItem(journal_list[i].getName());
        }

        String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};

        for(int i = 0; i< months.length; i++){
            monthss.addItem(months[i]);
        }

        generateVolumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //volume_list = new Volume[0];
                VolumeCB.removeAllItems();
                volume_list = null;

                Journal joure = null;
                for (Journal journal : journal_list){
                    if (journal.getName() == JournalCB.getSelectedItem()){
                        joure = journal;
                        break;
                    }
                }

                try {
                    volume_list = SessionData.db.getAllJournalVolumes(joure).toArray(new Volume[0]);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                for(int i = 0; i<= volume_list.length-1 ; i++) {
                    VolumeCB.addItem(volume_list[i].getName());
                }
            }
        });


        createEditionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Volume my_volume = null;
                for (Volume volume : volume_list){
                    if (volume.getName().equals(VolumeCB.getSelectedItem())){
                        my_volume = volume;
                        break;
                    }
                }

                Journal paper = null;
                for (Journal journale : journal_list){
                    if (journale.getName() == JournalCB.getSelectedItem()){
                        paper = journale;
                        break;
                    }
                }

                String publicationMonth = (String) monthss.getSelectedItem();

                JournalEditor user = new JournalEditor(SessionData.currentUser);
                user.setIssn(paper.getISSN());

                try {
                     SessionData.db.createNextEdition(my_volume,user,publicationMonth);
                     JOptionPane.showMessageDialog(null,"Edition added");
                } catch (ObjectDoesNotExistException e1) {
                    JOptionPane.showMessageDialog(null, "Something went wrong!");
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Make sure you correctly fill out the form");
                } catch (InvalidAuthenticationException e1) {
                    JOptionPane.showMessageDialog(null, "Something went wrong!");
                    e1.printStackTrace();
                } catch (VolumeFullException e1) {
                    JOptionPane.showMessageDialog(null, "Sorry this volume is already full");
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Something went wrong!");
                    e1.printStackTrace();
                } catch (NoMoreEditionsAllowedInVolumeException e1) {
                    JOptionPane.showMessageDialog(null, "This Volume is not allowed any more Editions because a newer Volume of your journal exists.");
                    e1.printStackTrace();
                } catch (LastEditionNotFinishedException e1) {
                    JOptionPane.showMessageDialog(null, "You are not allowed to create a new edition in this volume " +
                            "because the last edition is not public yet or has room for more articles. Please publish the last edition or make sure it is full before creating a new one.");
                    e1.printStackTrace();
                }
            }
        });


        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChiefEditorArea main_screen = new ChiefEditorArea();
                main_screen.setVisible(true);
                dispose();
            }
        });
    }
}
