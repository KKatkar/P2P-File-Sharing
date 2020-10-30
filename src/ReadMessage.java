import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ReadMessage {
    private Socket socket;
    private PeerProcess peerProc;
    private boolean isHandshakeDone = false;

    public ReadMessage(Socket socket, PeerProcess peerProcess){
        this.socket = socket;
        this.peerProc = peerProcess;

    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setPeerProc(PeerProcess peerProc) {
        this.peerProc = peerProc;
    }

    public Handshake read() throws Exception {
        InputStream inputStream = this.getSocket().getInputStream();

        byte[] header = new byte[18];
        try {
            inputStream.read(header, 0, 18);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        byte[] zeroBits = new byte[10];
        try {
            inputStream.read(zeroBits, 0, 10);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        byte[] peerId = new byte[4];
        try {
            inputStream.read(peerId);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return new Handshake(ByteBuffer.wrap(peerId).getInt());
    }

}
