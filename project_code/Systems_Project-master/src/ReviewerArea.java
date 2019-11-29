import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReviewerArea extends JFrame{
    private JPanel ReviewerPanel;
    private JButton appointNewReviewButton;
    private JButton submitReviewButton;
    private JButton backward;

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
                // How to keep this kind of session which reminds of the username on the welcomeLabel in ChiefEditorArea named "LoggedAs" ? However not immportant Feature
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
