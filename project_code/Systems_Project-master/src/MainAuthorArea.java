import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MainAuthorArea extends JFrame{
    private JButton registerCoAuthorsButton;
    private JButton checkArticleStatusButton;
    private JButton respondToCriticismsButton;
    private JButton reviseArticleButton;
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
                if(registerArea.comboBox1.getItemCount() == 0){
                    JOptionPane.showMessageDialog(null, "Sorry you do not have permission to access this page, " +
                            "try submitting an article");
                }else{
                registerArea.setVisible(true);
                dispose();}
            }
        });

        checkArticleStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CheckArticleStatus check = new CheckArticleStatus();
                if(check.comboBox1.getItemCount() == 0){
                    JOptionPane.showMessageDialog(null, "Sorry you do not have any article to check the status of, " +
                            "try submitting an article");
                }else{
                check.setVisible(true);
                dispose();}
            }
        });

        respondToCriticismsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RespondToCritiques respondPage = new RespondToCritiques();
                if(respondPage.articles.getItemCount() == 0){
                    JOptionPane.showMessageDialog(null, "Sorry you do not have any artcile that could have associated critiques, " +
                            "try submitting an article");
                }else{
                respondPage.setVisible(true);
                dispose();}
            }
        });

        reviseArticleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SubmitRevision revise_article = new SubmitRevision();
                if(revise_article.comboBox1.getItemCount() == 0){
                    JOptionPane.showMessageDialog(null, "Sorry you do not have any artcile that needs revision, " +
                            "try submitting an article");
                }else{
                revise_article.setVisible(true);
                dispose();}
            }
        });



    }




}
