import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import main.*;
import exceptions.InvalidAuthenticationException;
import exceptions.UserDoesNotExistException;
import models.*;
import database_interface.*;

public class RespondToCritiques extends JFrame {
    private JPanel RespondToCritiques;
    private JComboBox articles;
    private JComboBox reviews;
    private JButton respondButton;
    private JButton backward;
    private JButton generateReviewsButton;

    public RespondToCritiques(){
        add(RespondToCritiques);
        setTitle("Respond to Critiques Area");
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
            articles.addItem(articles_list.get(i).getTitle());
        }

        respondButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] critiques = {"This is bad", "How can u do this!", "OMG!"};
                String[] options = {"Next"};
                for(int i = 0; i< critiques.length; i++){
                        JTextArea ta = new JTextArea(20, 40);
                        JFrame frame = new JFrame();
                        JLabel label = new JLabel();
                        frame.setSize(300,300);
                        frame.setBounds(20,20,300,400);
                        label.setText(critiques[i]);
                        frame.add(label);
                        frame.setVisible(true);

                        JOptionPane.showConfirmDialog(null, ta,"respond!", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION);
                        System.out.println(ta.getText());
                        frame.dispose();
                    }
            }
        });

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
