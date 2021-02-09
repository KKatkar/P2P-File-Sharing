package fileManager;
import java.io.*;
import java.util.Properties;


public class CommonConfigParser {

	private int numberOfPreferredNeighbors;
    private int unchokingInterval;
    private int optimisticUnchokingInterval;
    private String fileName;
    private long fileSize;
    private long pieceSize;
    
    public void readFile() {

		String common = "/Common.cfg";

		String filename = (new File(System.getProperty("user.dir")).getParent() + common);

		Properties file = new Properties();
		
		try {
			
			file.load(new BufferedInputStream(new FileInputStream(filename)));
			numberOfPreferredNeighbors = Integer.parseInt(file.getProperty("NumberOfPreferredNeighbors"));
			unchokingInterval = Integer.parseInt(file.getProperty("UnchokingInterval"));
			optimisticUnchokingInterval = Integer.parseInt(file.getProperty("OptimisticUnchokingInterval"));
			fileName = file.getProperty("FileName");
			fileSize = Long.parseLong(file.getProperty("FileSize"));
			pieceSize = Long.parseLong(file.getProperty("PieceSize"));
			
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public int getNumberOfPreferredNeighbors() {
		return numberOfPreferredNeighbors;
	}

	public int getUnchokingInterval() {
		return unchokingInterval;
	}

	public int getOptimisticUnchokingInterval() {
		return optimisticUnchokingInterval;
	}

	public String getFileName() {
		return fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public long getPieceSize() {
		return pieceSize;
	}

}
