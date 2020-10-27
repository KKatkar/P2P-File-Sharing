import java.io.FileNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;

public class PeerProcess implements Runnable{

    private Config config;
    private int thisID;
    private thisLogger log;
    private Neighbors[] neighbor;
    private int numberOfNeighbors;

    public PeerProcess(int ID) throws FileNotFoundException {
        this.thisID = ID;
        this.config = new Config();
        log = new thisLogger(thisID);
        this.numberOfNeighbors = config.getNumberOfPeers() - 1;
        neighbor = new Neighbors[numberOfNeighbors];
    }

    @Override
    public void run(){
    }
}
