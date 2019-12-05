import com.mysql.cj.Session;
import exceptions.InvalidAuthenticationException;
import exceptions.ObjectDoesNotExistException;
import exceptions.UserDoesNotExistException;
import main.*;
import models.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class FinalDecision extends JFrame{
    public JComboBox comboBox1;
    private JComboBox comboBox2;
    private JLabel verdict1;
    private JLabel verdict2;
    private JLabel verdict3;
    private JButton rejectButton;
    private JButton acceptButton;
    private JButton backward;
    private JPanel FinalDecision;
    private JButton generateArticlesButton;
    private JButton generateVerdictsButton;
    private Journal[] journal_list;
    private ArrayList<Article> articles_list;
    private ArrayList<String> list_of_verdicts;



    public FinalDecision(){
        add(FinalDecision);
        setTitle("Final Decision Page");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        journal_list = new Journal[0];

        try {
            journal_list = SessionData.db.getJournalsByUser(SessionData.currentUser).toArray(new Journal[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        } catch (InvalidAuthenticationException e) {
            e.printStackTrace();
        }
        for(int i = 0; i<= journal_list.length-1 ; i++) {
            comboBox1.addItem(journal_list[i].getName());
        }

        generateArticlesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //volume_list = new Volume[0];
                comboBox2.removeAllItems();
                articles_list = null;

                Journal joure = null;
                for (Journal journal : journal_list){
                    if (journal.getName().equals(comboBox1.getSelectedItem())){
                        joure = journal;
                        break;
                    }
                }

                try {
                    articles_list = SessionData.db.getJournalArticlesNeedingEditorDecision(joure,SessionData.currentUser);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (ObjectDoesNotExistException e1) {
                    e1.printStackTrace();
                }

                for(int i = 0; i < articles_list.size() ; i++) {
                    comboBox2.addItem(articles_list.get(i).getTitle());
                }
            }
        });

        generateVerdictsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //volume_list = new Volume[0];
                verdict1.setText("");
                verdict2.setText("");
                verdict3.setText("");
                articles_list = null;

                Journal joure = null;
                for (Journal journal : journal_list){
                    if (journal.getName().equals(comboBox1.getSelectedItem())){
                        joure = journal;
                        break;
                    }
                }

                try {
                    articles_list = SessionData.db.getJournalArticlesNeedingEditorDecision(joure,SessionData.currentUser);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (ObjectDoesNotExistException e1) {
                    e1.printStackTrace();
                }

                Article art = null;
                for (Article arti : articles_list ){
                    if(arti.getTitle().equals(comboBox2.getSelectedItem())){
                        art = arti;
                        break;
                    }
                }

                try {
                    list_of_verdicts = SessionData.db.getAllVerdictsForArticle(art,SessionData.currentUser);
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (ObjectDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                }

                verdict1.setText(list_of_verdicts.get(0));
                verdict2.setText(list_of_verdicts.get(1));
                verdict3.setText(list_of_verdicts.get(2));


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

        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                articles_list = null;

                Journal joure = null;
                for (Journal journal : journal_list){
                    if (journal.getName().equals(comboBox1.getSelectedItem())){
                        joure = journal;
                        break;
                    }
                }

                try {
                    articles_list = SessionData.db.getJournalArticlesNeedingEditorDecision(joure,SessionData.currentUser);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (ObjectDoesNotExistException e1) {
                    e1.printStackTrace();
                }

                Article art = null;
                for (Article arti : articles_list ){
                    if(arti.getTitle().equals(comboBox2.getSelectedItem())){
                        art = arti;
                        break;
                    }
                }

                art.isAccepted();

                try {
                    SessionData.db.setIsAcceptedOrRejected(art, SessionData.currentUser);
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (ObjectDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                }
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                articles_list = null;

                Journal joure = null;
                for (Journal journal : journal_list){
                    if (journal.getName().equals(comboBox1.getSelectedItem())){
                        joure = journal;
                        break;
                    }
                }

                try {
                    articles_list = SessionData.db.getJournalArticlesNeedingEditorDecision(joure,SessionData.currentUser);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (ObjectDoesNotExistException e1) {
                    e1.printStackTrace();
                }

                Article art = null;
                for (Article arti : articles_list ){
                    if(arti.getTitle().equals(comboBox2.getSelectedItem())){
                        art = arti;
                        break;
                    }
                }
                
                try {
                    SessionData.db.setIsAcceptedOrRejected(art, SessionData.currentUser);
                } catch (InvalidAuthenticationException e1) {
                    e1.printStackTrace();
                } catch (ObjectDoesNotExistException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    e1.printStackTrace();
                }
            }
        });



    }
}
