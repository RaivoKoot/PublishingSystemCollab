import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubmitRevision extends JFrame{
    private JPanel SubmitRevision;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JTextArea textArea1;
    private JTextField content;
    private JButton submitRevisionButton;
    private JButton backward;

    public SubmitRevision(){
        add(SubmitRevision);
        setTitle("Chief Editor Area");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        textArea1.setLineWrap(true);

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
