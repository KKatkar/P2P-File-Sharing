import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MessageWriter {

    private Handshake message;
    private DataOutputStream outputStream;

    public MessageWriter(Handshake handshake, DataOutputStream outStream) {

        this.message = handshake;
        this.outputStream = outStream;
    }

    public void write() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.out.println("Sending Message : " + this.getMessage());
        Handshake handShakeMessage = this.getMessage();
        bos.write(handShakeMessage.getHeader(), 0, handShakeMessage.getHeader().length);
        bos.write(handShakeMessage.getZeroBits(), 0, handShakeMessage.getZeroBits().length);
        bos.write(handShakeMessage.getPeerID(), 0, handShakeMessage.getPeerID().length);
        bos.flush();
    }

    public Handshake getMessage() {
        return message;
    }

    public void setMessage(Handshake message) {
        this.message = message;
    }
}
