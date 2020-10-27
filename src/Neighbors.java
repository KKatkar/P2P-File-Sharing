import java.net.Socket;

public class Neighbors {

    private final int peerID;
    private final Socket downloadSocket;
    private final Socket uploadSocket;
    private final Socket haveSocket;
    private final int state;

    public Neighbors(int peerID, Socket socket1, Socket socket2, Socket socket3){
        this.peerID = peerID;
        this.downloadSocket = socket1;
        this.uploadSocket = socket2;
        this.haveSocket = socket3;
        this.state = 0;
    }

    public int getPeerID() {
        return peerID;
    }

    public Socket getDownloadSocket() {
        return downloadSocket;
    }

    public Socket getUploadSocket() {
        return uploadSocket;
    }

    public Socket getHaveSocket() {
        return haveSocket;
    }

    public int getState() {
        return state;
    }
}
