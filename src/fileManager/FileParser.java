package fileManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import peer.message.Piece;

public class FileParser {
	
	private int ID;
	private long pieceSize;
	private HashMap<Integer, Piece> map = new HashMap<Integer, Piece>();
	private int pieceNum = 1;
	private String fileName;
	
	public FileParser(int ID, long pieceSize, String fileName) {
		this.ID = ID;
		this.pieceSize = pieceSize;
		this.fileName = fileName;
	}
	
	public HashMap<Integer, Piece> readFile() {

		fileName = (new File(System.getProperty("user.dir")).getParent() + "/peer_" + ID + "/" + fileName);

		File f = new File(fileName);
		
		try {
			
			InputStream in = new FileInputStream(f);
			byte[] buf = new byte[(int)pieceSize];
			
			@SuppressWarnings("unused")
			int len;
			
			while((len = in.read(buf)) > 0) {
			
				map.put(pieceNum, new Piece(pieceNum, buf));
				pieceNum++;
			}
			
			in.close();
			
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
		
		return map;
	}

}
