import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PeerConfig {

    private static ArrayList<Peer> peerInfo= new ArrayList<>();
    private int totalPeers;

    public PeerConfig(){
        setTotalPeers(0);
    }

    public int getTotalPeers() {
        return totalPeers;
    }

    public void setTotalPeers(int totalPeers) {
        this.totalPeers = totalPeers;
    }

    public void readFile() throws FileNotFoundException {

        Scanner scan = new Scanner(new FileReader("PeerInfo.cfg"));
        int numPeers = 0;
        while (scan.hasNextLine()) {
            String s = scan.nextLine();
            String[] split = s.split(" ");

            final boolean peerHasFile = (split[3].trim().compareTo("1") == 0);
            peerInfo.add(new Peer(split[0].trim(), split[1].trim(),split[2].trim(), peerHasFile));
            numPeers++;
        }
        setTotalPeers(numPeers);
    }

    public void setPeers(PeerProcess peerProcess, String myID) throws FileNotFoundException {

        int bufferSize = CommonConfig.getNumPiece();
        Scanner scan = new Scanner(new FileReader("PeerInfo.cfg"));
        int numPeers = 0;
        while (scan.hasNextLine()) {
            String s = scan.nextLine();
            String[] split = s.split(" ");
            final boolean peerHasFile = (split[3].trim().compareTo("1") == 0);
            Peer new_peer = new Peer(split[0].trim(), split[1].trim(),split[2].trim(), peerHasFile);
            new_peer.setBitField(new boolean[bufferSize]);
            if(!split[0].equals(myID)){
                peerProcess.getNeighbor().add(new_peer);
                numPeers++;
            }
            else{
                peerProcess.setCurrentPeer(new_peer);
            }
        }
        setTotalPeers(numPeers);
    }

    public void FileManager(PeerProcess peerProcess, String myID) throws IOException {

        int bufferSize = CommonConfig.getNumPiece();
        String destination = "peer_" + myID + "/";
        Helper.MakeDirectory(destination);
        destination = destination + CommonConfig.getFileName();
        peerProcess.getCurrentPeer().setBitField(new boolean[bufferSize]);

        if(peerProcess.getCurrentPeer().isHasFile()){

            Helper.copyFileUsingStream(CommonConfig.getFileName(), destination);
            CommonConfig.setFileName(destination);

            for (int i = 0; i < CommonConfig.getNumPiece(); i++) {
                peerProcess.getCurrentPeer().getBitField()[i]= true;
            }
        }
        else{
            CommonConfig.setFileName(destination);
            new File(destination).delete();
            new File(destination).createNewFile();
        }

    }
}
