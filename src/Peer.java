import java.net.Inet4Address;

public class Peer {

    private int Id;
    private String IP;
    private int port;
    private boolean hasFile;
    private boolean[] bitField;

    public Peer(String peerId, String peerIP, String peerPort, boolean hasFile){

        this.Id = Integer.parseInt(peerId);
        this.IP = peerIP;
        this.port = Integer.parseInt(peerPort);
        this.hasFile = hasFile;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isHasFile() {
        return hasFile;
    }

    public void setHasFile(boolean hasFile) {
        this.hasFile = hasFile;
    }

    public boolean[] getBitField() {
        return bitField;
    }

    public void setBitField(boolean[] bitField) {
        this.bitField = bitField;
    }
}
