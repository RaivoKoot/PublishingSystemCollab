import javax.swing.*;

import exceptions.EditionFullException;
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
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private Edition my_edition;
    private ArrayList<Article> accepted_articles_list;
    private Article selected_article;
    private Journal own_journal;

    public AssignArticle(){
        add(AssignArticle);
        setTitle("Assign an Article to Edition Form");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ArrayList<Journal> journals_list =null;

        try {
            journals_list = SessionData.db.getJournalsByUser(SessionData.currentUser);
        } catch (Exception ee){
            JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
        }

        for(int i = 0; i< journals_list.size() ; i++) {
            journals.addItem(journals_list.get(i).getName());
        }

        generateAcceptedArticlesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Journal> journals_list =null;
                accepted_articles_list = new ArrayList<>();

                try {
                    journals_list = SessionData.db.getJournalsByUser(SessionData.currentUser);
                } catch (Exception ee){
                    JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
                }

                own_journal = null;

                for (Journal tar : journals_list){
                    if (tar.getName().equals(journals.getSelectedItem())){
                        own_journal = tar;
                        break;
                    }
                }

                try {
                    accepted_articles_list = SessionData.db.getAcceptedArticlesByJournal(own_journal,SessionData.currentUser);
                } catch (Exception ee){
                    JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
                }

                for(int i = 0; i< accepted_articles_list.size() ; i++) {
                    accepted_articles.addItem(accepted_articles_list.get(i).getTitle());
                }


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
                ArrayList<Journal> journals_list =null;
                my_edition = new Edition();
                try {
                    journals_list = SessionData.db.getJournalsByUser(SessionData.currentUser);
                } catch (Exception ee){
                    JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
                }

                Journal own_journal = null;

                for (Journal tar : journals_list){
                    if (tar.getName().equals(journals.getSelectedItem())){
                        own_journal = tar;
                        break;
                    }
                }


                try {
                    my_edition = SessionData.db.getLatestEdition(own_journal,SessionData.currentUser);
                } catch (Exception ee){
                    JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
                }

                if(my_edition.getVolumeNum() == 0 || my_edition.getEditionNum() == 0){
                    JOptionPane.showMessageDialog(null,"This journal has currently no available edition in a volume. Either create a new edition or create a new volume and edition.");
                }
                else {
                    label1.setText(String.valueOf(my_edition.getVolumeNum()));
                    label2.setText(String.valueOf(my_edition.getEditionNum()));
                    label3.setText(String.valueOf(my_edition.getCurrentLastPage() + 1));
                }

                String selected_article_name = (String) accepted_articles.getSelectedItem();

                for(Article article: accepted_articles_list)
                    if(article.getTitle().equals(selected_article_name)){
                        selected_article = article;
                        break;
                    }

                    System.out.println(selected_article);
            }
        });

        assignEditionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Journal> journals_list = null;
                //Edition my_edition = new Edition();
                ArrayList<EditionArticle> lister = null;

                if (!textField1.getText().isEmpty()) {
                    EditionArticle article = new EditionArticle();
                    article.setArticleID(selected_article.getArticleID());
                    article.setEditionID(my_edition.getEditionID());
                    article.setStartingPage(my_edition.getCurrentLastPage() + 1);
                    article.setEndingPage(Integer.parseInt(textField1.getText()));
                    article.setIssn(own_journal.getISSN());

                    try {
                        boolean success = SessionData.db.assignArticleToEdition(article, SessionData.currentUser);
                        if (success) {
                            JOptionPane.showMessageDialog(null, "Article successfully assigned to Edtion");
                            ChiefEditorArea getback = new ChiefEditorArea();
                            getback.setVisible(true);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Something went wrong!");
                        }
                    } catch (EditionFullException e1) {
                        JOptionPane.showMessageDialog(null,"Edition Full!");
                        e1.printStackTrace();
                    } catch (Exception ee){
                        JOptionPane.showMessageDialog(null,"Sorry something went wrong!");
                    }

                } else{JOptionPane.showMessageDialog(null,"Make sure you set the Ending Page!");}
            }
        });




    }
}
