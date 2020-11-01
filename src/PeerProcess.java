import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.*;

public class PeerProcess{

    private Peer currentPeer;
    private String peerIDx;
    private int thisID;
    private thisLogger log;
    private ArrayList<Peer> neighbor;
    private int numberOfNeighbors, currentPeerNo;
    private PriorityQueue<DownloadingRate> unchokeDownloadRateQueue;
    private HashMap<Peer, Socket> peerSocketHashMap;
    private BlockingQueue<WriteMessage> blockMessages;
    private ServerSocket serverSocket;
    private volatile boolean end = false;
    private Future<?> messageQueueTask;

    public PeerProcess(String id){
        this.peerIDx = id;
    }

    public PeerProcess() {
        this.neighbor = new ArrayList<>();
        this.peerSocketHashMap = new HashMap<>();
        this.blockMessages = new LinkedBlockingQueue<WriteMessage>();
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
    public int getCurrentPeerNo() {
        return currentPeerNo;
    }

    public void setCurrentPeerNo(int currentPeerNo) {
        this.currentPeerNo = currentPeerNo;
    }

    public HashMap<Peer, Socket> getPeerSocketHashMap() {
        return peerSocketHashMap;
    }

    public void setPeerSocketHashMap(HashMap<Peer, Socket> peerSocketHashMap) {
        this.peerSocketHashMap = peerSocketHashMap;
    }


    public BlockingQueue<WriteMessage> getBlockMessages() {
        return blockMessages;
    }

    public void setBlockMessages(BlockingQueue<WriteMessage> blockMessages) {
        this.blockMessages = blockMessages;
    }
    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public void setConnection(PeerProcess peerProcess){
        int indexI = 0;
        System.out.println("Current peer no :" + getCurrentPeerNo());
        while (this.getCurrentPeerNo() != 0 && indexI <= this.getCurrentPeerNo() - 1) {
            Peer peer =  peerProcess.getNeighbor().get(indexI);
            Socket socket;
            try {
                socket = new Socket(peer.getIP(), peer.getPort());
                System.out.println("Peer " + this.getCurrentPeer().getId()
                        + " makes a connection to Peer " + peer.getId());
                this.getPeerSocketHashMap().put(neighbor.get(this.neighbor.indexOf(peer)), socket);
                new Thread(new ConnectionManager(this, peer, true)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            indexI++;
        }
    }
    public void createServerSocket(int portNo) throws IOException {
        ExecutorService exec = Executors.newFixedThreadPool(2);

        try {
            this.setMessageQueueTask(exec.submit(new ProcessMessage(this)));
            System.out.println("Create server Socket :" + this.currentPeer.getId());
            this.setServerSocket(new ServerSocket(portNo));

            while (!isEnd()) {
                Socket serSocket = this.getServerSocket().accept();
                Peer temp = this.getNeighbor().get(0);
                System.out.println("Neighbor port number"+temp.getPort());
                this.getPeerSocketHashMap()
                        .put(this.getNeighbor().get(this.getNeighbor().indexOf(temp)), serSocket);

                new Thread(new ConnectionManager(this, temp, false)).start();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }
        finally {
            try{
                while(!exec.isTerminated()){
                    while(!this.getBlockMessages().isEmpty()){

                    }
                    this.getMessageQueueTask().cancel(false);
                    exec.awaitTermination(1,TimeUnit.SECONDS);

                    Iterator iter =this.getPeerSocketHashMap().values().iterator();

                    while(iter.hasNext()){
                        Socket s = (Socket) iter.next();
                        if(!s.isClosed()){
                            s.close();
                        }
                    }
                }
                this.getServerSocket().close();

            }catch (Exception e){
                e.printStackTrace();
                return;
            }
        }


            }
    public void beginRun(){
        CommonConfig commonConfig = null;
        PeerConfig peerconfig = null;
        PeerProcess peerProcess = new PeerProcess();
        try {
            peerconfig = new PeerConfig();
            commonConfig = new CommonConfig();
            commonConfig.readFile();
            peerconfig.readFile();
            peerconfig.setPeers(peerProcess, peerIDx);
            peerconfig.FileManager(peerProcess, peerIDx);
            peerProcess.setConnection(peerProcess);
            peerProcess.createServerSocket(peerProcess.getCurrentPeer().getPort());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }


    public Future<?> getMessageQueueTask() {
        return messageQueueTask;
    }

    public void setMessageQueueTask(Future<?> messageQueueTask) {
        this.messageQueueTask = messageQueueTask;
    }

    //for Unchoking
    public void setUnchokeDownloadRateQueue(PriorityQueue<DownloadingRate> unchokeDownloadRateQueue){
        this.unchokeDownloadRateQueue = unchokeDownloadRateQueue;
    }

    public PriorityQueue<DownloadingRate> getUnchokeDownloadRateQueue() {
        return unchokeDownloadRateQueue;
    }
}
