import java.util.*;

public class PeerManager implements Runnable{
    private long unchokeIntervalTimeout;
    private long optimisticUnchokingInterval;
    private Vector<Peer> interestedPeers; //requires piece information
    private PeerProcess peerProc;
    public HashSet<Peer> currentNeighbors; //Current P2P connections
    public HashSet<Peer> preferredNeighbors; //New neighbors
    public HashSet<Peer> unchokePool;
    public Peer optimisticallyUnchokedPeer;

    public PeerManager(long unchokeIntervalTimeout, long optimisticUnchokingInterval, Vector<Peer> interestedPeers, PeerProcess peerProc) {
        this.unchokeIntervalTimeout = unchokeIntervalTimeout;
        this.optimisticUnchokingInterval = optimisticUnchokingInterval;
        this.interestedPeers = interestedPeers;
        this.peerProc = peerProc;
    }


    @Override
    public void run() {
        findPreferredNeighbors(this.getPeerProc(), this.getUnchokeIntervalTimeout());
        findOptimisticallyUnchokedPeer();
    }

    private void findPreferredNeighbors(PeerProcess peerProc, long unchokeIntervalTimeout) {

    }

    private void findOptimisticallyUnchokedPeer(){
        new Thread(){
            public void run(){
                for(;!peerProc.isExit();){
                    try {
                        Thread.sleep(CommonConfig.optimisticUnchokingInterval() * 1000);
                        Iterator<Peer> iter = peerProc.peerSocketHashMap().keySet().iterator();
                        while(iter.hasNext())
                        {
                            Peer p = iter.next();
                            // if else checking condition here after adding piece information
                            interestedPeers.addElement(p);
                        }
                        if(interestedPeers.size() > 0){
                            Random randNum = new Random();
                            if(optimisticallyUnchokedPeer != null){
                                //checking if the peer is preferred
                                if(currentNeighbors!= null && !currentNeighbors.contains(optimisticallyUnchokedPeer)){
                                    //send choke message to previous neighbor peer
                                    peerProc.sendChokeMessage(optimisticallyUnchokedPeer);

                                }
                            }
                            optimisticallyUnchokedPeer = interestedPeers.get(randNum.nextInt(interestedPeers.size()));
                            peerProc.sendUnchokeMessage(new HashSet<>(Arrays.asList(optimisticallyUnchokedPeer)));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        peerProc.setExit(true);
                        break;
                    }
                }
            }
        }.start();
    }

    private void findPreferredNeighbors(PeerProcess peerProc, long unchokeIntervalTimeout){
        new Thread(){
            public void run(){
                for(;!peerProc.isExit();){
                    try{
                        Thread.sleep(CommonConfig.getUnchokingInterval()*1000);
                        if(peerProc.getNeighbor().size() > 0)
                        {
                            preferredNeighbors = new HashSet<Peer>();
                            if(peerProc.getUnchokeDownloadRateQueue().size() ==0){ //occurs in the first pass
                                //initialize preferred neighbors list with random peers
                                Random randP = new Random();
                                int idx = 0;
                                while(idx < peerProc.getNeighbor().size()
                                && preferredNeighbors.size()< CommonConfig.getNumberOfPreferredNeighbors()) {
                                    Peer peer = peerProc.getNeighbor().get(randP.nextInt(peerProc.getNeighbor().size()));
                                    if(/*peer.isInterestedInPieces()*/ true) // update after adding piece information
                                    {
                                        preferredNeighbors.add(peer);
                                    }
                                    idx++;
                                }
                                else{
                                    // if pass is not first
                                    Random randP = new Random();
                                    int idx = 0;
                                    while(idx < CommonConfig.getNumberOfPreferredNeighbors())
                                    {
                                        if(peerProc.getUnchokeDownloadRateQueue().isEmpty())
                                            preferredNeighbors.add(peerProc.getUnchokeDownloadRateQueue().poll().getPeer());
                                    }
                                    idx++;
                                }

                                int idxA = 0;
                                while (idxA < peerProc.getNeighbor().get(randP.nextInt(peerProc.getNeighbor().size())));
                                if(true /*peer.getInterestedPieces()*/) //with piece information
                                {
                                    preferredNeighbors.add(peer); // only adds new neighbors
                                }
                                idxA++;
                            }
                        }
                        if(preferredNeighbors.size() > 0)
                        {
                            //send unchoke to new neighbors
                            unchokePool = new HashSet<>();
                        }
                        if(currentNeighbors== null){
                            currentNeighbors = new HashSet<>();
                        }
                        Iterator<Peer> iter = preferredNeighbors.iterator();
                        while(iter.hasNext())
                        {
                            Peer peer = iter.next();
                            if(!currentNeighbors.contains(peer))
                            {
                                unchokePool.add(peer);
                            }
                        }
                        unchokePool.removeAll(currentNeighbors);
                        currentNeighbors.removeAll(preferredNeighbors);
                        peerProc.sendChokeMessage(currentNeighbors); // choke all not in preferred neighbors
                        peerProc.sendUnchokeMessage(new HashSet<>(unchokePool));

                        currentNeighbors = preferredNeighbors;

                        //Make log entry

                    }
                    catch (Exception e){
                        e.printStackTrace(e);
                        peerProc.setExit(true);
                        break;
                    }
                }
            }
        }.start();
    }

    public void setPeerProc(PeerProcess peerProc) {
        this.peerProc = peerProc;
    }

    public PeerProcess getPeerProc() {
        return peerProc;
    }

    public void setUnchokeIntervalTimeout(long unchokeIntervalTimeout) {
        this.unchokeIntervalTimeout = unchokeIntervalTimeout;
    }

    public long getUnchokeIntervalTimeout(){
        return this.unchokeIntervalTimeout;
    }

    public void setOptimisticUnchokingInterval(long optimisticUnchokingInterval) {
        this.optimisticUnchokingInterval = optimisticUnchokingInterval;
    }

    public long getOptimisticUnchokingInterval() {
        return optimisticUnchokingInterval;
    }

    public void setInterestedPeers(Vector<Peer> interestedPeers) {
        this.interestedPeers = interestedPeers;
    }

    public Vector<Peer> getInterestedPeers() {
        return interestedPeers;
    }
}
