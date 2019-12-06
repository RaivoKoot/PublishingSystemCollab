import models.Journal;

import javax.swing.*;
import java.util.ArrayList;

public class ArticleTreeDisplay extends JFrame{
    private JPanel TreeShow;
    public JTextField title_field;
    public JTextArea summary_field;

    public ArticleTreeDisplay(){
        add(TreeShow);
        setTitle("Article Info View");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        summary_field.setLineWrap(true);
    }
}
