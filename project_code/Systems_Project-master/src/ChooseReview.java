import com.mysql.cj.Session;
import exceptions.InvalidAuthenticationException;
import exceptions.ObjectDoesNotExistException;
import exceptions.UserDoesNotExistException;
import models.*;
import main.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChooseReview extends JFrame {
    private JPanel ChooseReview;
    public JComboBox article_selection_CB;
    private JButton send_review;
    private JButton backward;
    private JComboBox comboBox1;
    private JTextArea summary;
    private JTextArea critique_field;
    private JButton getContentOfChosenButton;
    private JTextField chosenarticletitle;
    private JTextArea chosenarticleabstract;

    private ArrayList<Critique> critiques;
    private Review selected = null;

    ChooseReview() {
        add(ChooseReview);
        setTitle("Choose Review Page");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        summary.setLineWrap(true);
        critique_field.setLineWrap(true);

        String[] verdicts = {"Strong Accept (champion)", "Weak Accept", "Weak Reject", "Strong Reject (detractor)"};

        for(int i = 0; i<= verdicts.length-1 ; i++) {
            comboBox1.addItem(verdicts[i]);
        }

        ArrayList<Review> choose_list = new ArrayList<Review>();

        try {
            choose_list = SessionData.db.emptyReviews(SessionData.currentUser);
        } catch (Exception ee){
            JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
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
                if(selected==null){
                    // box
                    return;
                }

                if(!summary.getText().isEmpty() && !critique_field.getText().isEmpty()) {
                    Critique critique = new Critique();
                    critique.setDescription(critique_field.getText());
                    selected.addCritique(critique);

                    aa:
                    while (true) {
                        int dialogButton = JOptionPane.YES_NO_CANCEL_OPTION;
                        int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to add another critique?",
                                "Another critique?", dialogButton);

                        if (dialogResult == 0) { // if yes
                            int option = JOptionPane.YES_NO_OPTION;
                            JTextArea ta = new JTextArea(20, 20);
                            ta.setLineWrap(true);
                            switch (JOptionPane.showConfirmDialog(null, ta, "Box Title", option)) {
                                case JOptionPane.YES_OPTION:
                                    Critique crit = new Critique();
                                    crit.setDescription(ta.getText());
                                    selected.addCritique(crit);
                                    break;
                            }
                        }

                        if (dialogResult == -1 || dialogResult == 2) {
                            JOptionPane.showMessageDialog(null, "Process Cancelled");
                            ChooseReview backtoarea = new ChooseReview();
                            selected = null;
                            break aa;
                        }

                        if (dialogResult == 1) { // if no
                            selected.setSummary(summary.getText());
                            selected.setVerdict((String) comboBox1.getSelectedItem());

                            try {
                                for (Critique crit : selected.getCritiques())
                                    System.out.println(crit.getDescription());
                                boolean success = SessionData.db.submitReview(selected, SessionData.currentUser);

                                if (!success) {
                                    throw new SQLException();
                                }
                                JOptionPane.showMessageDialog(null, "Review Sent");
                                ReviewerArea backtoarea = new ReviewerArea();
                                backtoarea.setVisible(true);
                                dispose();
                                break;

                            } catch (Exception e1) {
                                e1.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Something went wrong. Please try again.");
                            }
                        }


                    }
                } else{
                    JOptionPane.showMessageDialog(null, "Make sure you fill out the form correctly!");
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
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "Something went wrong.");
                    e1.printStackTrace();
                    return;
                }

                for (Review awd : lister){
                    if (awd.getArticleName().equals(article_selection_CB.getSelectedItem())){
                        selected = awd;
                        break;
                    }
                }

                try {
                    abc = SessionData.db.getArticleInfo(selected.getSubmissionArticleID());
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "Something went wrong.");
                    e1.printStackTrace();
                    return;
                }

                try {
                    abc.savePdfToPC();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "Something went wrong.");
                    e1.printStackTrace();
                    return;
                }
                chosenarticletitle.setText(abc.getTitle());
                chosenarticleabstract.setText(abc.getSummary());

            }});


    }
}
