public class PayloadBitfield extends Payload{
//    private static long serialVersionUID;
    private byte[] bitfieldPayload;

    public PayloadBitfield(byte[] payload){
        super(payload);
        this.setBitfieldPayload(payload);
    }

    public void setBitfieldPayload(byte[] bitfieldPayload) {
        this.bitfieldPayload = bitfieldPayload;
    }

    public byte[] getBitfieldPayload() {
        return bitfieldPayload;
    }
}
