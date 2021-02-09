package peer.message;

import java.nio.ByteBuffer;

public class Request {
	
	public byte[] request = new byte[9];
	private byte[] message_length = new byte[4];
	private byte type = 6;
	private byte[] payload = new byte[4];
	
	public Request(int index) {
		message_length = ByteBuffer.allocate(4).putInt(4).array();
		payload = ByteBuffer.allocate(4).putInt(index).array();
		
		int i = 0;
		for (i = 0; i < message_length.length; i++) {
			request[i] = message_length[i];
		}
		
		request[i] = type;
		
		for (int j = 0; j < payload.length; j++) {
			i++;
			request[i] = payload[j];
		}
		
	}
	
}
