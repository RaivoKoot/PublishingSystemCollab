import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

import exceptions.ObjectDoesNotExistException;
import main.*;
import exceptions.InvalidAuthenticationException;
import exceptions.UserDoesNotExistException;
import models.*;
import database_interface.*;

public class RespondToCritiques extends JFrame {
    private JPanel RespondToCritiques;
    private JComboBox articles;
    private JComboBox reviews;
    private JButton respondButton;
    private JButton backward;
    private JButton generateReviewsButton;
    private int validity =0;
    public RespondToCritiques(){
        add(RespondToCritiques);
        setTitle("Respond to Critiques Area");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ArrayList<Article> articles_list = null;

        try {
            articles_list = SessionData.db.getOwnArticles(SessionData.currentUser);
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        } catch (InvalidAuthenticationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i< articles_list.size() ; i++) {
            articles.addItem(articles_list.get(i).getTitle());
        }

        respondButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Review> reviews_list = new ArrayList<Review>();
                ArrayList<Article> articles_list = null;
                ArrayList<Critique> critiques_list = null;



                try {
                    articles_list = SessionData.db.getOwnArticles(SessionData.currentUser);
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                Article art = null;

                for (Article er : articles_list){
                    if (er.getTitle().equals(articles.getSelectedItem())){
                        art = er;
                        break;
                    }
                }

                try {
                    reviews_list = SessionData.db.getInitialReviewsOfArticle(art, SessionData.currentUser);
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                Review our_review = null;

                for (Review rev : reviews_list){
                    if(rev.getReviewerPseudonym().equals(reviews.getSelectedItem())){
                        our_review = rev;
                        break;
                    }
                }

                try {
                    critiques_list = SessionData.db.getReviewCritiques(our_review,SessionData.currentUser);
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (ObjectDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                }

                    Review response = new Review();
                    for (int i = 0; i < critiques_list.size(); i++) {
                        JScrollPane pane = new JScrollPane();
                        JTextArea ta = new JTextArea(20, 40);
                        JFrame frame = new JFrame();
                        JTextArea label = new JTextArea();
                        label.setLineWrap(true);
                        label.setEditable(false);
                        frame.setSize(300, 300);
                        frame.setBounds(20, 20, 300, 400);
                        label.setText(critiques_list.get(i).getDescription());
                        frame.add(label);
                        frame.setVisible(true);
                        ta.setLineWrap(true);
                        int n = JOptionPane.showConfirmDialog(null, ta, "Respond!", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION);

                        Critique crit = new Critique();
                        if(!ta.getText().isEmpty()){
                        crit.setReply(ta.getText());
                        crit.setCritiqueID(critiques_list.get(i).getCritiqueID());
                        response.addCritique(crit);}
                        frame.dispose();
                    }

                    if(response.getCritiques().size() == critiques_list.size()) {
                        try {
                            response.setSubmissionArticleID(art.getArticleID());
                            response.setReviewID(our_review.getReviewID());

                            boolean success = SessionData.db.submitReviewResponse(response, SessionData.currentUser);
                            if (success) {
                                JOptionPane.showMessageDialog(null, "Responses submitted!");
                            } else {
                                JOptionPane.showMessageDialog(null, "Sorry something went wrong!");
                            }
                        } catch (InvalidAuthenticationException e1) {
                            e1.printStackTrace();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        } catch (UserDoesNotExistException e1) {
                            e1.printStackTrace();
                        }
                    } else{
                        JOptionPane.showMessageDialog(null, "You did not reply to all the critiques.");
                        MainAuthorArea back = new MainAuthorArea();
                        back.setVisible(true);
                        dispose();
                    }

            }
        });

        generateReviewsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Review> reviews_list = new ArrayList<Review>();
                ArrayList<Article> articles_list = null;
                reviews.removeAllItems();

                try {
                    articles_list = SessionData.db.getOwnArticles(SessionData.currentUser);
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                Article art = null;

                for (Article er : articles_list){
                    if (er.getTitle().equals(articles.getSelectedItem())){
                        art = er;
                        break;
                    }
                }

                try {
                    reviews_list = SessionData.db.getInitialReviewsOfArticle(art, SessionData.currentUser);
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                for(int i = 0; i< reviews_list.size() ; i++) {
                    reviews.addItem(reviews_list.get(i).getReviewerPseudonym());
                }

                Review our_review = null;

                for (Review rev : reviews_list){
                    if(rev.getReviewerPseudonym().equals(reviews.getSelectedItem())){
                        our_review = rev;
                        break;
                    }
                }



            }
        });

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainAuthorArea backtoarea = new MainAuthorArea();
                backtoarea.setVisible(true);
                dispose();
            }
        });



    }
}
