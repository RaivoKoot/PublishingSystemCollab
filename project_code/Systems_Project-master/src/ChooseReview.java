import com.mysql.cj.Session;
import exceptions.InvalidAuthenticationException;
import exceptions.ObjectDoesNotExistException;
import exceptions.UserDoesNotExistException;
import models.*;
import main.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChooseReview extends JFrame {
    private JPanel ChooseReview;
    private JComboBox article_selection_CB;
    private JButton send_review;
    private JButton backward;
    private JComboBox comboBox1;
    private JTextArea summary;
    private JTextArea critique_field;
    private JButton getContentOfChosenButton;
    private JTextField chosenarticletitle;
    private JTextArea chosen_article_content;
    private JTextArea chosenarticleabstract;

    private ArrayList<Critique> critiques;
    private Review selected;

    ChooseReview() {
        add(ChooseReview);
        setTitle("Choose Review Page");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



        String[] verdicts = {"Strong Accept (champion)", "Weak Accept", "Weak Reject", "Strong Reject (detractor)"};

        for(int i = 0; i<= verdicts.length-1 ; i++) {
            comboBox1.addItem(verdicts[i]);
        }

        ArrayList<Review> choose_list = new ArrayList<Review>();

        try {
            choose_list = SessionData.db.emptyReviews(SessionData.currentUser);
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        } catch (InvalidAuthenticationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < choose_list.size() ; i++) {
            article_selection_CB.addItem(choose_list.get(i).getArticleName());
        }

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReviewerArea backtoarea = new ReviewerArea();
                backtoarea.setVisible(true);
                dispose();
            }});

        send_review.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                critiques = new ArrayList<>();
                Critique critique = new Critique();
                critique.setDescription(critique_field.getText());
                critiques.add(critique);

                while(true) {
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to add another critique?",
                            "Another critique?", dialogButton);
                    if (dialogResult == 0) {
                        JTextArea ta = new JTextArea(20, 20);
                        switch (JOptionPane.showConfirmDialog(null, new JScrollPane(ta))) {
                            case JOptionPane.OK_OPTION:
                                JOptionPane.showMessageDialog(null, "Critique added");
                                critique = new Critique();
                                critique.setDescription(ta.getText());
                                critiques.add(critique);

                                break;
                        }
                    } else {

                        for(Critique crit: critiques)
                            crit.setReviewID(selected.getReviewID());
                        // db.functionCall(Review, critiques, currentUser)

                        JOptionPane.showMessageDialog(null, "Review Sent");
                        ReviewerArea backtoarea = new ReviewerArea();
                        backtoarea.setVisible(true);
                        dispose();
                        break;
                    }
                }
            }});

        getContentOfChosenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selected = new Review();
                Article abc = new Article();
                ArrayList<Review> lister = new ArrayList<Review>();

                try {
                    lister = SessionData.db.emptyReviews(SessionData.currentUser);
                } catch (UserDoesNotExistException ex) {
                    ex.printStackTrace();
                } catch (InvalidAuthenticationException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                for (Review awd : lister){
                    if (awd.getArticleName().equals(article_selection_CB.getSelectedItem())){
                        selected = awd;
                        break;
                    }
                }

                try {
                    abc = SessionData.db.getArticleInfo(selected.getSubmissionArticleID());
                } catch (ObjectDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }


                chosenarticletitle.setText(abc.getTitle());
                chosen_article_content.setText(abc.getContent());
                chosenarticleabstract.setText(abc.getSummary());

            }});


    }
}
