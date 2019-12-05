import javax.swing.*;
import main.*;
import models.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AssignArticle extends JFrame{
    private JPanel AssignArticle;
    public JComboBox journals;
    private JComboBox accepted_articles;
    private JButton generateAcceptedArticlesButton;
    private JButton generateEditionInfoButton;
    private JTextField textField1;
    private JButton backward;
    private JButton assignEditionButton;

    public AssignArticle(){
        add(AssignArticle);
        setTitle("Assign an Edition Form");
        setSize(700, 500);
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
