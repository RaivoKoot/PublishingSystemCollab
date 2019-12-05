import javax.swing.*;

import exceptions.InvalidAuthenticationException;
import exceptions.ObjectDoesNotExistException;
import exceptions.UserDoesNotExistException;
import main.*;
import models.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

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
        ArrayList<Journal> journals_list =null;

        try {
            journals_list = SessionData.db.getJournalsByUser(SessionData.currentUser);
        } catch (InvalidAuthenticationException e) {
            e.printStackTrace();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int i = 0; i< journals_list.size() ; i++) {
            journals.addItem(journals_list.get(i).getName());
        }

        generateAcceptedArticlesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Journal> journals_list =null;
                ArrayList<Article> accepted_articles_list = new ArrayList<>();

                try {
                    journals_list = SessionData.db.getJournalsByUser(SessionData.currentUser);
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                Journal own_journal = null;

                for (Journal tar : journals_list){
                    if (tar.getName().equals(journals.getSelectedItem())){
                        own_journal = tar;
                        break;
                    }
                }

                try {
                    accepted_articles_list = SessionData.db.getAcceptedArticlesByJournal(own_journal,SessionData.currentUser);
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (ObjectDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                }

                for(int i = 0; i< accepted_articles_list.size() ; i++) {
                    accepted_articles.addItem(accepted_articles_list.get(i).getTitle());
                }

               /*
                Article the_article = null;

                for (Article tar : accepted_articles_list){
                    if (tar.getTitle().equals(journals.getSelectedItem())){
                        the_article = tar;
                        break;
                    }
                }*/

            }
        });

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChiefEditorArea main_screen = new ChiefEditorArea();
                main_screen.setVisible(true);
                dispose();
            }
        });

        generateEditionInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Edition> edition_info = null;

                //edition_info = SessionData.db.g
            }
        });




    }
}
