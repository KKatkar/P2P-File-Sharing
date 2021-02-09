package fileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import peer.PeerProcess;
import peer.message.Piece;


public class MergeFile {
	
	
	public void reassemble(int totalpieces, int PeerID, long fileSize, long pieceSize) {
		System.out.println("%%%%");
		String dir = (new File(System.getProperty("user.dir")).getParent() + "/peer_" + PeerID);
		File theDir = new File(dir);

		if (!theDir.exists()) {
			
			try {
				theDir.mkdir();	
			} catch(SecurityException e) {
				System.err.println(e);
			}        
		}
		
		String fileName = (dir + "/" + PeerProcess.fileName);
		
		File file = new File(fileName);
		try {
			OutputStream out = new FileOutputStream(file);
			
			for (int i = 1; i <= totalpieces - 1; i++) {
				
				Integer num = new Integer(i);
				Piece p = PeerProcess.hm.get(num);
				byte[] temp = new byte[4];
				
				for (int j = 0; j < 4; j++) {
					temp[j] = p.piece[j];
				}
				
				int size = ByteBuffer.wrap(temp).getInt();
				size = size - 4;
				
				System.out.println(i + " = " + size);
				
				byte[] buffer = new byte[size];
				
				for (int j = 0, z = 9; j < buffer.length && z < p.piece.length; j++, z++) {
					buffer[j] = p.piece[z];
				}
				
				out.write(buffer);
			}
			
			Integer num = new Integer(totalpieces);
			Piece p = PeerProcess.hm.get(num);
			
			int size = (int) (fileSize % pieceSize);
			System.out.println(size);
			
			byte[] buffer = new byte[size];
			
			for (int j = 0, z = 9; j < buffer.length && z < p.piece.length; j++, z++) {
				buffer[j] = p.piece[z];
			}
			
			out.write(buffer);
			out.close();
			
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
		
	}
}
