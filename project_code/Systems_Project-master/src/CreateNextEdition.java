import exceptions.InvalidAuthenticationException;
import exceptions.ObjectDoesNotExistException;
import exceptions.VolumeFullException;
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
    private JComboBox JournalCB;
    private JComboBox VolumeCB;
    private JButton backward;
    private JButton generateVolumeButton;
    private JButton createEditionButton;
    private Journal[] journal_list;
    private Volume[] volume_list;

    public CreateNextEdition() {
        // TODO: place custom component creation code here
        add(createNextEdition);
        setTitle("Create Next Edition Page");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        journal_list = new Journal[0];

        try {
            journal_list = SessionData.db.getAllJournals().toArray(new Journal[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(int i = 0; i<= journal_list.length-1 ; i++) {
            JournalCB.addItem(journal_list[i].getName());
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

                String publicationMonth = JOptionPane.showInputDialog("What is the Publication Month?");

                JournalEditor user = new JournalEditor(SessionData.currentUser);
                user.setIssn(paper.getISSN());

                try {
                    SessionData.db.createNextEdition(my_volume,user,publicationMonth);
                } catch (ObjectDoesNotExistException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Make sure you correctly fill out the form");
                } catch (InvalidAuthenticationException e1) {
                    JOptionPane.showMessageDialog(null, "Sorry something went wrong!");
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Sorry this Volume is already full!");
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
