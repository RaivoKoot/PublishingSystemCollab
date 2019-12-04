import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import exceptions.InvalidAuthenticationException;
import exceptions.ObjectDoesNotExistException;
import exceptions.UserDoesNotExistException;
import main.*;
import models.*;

public class SubmitRevision extends JFrame{
    private JPanel SubmitRevision;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JTextArea textArea1;
    private JButton submitRevisionButton;
    private JButton backward;
    private JTextArea content;
    private ArrayList<Article> articles_for_revision;

    public SubmitRevision(){
        add(SubmitRevision);
        setTitle("Chief Editor Area");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        textArea1.setLineWrap(true);
        content.setLineWrap(true);

        try {
            articles_for_revision = SessionData.db.getArticlesNeedingRevision(SessionData.currentUser);
        } catch (InvalidAuthenticationException e) {
            e.printStackTrace();
        } catch (ObjectDoesNotExistException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        }

        for(int i = 0; i< articles_for_revision.size() ; i++) {
            comboBox1.addItem(articles_for_revision.get(i).getTitle());
        }

        Article tar = null;

        for (Article ter : articles_for_revision){
            if (ter.getTitle().equals(comboBox1.getSelectedItem())){
                tar = ter;
                break;
            }
        }


        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainAuthorArea backtoarea = new MainAuthorArea();
                backtoarea.setVisible(true);
                dispose();
            }
        });

        submitRevisionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    articles_for_revision = SessionData.db.getArticlesNeedingRevision(SessionData.currentUser);
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (ObjectDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                }

                Article tar = null;

                for (Article ter : articles_for_revision){
                    if (ter.getTitle().equals(comboBox1.getSelectedItem())){
                        tar = ter;
                        break;
                    }
                }

                tar.setTitle(textField1.getText());
                tar.setContent(content.getText());
                tar.setSummary(textArea1.getText());
            }
        });

    }
}
