import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class UserList extends JFrame {
    DefaultTableModel model = new DefaultTableModel();
    Container cnt = this.getContentPane();
    JTable jtbl = new JTable(model);
    public UserList() {
        cnt.setLayout(new FlowLayout(FlowLayout.LEFT));
        model.addColumn("Forename");
        model.addColumn("Surname");
        model.addColumn("Role");
        model.addColumn("Username");
        model.addColumn("Password");
        try {
            String DB="jdbc:mysql://stusql.dcs.shef.ac.uk/team035?user=team035&password=5455d7fb";
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DB);
            PreparedStatement pstm = con.prepareStatement("SELECT * FROM editors");
            ResultSet Rs = pstm.executeQuery();
            while(Rs.next()){
                model.addRow(new Object[]{Rs.getString(1), Rs.getString(2),Rs.getString(3),Rs.getString(4),Rs.getString(5)});
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        JScrollPane pg = new JScrollPane(jtbl);
        cnt.add(pg);
        this.pack();
    }
}
