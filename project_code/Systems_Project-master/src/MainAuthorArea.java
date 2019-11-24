import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MainAuthorArea extends JFrame{
    private JButton registerCoAuthorsButton;
    private JButton checkArticleStatusButton;
    private JButton respondToCriticismsButton;
    private JButton checkReviewsButton;
    private JButton updatePasswordButton;
    private JPanel MAPanel;
    private JButton backward;
    Connection con = null;
    Statement stmt = null;

    public MainAuthorArea(){
        add(MAPanel);
        setTitle("Main Author Area");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainScreen main_screen = new MainScreen();
                main_screen.setVisible(true);
                dispose();
            }
        });

        registerCoAuthorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterCoAuthors registerArea = new RegisterCoAuthors();
                registerArea.setVisible(true);
                dispose();
            }
        });

    }




}
