import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class PeerProcess{

    private Peer currentPeer;
    private int thisID;
    private thisLogger log;
    private ArrayList<Peer> neighbor;
    private int numberOfNeighbors;

    public PeerProcess() {
        this.neighbor = new ArrayList<>();
    }

    public ArrayList<Peer> getNeighbor() {
        return neighbor;
    }

    public void setNeighbor(ArrayList<Peer> neighbor) {
        this.neighbor = neighbor;
    }

    public Peer getCurrentPeer() {
        return currentPeer;
    }

    public void setCurrentPeer(Peer currentPeer) {
        this.currentPeer = currentPeer;
    }

    public static void main(String[] args) throws IOException {

        CommonConfig commonConfig = new CommonConfig();
        PeerConfig peerconfig = new PeerConfig();

        commonConfig.readFile();
        peerconfig.readFile();

        PeerProcess peerprocess = new PeerProcess();

        peerconfig.setPeers(peerprocess, "1003");

        peerconfig.FileManager(peerprocess, "1003");

        for(int i=0;i<124;i++){
            System.out.println(peerprocess.getCurrentPeer().getBitField()[i]);
        }

    }

}
