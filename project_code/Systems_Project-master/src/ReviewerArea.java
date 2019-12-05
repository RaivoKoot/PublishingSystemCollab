import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReviewerArea extends JFrame{
    private JPanel ReviewerPanel;
    private JButton appointNewReviewButton;
    private JButton submitReviewButton;
    private JButton backward;
    private JButton yourResponsesButton;

    ReviewerArea(){
        add(ReviewerPanel);
        setTitle("Reviewer Area");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        appointNewReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                AppointReview appointpage = new AppointReview();

                if(appointpage.own_article_CB.getItemCount() == 0){
                    JOptionPane.showMessageDialog(null, "You are not authorised to create a new review. This is because " +
                            "you do not have any submissions that require you to contribute to any more reviews.");
                } else if (appointpage.selection_CB.getItemCount() == 0){
                    JOptionPane.showMessageDialog(null, "Unfortunately, there are no articles at the moment " +
                            "that still need reviews and to which you are unaffiliated to. Please be patient and wait until new articles are submitted.");
                } else {
                    appointpage.setVisible(true);
                    dispose();
                }
            }});

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainScreen backtoarea = new MainScreen();
                backtoarea.setVisible(true);
                dispose();
            }});

        submitReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                ChooseReview gotoarea = new ChooseReview();
                if(gotoarea.article_selection_CB.getItemCount() == 0){
                    JOptionPane.showMessageDialog(null, "You currently have no reviews appointed to yourself to submit. " +
                            "Go to 'Appoint New Review' and select a new article you want to review first.");
                } else {
                    gotoarea.setVisible(true);
                    dispose();
                }
            }});

        yourResponsesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Reviewer_responses backtoarea = new Reviewer_responses();
                if (backtoarea.comboBox1.getItemCount() == 0){
                    JOptionPane.showMessageDialog(null, "You currently have no reponses to your initial reviews. Please, Try later");
                }
                else {
                    backtoarea.setVisible(true);
                    dispose();
                }
            }});



    }
}
