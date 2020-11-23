public class PayloadRequest extends Payload{
//    private static long final serial VersionUID;
    private byte[] requestPayload;

    public PayloadRequest(byte[] payload){
        super(payload);
        this.setRequestPayload(payload);
    }

    public void setRequestPayload(byte[] requestPayload) {
        this.requestPayload = requestPayload;
    }

    public byte[] getRequestPayload() {
        return requestPayload;
    }
}

