package fileManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.LinkedList;

public class LogWritter {

	private static int myPeerID;
	private static File file;
	private static BufferedWriter out;
	private static int numberOfPieces = 0;
	public static boolean fileFlag = false;
	public static boolean fileCompleteFlag = false;
	public static LinkedList<Integer> fileWriteOperation = new LinkedList<Integer>();
	
	public static void startLogger(int PeerID) {
		
		myPeerID = PeerID;
		String fileName = (new File(System.getProperty("user.dir")).getParent() + "/log_peer_" + myPeerID + ".log");

		file = new File(fileName);
		
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		} catch (FileNotFoundException e) {
			System.err.println(e);
		}
	}
	 
	public static void makeTCPConnection(int PeerID) {
		
		try {
			String date = new Date().toString();
			String s = date + " : Peer " + myPeerID + " makes a connection to Peer " + PeerID + ".";
			out.append(s);
			out.newLine();
			out.newLine();
			out.flush();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public static void madeTCPConnection(int PeerID) {
		
		try {
			String date = new Date().toString();
			String s = date + " : Peer " + myPeerID + " is connected from Peer " + PeerID + ".";
			out.append(s);
			out.newLine();
			out.newLine();
			out.flush();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public static void receiveHave(int PeerID, int pieceIndex) {
		
		try {
			String date = new Date().toString();
			String s = date + " : Peer " + myPeerID + " received the 'have' message from Peer " + PeerID + " for the piece " + pieceIndex + ".";
			out.append(s);
			out.newLine();
			out.newLine();
			out.flush();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public static void receiveInterested(int PeerID) {
		
		try {
			String date = new Date().toString();
			String s = date + " : Peer " + myPeerID + " received the 'interested' message from Peer " + PeerID + ".";
			out.append(s);
			out.newLine();
			out.newLine();
			out.flush();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public static void receiveNotInterested(int PeerID) {
		
		try {
			String date = new Date().toString();
			String s = date + " : Peer " + myPeerID + " received the 'not interested' message from Peer " + PeerID + ".";
			out.append(s);
			out.newLine();
			out.newLine();
			out.flush();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public static void downloadPiece(int PeerID, int pieceIndex) {
		
		numberOfPieces++;
		try {
			String date = new Date().toString();
			String s = date + " : Peer " + myPeerID + " has downloaded the piece " + pieceIndex +" from Peer " + PeerID + ".";
			out.append(s);
			out.newLine();
			s = "Now  the number of pieces it has is " + numberOfPieces;
			out.append(s);
			out.newLine();
			out.newLine();
			out.flush();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public static void downloadComplete() {
		
		if(fileFlag == true) {
			
			try {
				String date = new Date().toString();
				String s = date + " : Peer " + myPeerID + " has downloaded the complete file.";
				out.append(s);
				out.newLine();
				out.newLine();
				out.flush();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}
	
	public static void closeLogger() {
		try {
			out.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}
