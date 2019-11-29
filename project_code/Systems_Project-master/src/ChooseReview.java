import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseReview extends JFrame {
    private JPanel ChooseReview;
    private JComboBox article_selection_CB;
    private JButton send_review;
    private JButton backward;
    private JTextArea summary;
    private JComboBox comboBox1;
    private JTextArea critique;

    ChooseReview() {
        add(ChooseReview);
        setTitle("Choose Review Page");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] months = {"Strong Accept (champion)", "Weak Accept", "Weak Reject", "Strong Reject (detractor)"};

        for(int i = 0; i<= months.length-1 ; i++) {
            comboBox1.addItem(months[i]);
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
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to add another critique?",
                        "Another critique?", dialogButton);
                if(dialogResult == 0) {
                    JTextArea ta = new JTextArea(20, 20);
                    switch (JOptionPane.showConfirmDialog(null, new JScrollPane(ta))) {
                        case JOptionPane.OK_OPTION:
                            JOptionPane.showMessageDialog(null, "Critique added");
                            
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Review Sent");
                    ReviewerArea backtoarea = new ReviewerArea();
                    backtoarea.setVisible(true);
                    dispose();
                }
            }});



    }
}
