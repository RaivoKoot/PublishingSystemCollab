package helpers;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class ChoosePDF {

    public static File choosePDF(){
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setFileFilter(new FileNameExtensionFilter("PDFs", "pdf"));
        jfc.setAcceptAllFileFilterUsed(false);

        JOptionPane.showMessageDialog(null,"Please choose the pdf file next.");
        int returnValue = jfc.showOpenDialog(null);

        File selectedFile;
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = jfc.getSelectedFile();
            return selectedFile;
        } else
        {
            JOptionPane.showMessageDialog(null,"Failed. Make sure you correctly chose a file");
            return null;
        }
    }
}
