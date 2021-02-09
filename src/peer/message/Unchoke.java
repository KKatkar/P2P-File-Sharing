package peer.message;

import java.nio.ByteBuffer;

public class Unchoke {

	public byte[] unchoke = new byte[5];
	private byte[] message_length = new byte[4];
	private byte type = 1;
	
	public Unchoke() {
		message_length = ByteBuffer.allocate(4).putInt(0).array();
		
		int i = 0;
		for (i = 0; i < message_length.length; i++) {
			unchoke[i] = message_length[i];
		}
		
		unchoke[i] = type;

	}
	
}
