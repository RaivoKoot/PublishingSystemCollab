import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import exceptions.InvalidAuthenticationException;
import exceptions.ObjectDoesNotExistException;
import exceptions.UserDoesNotExistException;
import helpers.ChoosePDF;
import main.*;
import models.*;

public class SubmitRevision extends JFrame{
    private JPanel SubmitRevision;
    public JComboBox comboBox1;
    private JTextField textField1;
    private JTextArea textArea1;
    private JButton submitRevisionButton;
    private JButton backward;
    private JButton getInfoButton;
    private ArrayList<Article> articles_for_revision;

    public SubmitRevision(){
        add(SubmitRevision);
        setTitle("Chief Editor Area");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        textArea1.setLineWrap(true);

        try {
            articles_for_revision = SessionData.db.getArticlesNeedingRevision(SessionData.currentUser);

        } catch (Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(null,"Something went wrong");

        }

        for(int i = 0; i< articles_for_revision.size() ; i++) {
            comboBox1.addItem(articles_for_revision.get(i).getTitle());
        }

        Article tar = null;

        for (Article ter : articles_for_revision){
            if (ter.getTitle().equals(comboBox1.getSelectedItem())){
                tar = ter;
                break;
            }
        }


        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainAuthorArea backtoarea = new MainAuthorArea();
                backtoarea.setVisible(true);
                dispose();
            }
        });
        getInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    articles_for_revision = SessionData.db.getArticlesNeedingRevision(SessionData.currentUser);

                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Sorry, something went wrong");

                }

                for(int i = 0; i< articles_for_revision.size() ; i++) {
                    comboBox1.addItem(articles_for_revision.get(i).getTitle());
                }

                Article tar = null;

                for (Article ter : articles_for_revision){
                    if (ter.getTitle().equals(comboBox1.getSelectedItem())){
                        tar = ter;
                        break;
                    }
                }

                textField1.setText(tar.getTitle());
                textArea1.setText(tar.getSummary());

                try {
                    tar.savePdfToPC();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        submitRevisionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    articles_for_revision = SessionData.db.getArticlesNeedingRevision(SessionData.currentUser);

                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Sorry, something went wrong");

                }

                Article tar = null;

                for (Article ter : articles_for_revision){
                    if (ter.getTitle().equals(comboBox1.getSelectedItem())){
                        tar = ter;
                        break;
                    }
                }

                tar.setTitle(textField1.getText());
                tar.setContent("PLACEHOLDER");
                tar.setSummary(textArea1.getText());


                File pdf = ChoosePDF.choosePDF();

                if(pdf == null){
                    return;
                }
                tar.setPdf(pdf);

                try {
                    boolean success = SessionData.db.submitFinalArticleVersion(tar,SessionData.currentUser);
                    if (success){
                        JOptionPane.showMessageDialog(null,"Revised Articles submitted");
                        MainAuthorArea back = new MainAuthorArea();
                        back.setVisible(true);
                        dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Sorry, Something Went Wrong!");

                    }
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null,"Sorry, Something Went Wrong!");
                    e1.printStackTrace();
                }
            }
        });

    }
}
