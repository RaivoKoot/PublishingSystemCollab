import javax.swing.*;

import exceptions.InvalidAuthenticationException;
import exceptions.ObjectDoesNotExistException;
import exceptions.UserDoesNotExistException;
import main.*;
import models.*;
import main.*;
import database_interface.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class CheckArticleStatus extends JFrame {
    public JComboBox comboBox1;
    private JButton getInfoButton;
    private JTextField InitialReviews;
    private JTextField Contributions;
    private JTextField Reponses;
    private JTextField RevisedStatus;
    private JTextField FinalReviews;
    private JTextField Decisions;
    private JPanel CheckPanel;
    private JButton backward;

    public CheckArticleStatus(){
        add(CheckPanel);
        setTitle("Check Status View");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ArrayList<Article> articles_list = null;
        ArrayList<Article> articles_infos = null;

        try {
            articles_infos = SessionData.db.getOwnArticleWithStatus(SessionData.currentUser);
        } catch (InvalidAuthenticationException e) {
            e.printStackTrace();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i< articles_infos.size() ; i++) {
            comboBox1.addItem(articles_infos.get(i).getTitle());
        }

        Article art = null;

        for (Article er : articles_infos){
            if (er.getTitle().equals(comboBox1.getSelectedItem())){
                art = er;
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
                ArrayList<Article> articles_infos = null;

                try {
                    articles_infos = SessionData.db.getOwnArticleWithStatus(SessionData.currentUser);
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                Article art = null;

                for (Article er : articles_infos){
                    if (er.getTitle().equals(comboBox1.getSelectedItem())){
                        art = er;
                        break;
                    }
                }



                InitialReviews.setText(String.valueOf(art.getReviewsReceived()));
                Contributions.setText(String.valueOf(art.getReviewsContributed()));
                Reponses.setText(String.valueOf(art.getResponesToReviewsGiven()));
                if(art.isFinal()){
                    RevisedStatus.setText("Revised");
                }
                else{
                    RevisedStatus.setText("Not Revised");
                }
                FinalReviews.setText(String.valueOf(art.getFinalReviewsReceived()));
                if(art.isAccepted()){
                    Decisions.setText("Accepted");
                }
                else{
                    Decisions.setText("Not considered yet");
                }
            }
        });

    }


}


