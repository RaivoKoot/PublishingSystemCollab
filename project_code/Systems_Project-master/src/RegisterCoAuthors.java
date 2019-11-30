import exceptions.*;
import models.Journal;
import models.JournalEditor;
import models.User;
import main.*;
import models.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class RegisterCoAuthors extends JFrame {
    private JTextField forename;
    private JTextField surname;
    private JTextField e_mail_address;
    private JTextField t_password;
    private JButton registerButton;
    private JButton backward;
    private JPanel RegisterCo;
    private JPanel AppointPanel;
    private JPanel first_view;
    private JLabel Success;
    private JComboBox comboBox1;
    private JPanel second_view;
    private JTextField university;
    private JComboBox comboBox2;
    Connection con = null;
    Statement stmt = null;
    Statement stmt2 = null;
    public ArrayList<Article> article_list;
    int n;


    public RegisterCoAuthors() {
        add(RegisterCo);
        setTitle("Register Author Page");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] title_list = {"Dr.", "Prof.", "Mr.", "Mrs"};
        for(int i = 0; i<= title_list.length-1 ; i++) {
            comboBox2.addItem(title_list[i]);
        }

        try {
            article_list = SessionData.db.getOwnArticles(SessionData.currentUser);
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        } catch (InvalidAuthenticationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < article_list.size(); i++) {
            comboBox1.addItem(article_list.get(i).getTitle());
        }


        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fname = forename.getText();
                String sname = surname.getText();
                String e_address = e_mail_address.getText();
                String te_password = t_password.getText();
                String uni = university.getText();

                User target = new User();

                Article the_article = null;
                for (Article ret : article_list) {
                    if (ret.getTitle().equals(comboBox1.getSelectedItem())) {
                        the_article = ret;
                        break;
                    }
                }


                try {
                    target.setEmail(e_address);
                    target.setUniversity(uni);
                    target.setForenames(fname);
                    target.setPassword(te_password);
                    target.setSurname(sname);
                    target.setTitle((String) comboBox2.getSelectedItem());
                    boolean exist = SessionData.db.userExists(target);
                    if (!exist){
                        boolean register = SessionData.db.registerBaseUser(target);

                        if(!register)
                            throw new UserDoesNotExistException("registering user failed");
                    }
                    boolean success = SessionData.db.addCoAuthor(the_article, target, SessionData.currentUser);
                    if (success) {
                        Success.setText("Success");
                        forename.setText("");
                        surname.setText("");
                        university.setText("");
                        e_mail_address.setText("");
                        t_password.setText("");
                    } else{
                        Success.setText("Nope");
                    }

                } catch (InvalidAuthenticationException e1) {
                    JOptionPane.showMessageDialog(null, "You are not a chief editor of that journal");
                    e1.printStackTrace();
                } catch (UserDoesNotExistException e1) {
                    JOptionPane.showMessageDialog(null, "Sorry, something went wrong");
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Sorry, something went wrong");
                    e1.printStackTrace();
                } catch (ObjectDoesNotExistException e1) {
                    JOptionPane.showMessageDialog(null, "Sorry, something went wrong");
                    e1.printStackTrace();
                } catch (UserAlreadyExistsException e1) {
                    JOptionPane.showMessageDialog(null, "Sorry, something went wrong");
                    e1.printStackTrace();
                } catch (IncompleteInformationException e1){
                    JOptionPane.showMessageDialog(null, "User does not exist, fill in the entire form");
                    e1.printStackTrace();
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

