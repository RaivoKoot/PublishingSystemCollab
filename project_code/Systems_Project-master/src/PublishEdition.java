import models.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import main.*;

public class PublishEdition extends JFrame{

    private JButton publishButton;
    private JPanel editionPanel;
    private JButton backward;
    private JComboBox edition_n_cb;
    private JComboBox publication_m_cb;
    private JComboBox volume_n_nb;
    private Edition[] edition_numbers;

    public PublishEdition() {
        add(editionPanel);

        setTitle("Publish Edition Page");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
/*
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
        "November", "December"};


        for(int i = 0; i<= months.length-1 ; i++) {
            publication_m_cb.addItem(months[i]);
        }*/
        /*
        Edition[] edition_numbers = new Edition([0];
        //edition_numbers = SessionData.db.getallEditionArticles().toArray(new EditionArticle[0]);
        for(int i = 0; i<= edition_numbers.length-1 ; i++) {
            edition_n_cb.addItem(edition_numbers[i].getEditionNum());
            publication_m_cb.addItem(edition_numbers[i].getPublicationMonth());
        }

        Volume[] volume_numbers = new Volume[0];
        //volume_numbers = SessionData.db.getAllJournalVolumes().toArray(new Volume[0]);
        for(int i = 0; i<= volume_numbers.length-1 ; i++) {
            volume_n_nb.addItem(volume_numbers[i].getVolNum());
        } */


        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChiefEditorArea chiefarea = new ChiefEditorArea();
                chiefarea.setVisible(true);
                dispose();

            }
        });
    }
}
