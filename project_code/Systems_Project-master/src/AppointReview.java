import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import com.mysql.cj.Session;
import exceptions.InvalidAuthenticationException;
import exceptions.UserDoesNotExistException;
import main.*;
import models.*;

public class AppointReview extends JFrame{
    private JComboBox own_article_CB;
    private JComboBox selection_CB;
    private JButton appointButton;
    private JPanel AppointReview;
    private JButton backward;

    AppointReview(){
        add(AppointReview);
        setTitle("Appoint Review Page");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Article[] reviews_list = new Article[0];
        Article[] reviews2_list = new Article[0];

        try {
            reviews_list = SessionData.db.articlesNeedingContributions(SessionData.currentUser).toArray(new Article[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        } catch (InvalidAuthenticationException e) {
            e.printStackTrace();
        }

        try {
            reviews2_list = SessionData.db.getUnaffiliatedArticlesToReview(SessionData.currentUser).toArray(new Article[0]);
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        } catch (InvalidAuthenticationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<= reviews_list.length-1 ; i++) {
            own_article_CB.addItem(reviews_list[i].getTitle());
        }

        for(int i = 0; i<= reviews2_list.length-1 ; i++){
            selection_CB.addItem(reviews2_list[i].getTitle());
        }



        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReviewerArea backtoarea = new ReviewerArea();
                backtoarea.setVisible(true);
                dispose();
            }});

        appointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Article[] reviews_list = new Article[0];
                Article[] reviews2_list = new Article[0];

                try {
                    reviews_list = SessionData.db.articlesNeedingContributions(SessionData.currentUser).toArray(new Article[0]);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                }

                try {
                    reviews2_list = SessionData.db.getUnaffiliatedArticlesToReview(SessionData.currentUser).toArray(new Article[0]);
                } catch (UserDoesNotExistException e2) {
                    e2.printStackTrace();
                } catch (InvalidAuthenticationException e2) {
                    e2.printStackTrace();
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }

                Article article_to_review = null;
                Article own_article = null;

                for (Article tar : reviews_list){
                    if (tar.getTitle().equals(own_article_CB.getSelectedItem())){
                        own_article = tar;
                        break;
                    }
                }

                for (Article tar : reviews2_list){
                    if (tar.getTitle().equals(selection_CB.getSelectedItem())){
                        article_to_review = tar;
                        break;
                    }
                }

                Review target = new Review();

                target.setSubmissionArticleID(article_to_review.getArticleID());
                target.setArticleOfReviwerID(own_article.getArticleID());

                try {
                    boolean success = SessionData.db.reserverReview(target,SessionData.currentUser);
                    if(success){
                        JOptionPane.showMessageDialog(null, "Submission Appointed!");
                        ReviewerArea back = new ReviewerArea();
                        back.setVisible(true);
                        dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Sorry, something went!");
                    }
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                }


            }});


    }
}
