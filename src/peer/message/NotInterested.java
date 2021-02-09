package peer.message;

import java.nio.ByteBuffer;

public class NotInterested {

	public byte[] not_interested = new byte[5];
	private byte[] message_length = new byte[4];
	private byte type = 3;
	
	public NotInterested() {
		message_length = ByteBuffer.allocate(4).putInt(0).array();
		
		int i = 0;
		for (i = 0; i < message_length.length; i++) {
			not_interested[i] = message_length[i];
		}
		
		not_interested[i] = type;
		
	}
	
}
