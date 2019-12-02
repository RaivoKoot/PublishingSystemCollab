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
                appointpage.setVisible(true);
                dispose();
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
                gotoarea.setVisible(true);
                dispose();
            }});



    }
}
