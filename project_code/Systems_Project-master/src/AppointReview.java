import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReviewerArea backtoarea = new ReviewerArea();
                backtoarea.setVisible(true);
                dispose();
                // How to keep this kind of session which reminds of the username on the welcomeLabel in ChiefEditorArea named "LoggedAs" ? However not immportant Feature
            }});
    }
}
