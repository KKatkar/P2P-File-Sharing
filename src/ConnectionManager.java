import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class ConnectionManager implements  Runnable{

    private PeerProcess peerProc;
    private Socket socket;
    private boolean initiateHandShake;
    private ReadMessage messageRead;
    private long startTime;
    private long endTime;
    private Peer peer;

    public ConnectionManager(PeerProcess peerProc, Peer peer, boolean initiateHandShake) throws SocketException {

        this.setPeerProc(peerProc);
        this.setSocket(peerProc.getPeerSocketHashMap().get(peer));
        System.out.println("socket read port for the server"+peerProc.getPeerSocketHashMap().get(peer));
        this.setInitiateHandShake(initiateHandShake);
        this.setMessageRead(new ReadMessage(this.getSocket(), peerProc));
        //System.out.println("Connection Manager Setmessageread : "+ this.getSocket().getPort());
//        this.getSocket().setSoLinger(true, 70);
        this.setPeer(peer);

        if(this.initiateHandShake){
            this.sendHandshake();
        }

    }

    private void sendHandshake() {
        Handshake handShake = new Handshake(peerProc.getCurrentPeer().getId());
        try {
            this.peerProc.getBlockMessages().put(new WriteMessage(handShake,
                    new DataOutputStream(socket.getOutputStream())));
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(!this.getPeerProc().isEnd()){
            try {
                startTime = System.currentTimeMillis();
                System.out.println("Read the Message"+this.getSocket());
                Handshake obj =  messageRead.read();
                endTime = System.currentTimeMillis();
                if (ByteBuffer.wrap(obj.getPeerID()).getInt() == this.peer.getId()) {
                    System.out.println("HandSHake done!!!!");
                }else
                {
                    System.out.println("Handshake peer same");
                }
            } catch (Exception e) {
                e.printStackTrace();
                peerProc.setEnd(true);
            }
        }

    }

    public PeerProcess getPeerProc() {
        return peerProc;
    }

    public void setPeerProc(PeerProcess peerProc) {
        this.peerProc = peerProc;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setInitiateHandShake(boolean initiateHandShake) {
        this.initiateHandShake = initiateHandShake;
    }

    public ReadMessage getMessageRead() {
        return messageRead;
    }

    public void setMessageRead(ReadMessage messageRead) {
        this.messageRead = messageRead;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setPeer(Peer peer) {
        this.peer = peer;
    }
}
