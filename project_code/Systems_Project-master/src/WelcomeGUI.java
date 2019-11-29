import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class WelcomeGUI extends JFrame{
    private JButton youMaySelfRegisterButton;
    private JPanel rootPanel;

    private JButton logInButton;
    private JButton updateContentButton;
    Connection con = null; // a Connection object
    Statement stmt = null;

    public WelcomeGUI()
    {
        add(rootPanel);

        setTitle("Welcome Page");
        setSize(400,500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        youMaySelfRegisterButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                rootPanel.setVisible(false);
                dispose();
                SubscribeForm sub = new SubscribeForm();
                sub.setVisible(true);
            }
        });



        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rootPanel.setVisible(false);
                dispose();
                LogIn logine = new LogIn();
                logine.setVisible(true);
            }
        });

        updateContentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Readers frame = new Readers();
                frame.show();
            }
        });

    }
}
