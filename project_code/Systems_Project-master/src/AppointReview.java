import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import com.mysql.cj.Session;
import exceptions.InvalidAuthenticationException;
import exceptions.UserDoesNotExistException;
import main.*;
import models.*;

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

        Article[] reviews_list = new Article[0];
        Article[] reviews2_list = new Article[0];

        try {
            reviews_list = SessionData.db.articlesNeedingContributions(SessionData.currentUser).toArray(new Article[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        } catch (InvalidAuthenticationException e) {
            e.printStackTrace();
        }

        try {
            reviews2_list = SessionData.db.getUnaffiliatedArticlesToReview(SessionData.currentUser).toArray(new Article[0]);
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        } catch (InvalidAuthenticationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i<= reviews_list.length-1 ; i++) {
            own_article_CB.addItem(reviews_list[i].getTitle());
        }

        for(int i = 0; i<= reviews2_list.length-1 ; i++){
            selection_CB.addItem(reviews2_list[i].getTitle());
        }

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReviewerArea backtoarea = new ReviewerArea();
                backtoarea.setVisible(true);
                dispose();
            }});


    }
}
