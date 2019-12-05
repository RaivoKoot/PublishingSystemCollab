import models.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import main.*;

public class PublishEdition extends JFrame {

    private JPanel editionPanel;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JButton publishEditionButton;
    private JButton generateVolumesButton;
    private JButton generateEditionsButton;
    private JButton backward;

    public PublishEdition() {
        add(editionPanel);
        setTitle("Publish Edition Area");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChiefEditorArea main_screen = new ChiefEditorArea();
                main_screen.setVisible(true);
                dispose();
            }
        });

    }

}

