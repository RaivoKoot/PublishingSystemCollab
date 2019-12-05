import javax.swing.*;
import javax.swing.tree.*;

import models.*;
import main.SessionData;
import database_interface.*;

import java.awt.*;
import java.io.File;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.sql.SQLException;
import java.util.EventObject;

public class Readers extends TreePath {
    private static final java.io.File File = null;
    private JFrame frame;
    private DefaultMutableTreeNode Journals;
    private DefaultMutableTreeNode Journal_names;
    private JTree tree;

    public Readers(){
        frame = new JFrame("Content");
        Journals = new DefaultMutableTreeNode("Journals");

        Journal[] journal_list = new Journal[0];


        try {
            journal_list = SessionData.db.getAllJournals().toArray(new Journal[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        for (int i=0; i<journal_list.length; i++) {
            Journal_names = new DefaultMutableTreeNode((journal_list[i]));
            Journals.add(Journal_names);
        }

        tree = new JTree(Journals);
        tree.setBounds(45,45,150,100);
        frame.add(tree);

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {

                TreePath treepath = e.getPath();
                DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) treepath.getLastPathComponent();

                Object userObject = currentNode.getUserObject();

                if(userObject instanceof Journal) {


                    Journal journal = (Journal) currentNode.getUserObject();
                    Volume[] volumes = null;
                    try {
                        volumes = SessionData.db.getAllJournalVolumes(journal).toArray(new Volume[0]);
                    } catch (SQLException exc) {
                        exc.printStackTrace();
                    }
                    for (int n = 0; n < volumes.length; n++) {
                        DefaultMutableTreeNode volumeNode = new DefaultMutableTreeNode((volumes[n]));
                        currentNode.add(volumeNode);
                    }
                } else if (userObject instanceof Volume){
                    Volume volume = (Volume) currentNode.getUserObject();
                    Edition[] editions = null;
                    try {
                        editions = SessionData.db.getAllVolumeEditions(volume).toArray(new Edition[0]);
                    } catch (SQLException exc) {
                        exc.printStackTrace();
                    }
                    for (int n = 0; n < editions.length; n++) {
                        DefaultMutableTreeNode volumeNode = new DefaultMutableTreeNode((editions[n]));
                        currentNode.add(volumeNode);
                    }

                } /*else if(userObject instanceof Edition){
                    Edition edition = (Edition) currentNode.getUserObject();
                    Article[] articles = null;
                    try {
                        articles = SessionData.db.getallEditionArticles(edition).toArray(new Article[0]);
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                    for (int n = 0; n < articles.length; n++) {
                        DefaultMutableTreeNode volumeNode = new DefaultMutableTreeNode((articles[n]));
                        currentNode.add(volumeNode);
                    }

                }*/ else if(userObject instanceof Article){
                    Article article = (Article) userObject;


                }

            }
        });

    }

    public void show(){
        tree.setBackground(new java.awt.Color(89, 94, 98));
        frame.getContentPane().setBackground(Color.WHITE/*new java.awt.Color(89, 94, 98)*/);
        frame.setBounds(100,100,1000,650);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    public static void main(String[] args) {
        Readers r = new Readers();
        r.show();
    }
}
