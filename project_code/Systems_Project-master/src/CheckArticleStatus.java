import javax.swing.*;

import exceptions.InvalidAuthenticationException;
import exceptions.ObjectDoesNotExistException;
import exceptions.UserDoesNotExistException;
import main.*;
import models.*;
import main.*;
import database_interface.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class CheckArticleStatus extends JFrame {
    private JComboBox comboBox1;
    private JButton getInfoButton;
    private JTextField InitialReviews;
    private JTextField Contributions;
    private JTextField Reponses;
    private JTextField RevisedStatus;
    private JTextField FinalReviews;
    private JTextField Decisions;
    private JPanel CheckPanel;
    private JButton backward;

    public CheckArticleStatus(){
        add(CheckPanel);
        setTitle("Check Status View");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ArrayList<Article> articles_list = null;

        try {
            articles_list = SessionData.db.getOwnArticles(SessionData.currentUser);
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        } catch (InvalidAuthenticationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i< articles_list.size() ; i++) {
            comboBox1.addItem(articles_list.get(i).getTitle());
        }
/*
        Article art = null;

        for (Article er : articles_list){
            if (er.getTitle().equals(comboBox1.getSelectedItem())){
                art = er;
                break;
            }
        }
        */

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainAuthorArea backtoarea = new MainAuthorArea();
                backtoarea.setVisible(true);
                dispose();
            }
        });


        getInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                InitialReviews.setText("");
                Contributions.setText("");
                Reponses.setText("");
                RevisedStatus.setText("");
                FinalReviews.setText("");
                Decisions.setText("");

            }
        });

    }


}


