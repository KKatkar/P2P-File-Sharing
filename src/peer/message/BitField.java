package peer.message;

import java.nio.ByteBuffer;


public class BitField {
	
	private static int total_pieces;
	private static boolean hasCompleteFile = false;
	private static byte[] payload; 
	private static byte[] message_length = new byte[4];
	private static byte type = 5;
	public static byte[] bitfield;
	
	
	public static void setBitfield(boolean hasFile, int num) {
		hasCompleteFile = hasFile;
		total_pieces = num;
		int payload_length = (int) Math.ceil((double)total_pieces/8);
		int remaining = total_pieces % 8;
		message_length = ByteBuffer.allocate(4).putInt(payload_length).array();
		payload = new byte[payload_length];
		bitfield = new byte[payload_length + 5];
		
		int i = 0;
		for(; i < message_length.length; i++) {
			bitfield[i] = message_length[i];
		}
		
		bitfield[i] = type;
		
		if(hasCompleteFile == false) {
			
			for (int j = 0; j < payload.length; j++) {
				i++;
				bitfield[i] = 0;
			}
		}
		
		else {
			for (int j = 0; j < payload.length - 1; j++) {
				
				i++;
				
				for(int k = 0; k < 8; k++) {
					bitfield[i] = (byte) (bitfield[i] | (1<<k));
				}				
			}
			
			i++;
			for (int j = 0; j < remaining; j++) {
				bitfield[i] = (byte) (bitfield[i] | (1<< (7 - j)));
			}
		}
			
//		for (int j = 0; j < bitfield.length; j++) {
//			System.out.print(bitfield[j]);
//			System.out.print(" ");
//		}
	}
	
	public static void updateBitField(int pieceIndex) {
		int i = (pieceIndex - 1) / 8;
		int k = 7 - ((pieceIndex - 1) % 8);
		bitfield[i + 5] = (byte) (bitfield[i + 5] | (1<<k));
	}
	
}
