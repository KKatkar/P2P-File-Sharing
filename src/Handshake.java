import java.nio.ByteBuffer;
import java.util.Arrays;

public class Handshake{


    private static String headerContent = "P2PFILESHARINGPROJ";
    private byte[] header;
    private byte[] zeroBits;
    private byte[] peerID;
    private int peeridentity;

    public Handshake(int peerId) {

        this.setHeader(new byte[18]);
        for(int i = 0; i < getHeadercontent().length(); i++) {
            this.getHeader()[i] = (byte) (getHeadercontent().charAt(i));
        }
        this.setPeerID(new byte[4]);
        this.setPeerID(ByteBuffer.allocate(4).putInt(peerId).array());
        peeridentity = peerId;
        this.setZeroBits(new byte[10]);
        for(int i=0; i < 10; i++) {
            this.getZeroBits()[i] = (byte) 0;
        }
    }

    public byte[] getHeader() {
        return header;
    }

    public void setHeader(byte[] header) {
        this.header = header;
    }

    public byte[] getZeroBits() {
        return zeroBits;
    }

    public void setZeroBits(byte[] zeroBits) {
        this.zeroBits = zeroBits;
    }

    public byte[] getPeerID() {
        return peerID;
    }

    public void setPeerID(byte[] peerID) {
        this.peerID = peerID;
    }

    public static String getHeadercontent() {
        return headerContent;
    }

    @Override
    public String toString() {
        return "HandShake [peerID=" + peeridentity + "]";
    }
}
