import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class CommonConfig {

    private static int numberOfPreferredNeighbors;
    private static int unchokingInterval;
    private static int optimisticUnchokingInterval;
    private static String fileName;
    private static int fileSize;
    private static int pieceSize;
    private static int numberOfPeers = 0;
    private static int numPiece;

    public CommonConfig() throws FileNotFoundException {

    }

    public void readFile() throws FileNotFoundException{
        Scanner scan1 = new Scanner(new FileReader("common.cfg"));
        numberOfPreferredNeighbors = Integer.parseInt(scan1.nextLine().trim());
        unchokingInterval = Integer.parseInt(scan1.nextLine().trim());
        optimisticUnchokingInterval= Integer.parseInt(scan1.nextLine().trim());
        fileName = scan1.nextLine().trim();
        fileSize = Integer.parseInt(scan1.nextLine().trim());
        pieceSize = Integer.parseInt(scan1.nextLine().trim());
        scan1.close();

        if(this.fileSize % this.pieceSize == 0)
            this.numPiece = this.fileSize/this.pieceSize;
        else
            this.numPiece =  this.fileSize/this.pieceSize + 1;
        System.out.println(numPiece);
    }

    public int getNumberOfPreferredNeighbors() {
        return numberOfPreferredNeighbors;
    }

    public int getUnchokingInterval() {
        return unchokingInterval;
    }

    public int getOptimisticUnchokingInterval() {
        return optimisticUnchokingInterval;
    }

    public static void setFileName(String fileName){
        CommonConfig.fileName = fileName;
    }

    public static String getFileName() {
        return fileName;
    }


    public static int getFileSize() {
        return fileSize;
    }

    public int getPieceSize() {
        return pieceSize;
    }

    public static int getNumPiece() {
        return numPiece;
    }

}
