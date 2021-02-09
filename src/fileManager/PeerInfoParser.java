package fileManager;

import java.io.*;
import java.util.ArrayList;


public class PeerInfoParser {
	
	private int peerID;
	private int mypeerID;
	private String peerIP;
	private int port;
	private boolean hasCompleteFile;

	private String filename = (new File(System.getProperty("user.dir")).getParent() + "/PeerInfo.cfg");


	public PeerInfoParser(int mypeerID) {
		this.mypeerID = mypeerID;
	}
	
	public void readFile() {

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(" ");
				
				if(mypeerID == Integer.parseInt(parts[0])) {
					peerID = Integer.parseInt(parts[0]);
					peerIP = parts[1];
					port = Integer.parseInt(parts[2]);
					
					if(parts[3].equals("1"))
						hasCompleteFile = true;
					else
						hasCompleteFile = false;
				}
					
			}
			
			reader.close();
		
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	
	public ArrayList<Integer> getAllPeerID() {
		
		ArrayList<Integer> arr = new ArrayList<Integer>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(" ");
				arr.add(Integer.parseInt(parts[0]));
			}
			
			reader.close();
		
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
		
		return arr;
	}
	
	
	public ArrayList<String[]> getPeerInfo() {
		
		ArrayList<String[]> arr = new ArrayList<String[]>(); 
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(" ");
				
				if(mypeerID != Integer.parseInt(parts[0])) {
					arr.add(parts);
				}
				else
					break;
			}
			
			reader.close();
		
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
		
		return arr;
	}
	
	public int getpeerID() {
		return peerID;
	}

	public String getpeerIP() {
		return peerIP;
	}

	public int getPort() {
		return port;
	}

	public boolean isHasCompleteFile() {
		return hasCompleteFile;
	}


}
