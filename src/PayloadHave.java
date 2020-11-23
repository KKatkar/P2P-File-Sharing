public class PayloadHave extends Payload{

//    private static final long serialVersionUID;
    private byte[] payloadHave;

    public PayloadHave(byte[] payload){
        super(payload);
        this.setPayloadHave(payload);
    }

    public void setPayloadHave(byte[] payloadHave) {
        this.payloadHave = payloadHave;
    }

    public byte[] getPayloadHave() {
        return payloadHave;
    }
}
