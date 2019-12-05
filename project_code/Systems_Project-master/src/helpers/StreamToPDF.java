package helpers;

import java.io.*;

public class StreamToPDF {

    public static void saveToPDF(byte[] input, String filename) throws IOException {
        File someFile = new File(filename +".pdf");
        FileOutputStream fos = new FileOutputStream(someFile);

        fos.write(input);
        fos.flush();
        fos.close();
    }

    public static byte[] streamToBytes(InputStream input) throws IOException {
        byte[] buffer = new byte[input.available()];
        input.read(buffer);
        input.close();

        return buffer;
    }
}
