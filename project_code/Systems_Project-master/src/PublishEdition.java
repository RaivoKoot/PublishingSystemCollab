import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PublishEdition extends JFrame{

    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton publishButton;
    private JPanel editionPanel;
    private JButton backward;

    public PublishEdition() {
        add(editionPanel);

        setTitle("Publish Edition Page");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChiefEditorArea chiefarea = new ChiefEditorArea();
                chiefarea.setVisible(true);
                dispose();

            }
        });
    }
}
