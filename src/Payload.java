import java.io.Serializable;

public class Payload implements Serializable {
//    public static final long serialVersionUID;
    private byte[] payload;

    public Payload(){}

    public Payload(byte[] payload){
        super();
        this.setPayload(payload);
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public byte[] getPayload() {
        return payload;
    }
}


