import com.mysql.cj.Session;
import exceptions.InvalidAuthenticationException;
import exceptions.ObjectDoesNotExistException;
import exceptions.UserDoesNotExistException;
import models.*;
import main.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public class Reviewer_responses extends JFrame{

    private JPanel Responses;
    public JComboBox comboBox1;
    private JTextField summary;
    private JButton getInfoButton;
    private JTextField titre;
    private JTextArea content;
    private JButton viewCritiquesAndGiveButton;
    private JButton backward;


    public Reviewer_responses(){
        add(Responses);
        setTitle("Reviewer Responses Area");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ArrayList<Article> lister = null;
        String[] verdicts = {"Strong Accept (champion)", "Weak Accept", "Weak Reject", "Strong Reject (detractor)"};

        try {
            lister =  SessionData.db.getArticlesNeedingFinalVerdicts(SessionData.currentUser);
        } catch (Exception ee){
            JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
        }

        for(int i = 0; i< lister.size() ; i++) {
            comboBox1.addItem(lister.get(i).getTitle());
        }

        getInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Article> lister = null;

                try {
                    lister =  SessionData.db.getArticlesNeedingFinalVerdicts(SessionData.currentUser);
                } catch (Exception ee){
                    JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
                }

                Article art = null;

                for (Article er : lister){
                    if (er.getTitle().equals(comboBox1.getSelectedItem())){
                        art = er;
                        break;
                    }
                }

                summary.setText(art.getSummary());
                titre.setText(art.getTitle());
                content.setText(art.getContent());
            }
        });

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReviewerArea backtoarea = new ReviewerArea();
                backtoarea.setVisible(true);
                dispose();
            }
        });

        viewCritiquesAndGiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Article> lister = null;
                ArrayList<Critique> liste = null;


                try {
                    lister =  SessionData.db.getArticlesNeedingFinalVerdicts(SessionData.currentUser);
                } catch (Exception ee){
                    JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
                }

                Article art = null;

                for (Article er : lister){
                    if (er.getTitle().equals(comboBox1.getSelectedItem())){
                        art = er;
                        break;
                    }
                }



                Review review = new Review();
                review.setReviewID(art.getReviewID());
                review.setSubmissionArticleID(art.getArticleID());

                try {
                    liste = SessionData.db.getReviewCritiques(review, SessionData.currentUser);
                } catch (Exception ee){
                    JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
                }

                for(int i = 0; i<liste.size();i++){
                    JScrollPane pane = new JScrollPane();
                    JTextArea ta = new JTextArea(20, 40);
                    JFrame frame = new JFrame();
                    JTextArea label = new JTextArea();
                    label.setLineWrap(true);
                    label.setEditable(false);
                    frame.setSize(300, 300);
                    frame.setBounds(20, 20, 300, 400);
                    label.setText(liste.get(i).getReply());
                    frame.add(label);
                    frame.setVisible(true);
                    ta.setLineWrap(true);
                    JTextArea te = new JTextArea(30,70);
                    te.setSize(200,200);
                    te.setLineWrap(true);
                    te.setEditable(false);
                    te.setText(liste.get(i).getDescription());
                    JOptionPane.showMessageDialog(null,te);
                    frame.dispose();
                }

                int final_verdict =  JOptionPane.showOptionDialog(null,
                        "Choose your Final Verdict",
                        "Final Verdict Box",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,     //do not use a custom Icon
                        verdicts,  //the titles of buttons
                        null); //default button title

                if (final_verdict == 0){
                    review.setVerdict(verdicts[0]);
                    try {
                        boolean success = SessionData.db.giveFinalVerdict(review,SessionData.currentUser);
                        if(success){
                            JOptionPane.showMessageDialog(null,"Final Verdict given!");
                            ReviewerArea back_view = new ReviewerArea();
                            back_view.setVisible(true);
                            dispose();
                        }
                        else{
                            JOptionPane.showMessageDialog(null,"Something went wrong!");
                        }
                    } catch (Exception ee){
                        JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
                    }
                }
                else if (final_verdict == 1){
                    review.setVerdict(verdicts[1]);
                    try {
                        boolean success = SessionData.db.giveFinalVerdict(review,SessionData.currentUser);
                        if(success){
                            JOptionPane.showMessageDialog(null,"Final Verdict given!");
                            ReviewerArea back_view = new ReviewerArea();
                            back_view.setVisible(true);
                            dispose();
                        }
                        else{
                            JOptionPane.showMessageDialog(null,"Something went wrong!");
                        }
                    } catch (Exception ee){
                        JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
                    }
                }
                else if (final_verdict == 2) {
                    review.setVerdict(verdicts[2]);
                    try {
                        boolean success = SessionData.db.giveFinalVerdict(review,SessionData.currentUser);
                        if(success){
                            JOptionPane.showMessageDialog(null,"Final Verdict given!");
                            ReviewerArea back_view = new ReviewerArea();
                            back_view.setVisible(true);
                            dispose();
                        }
                        else{
                            JOptionPane.showMessageDialog(null,"Something went wrong!");
                        }
                    } catch (Exception ee){
                        JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
                    }
                }
                else if (final_verdict == 3){
                    review.setVerdict(verdicts[3]);
                    try {
                        boolean success = SessionData.db.giveFinalVerdict(review,SessionData.currentUser);
                        if(success){
                            JOptionPane.showMessageDialog(null,"Final Verdict given!");
                            ReviewerArea back_view = new ReviewerArea();
                            back_view.setVisible(true);
                            dispose();
                        }
                        else{
                            JOptionPane.showMessageDialog(null,"Something went wrong!");
                        }
                    } catch (Exception ee){
                        JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
                    }
                }
            }
        });




    }
}
