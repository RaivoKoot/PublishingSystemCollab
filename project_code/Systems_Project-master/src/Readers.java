import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import models.*;
import main.SessionData;
import database_interface.*;
import java.util.*;

import java.sql.SQLException;

public class Readers {
    private JFrame frame;
    private DefaultMutableTreeNode Journals;
    private DefaultMutableTreeNode Journal_names;
    private DefaultMutableTreeNode Volumes;
    private DefaultMutableTreeNode volumesID;
    private JTree tree;

    public Readers(){
        frame = new JFrame("Content");
        Volumes = new DefaultMutableTreeNode("Volumes");
        Journals = new DefaultMutableTreeNode("Journals");

    }

    public void show(){
        frame.setBounds(45,45,500,250);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Journal[] journal_list = new Journal[0];
        List<String> journals_list = new ArrayList<String>();
        List<String> volumes_list = new ArrayList<String>();


        try {
            journal_list = SessionData.db.getAllJournals().toArray(new Journal[0]);
            //volumes_list = SessionData.db.getAllJournals().toArray(new Volume[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(int i = 0; i<= journal_list.length-1 ; i++) {
            journals_list.add(journal_list[i].getName());
        }

        for (int i=0; i<journals_list.size(); i++) {
            Journal_names = new DefaultMutableTreeNode((journals_list.get(i)));
            Journals.add(Journal_names);
            for (int n=0; n<volumes_list.size(); n++) {
                Volumes = new DefaultMutableTreeNode((volumes_list.get(n)));
                volumesID.add(Journal_names);
            }
        }

        tree = new JTree(Journals);
        tree.setBounds(45,45,150,100);
        frame.add(tree);
    }

    public static void main(String[] args) {
        Readers r = new Readers();
        r.show();
    }
}
