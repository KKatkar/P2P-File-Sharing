import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Config {

    private final int numberOfPreferredNeighbors;
    private final int unchokingInterval;
    private final int optimisticUnchokingInterval;
    private final String fileName;
    private final int fileSize;
    private final int pieceSize;
    private int numberOfPeers = 0;
    private final int numPiece;

    private final ArrayList<Integer> peerID;
    private final ArrayList<String> hostName;
    private final ArrayList<Integer> downloadPort;
    private final ArrayList<Boolean> flag;

    public Config() throws FileNotFoundException {

        //read the common file
        Scanner scan1 = new Scanner(new FileReader("common.cfg"));
        this.numberOfPreferredNeighbors = Integer.parseInt(scan1.nextLine().trim());
        this.unchokingInterval = Integer.parseInt(scan1.nextLine().trim());
        this.optimisticUnchokingInterval= Integer.parseInt(scan1.nextLine().trim());
        this.fileName = scan1.nextLine().trim();
        this.fileSize = Integer.parseInt(scan1.nextLine().trim());
        this.pieceSize = Integer.parseInt(scan1.nextLine().trim());
        scan1.close();

        if(this.fileSize % this.pieceSize == 0)
            this.numPiece = this.fileSize/this.pieceSize;
        else
            this.numPiece =  this.fileSize/this.pieceSize + 1;

        //Read the peer info
        this.peerID = new ArrayList<>();
        this.hostName = new ArrayList<>();
        this.downloadPort = new ArrayList<>();
        this.flag = new ArrayList<>();

        Scanner scan2 = new Scanner(new FileReader("PeerInfo.cfg"));

        while(scan2.hasNextLine()){
            String line = scan2.nextLine();
            String[] split = line.split(" ");
            this.peerID.add(Integer.parseInt(split[0]));
            this.hostName.add(split[1]);
            this.downloadPort.add(Integer.parseInt(split[2]));
            this.flag.add((Integer.parseInt(split[3] )== 1)? true : false);
            this.numberOfPeers++;
        }

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

    public String getFileName() {
        return fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public int getPieceSize() {
        return pieceSize;
    }

    public int getNumPiece(){
        return numPiece;
    }

    public int getPeerId(int x){
        return peerID.get(x);
    }

    public String getHostName(int x){
        return hostName.get(x);
    }

    public int getDownloadPort(int x){
        return downloadPort.get(x);
    }

    public int getUploadPort(int x){
        return downloadPort.get(x) + 1 ;
    }

    public int getHavePort(int x){
        return downloadPort.get(x) + 2 ;
    }

    public boolean getFlag(int x){
        return flag.get(x);
    }

    public ArrayList<Integer> getIDs(){
        return peerID;
    }

    public ArrayList<String> getHostNames(){
        return hostName;
    }

    public ArrayList<Boolean> getFlags(){
        return flag;
    }

    public int getNumberOfPeers() {
        return numberOfPeers;
    }
}
