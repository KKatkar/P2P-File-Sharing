package peer.message;

import java.nio.ByteBuffer;

public class Interested {

	public byte[] interested = new byte[5];
	private byte[] message_length = new byte[4];
	private byte type = 2;
	
	public Interested() {
		message_length = ByteBuffer.allocate(4).putInt(0).array();
		
		int i = 0;
		for (i = 0; i < message_length.length; i++) {
			interested[i] = message_length[i];
		}
		
		interested[i] = type;
		
	}
	
}
