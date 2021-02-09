package peer.message;

import java.nio.ByteBuffer;

public class Choke {
	
	public byte[] choke = new byte[5];
	private byte[] message_length = new byte[4];
	private byte type = 0;
	
	public Choke() {
		message_length = ByteBuffer.allocate(4).putInt(0).array();
		
		int i = 0;
		for (i = 0; i < message_length.length; i++) {
			choke[i] = message_length[i];
		}
		
		choke[i] = type;
	
	}
		
}
