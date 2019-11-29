import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegisterCoAuthors extends JFrame{
    private JTextField forename;
    private JTextField surname;
    private JTextField e_mail_address;
    private JTextField t_password;
    private JButton registerButton;
    private JButton doneButton;
    private JLabel Success;
    private JButton backward;
    private JPanel RegisterCo;
    private JPanel RegisterCoAuthors;
    Connection con = null;
    Statement stmt = null;
    Statement stmt2 = null;
    int n;

    public RegisterCoAuthors(){
        add(RegisterCo);
        setTitle("Register Author Page");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fname = forename.getText();
                String sname = surname.getText();
                String e_address = e_mail_address.getText();
                String te_password = t_password.getText();
            }
        });

        backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainAuthorArea backtoarea = new MainAuthorArea();
                backtoarea.setVisible(true);
                dispose();
            }});
    }
}
