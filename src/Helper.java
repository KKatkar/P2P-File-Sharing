import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Helper {

    public static void MakeDirectory(String directory){
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    public static void copyFileUsingStream(String fileSource, String fileDestination) throws IOException {
        FileInputStream srcStream = null;
        FileOutputStream dstStream = null;

        System.out.println(fileSource);
        try {
            srcStream = new FileInputStream(fileSource);
            dstStream = new FileOutputStream(fileDestination);
            dstStream.getChannel().transferFrom(srcStream.getChannel(), 0, srcStream.getChannel().size());
        }
        catch (IOException e) {
            System.out.println(e);
        }
        finally {
            try {
                srcStream.close();
            }
            catch (Exception e) {

            }
            try {
                dstStream.close();
            }
            catch (Exception e) {
            }
        }
    }

    public static void clearBit(byte[] b, int index) {
        byte b1 = 1;
        b[index / 8] = (byte) (b[index / 8] & (~(b1 << ((index) % 8))));
    }
}
