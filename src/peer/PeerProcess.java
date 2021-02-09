package peer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import peer.message.BitField;
import peer.message.Piece;
import Process.ServerListener;
import Process.ClientListener;
import fileManager.CommonConfigParser;
import fileManager.LogWritter;
import fileManager.FileParser;
import fileManager.PeerInfoParser;

public class PeerProcess {
	
	public static String fileName;
    private long fileSize;
    private long pieceSize;
    private int totalPieces;
    private int peerID;
    private int port;
    private boolean hasCompleteFile;
    public static HashMap<Integer, Piece> hm;
    public static ArrayList<Peer> peers = new ArrayList<Peer>();
    public static ArrayList<Integer> allPeerID;
    public static LinkedList<MessageBody> messageBody = new LinkedList<MessageBody>();
    public static ArrayList<HasCompleteFile> hasDownloadedCompleteFile = new ArrayList<HasCompleteFile>();

	public static void main(String[] args)  {
		
    	PeerProcess peerProc = new PeerProcess();
    	
    	CommonConfigParser commonParser = new CommonConfigParser();
		commonParser.readFile();

		fileName = commonParser.getFileName();
		peerProc.fileSize = commonParser.getFileSize();
		peerProc.pieceSize = commonParser.getPieceSize();

		peerProc.totalPieces = (int) Math.ceil((double)peerProc.fileSize/peerProc.pieceSize);
		
		PeerInfoParser peerInfo = new PeerInfoParser(Integer.parseInt(args[0]));
		peerInfo.readFile();
		allPeerID = peerInfo.getAllPeerID();
		
		peerProc.peerID = peerInfo.getpeerID();
		peerProc.port = peerInfo.getPort();
		peerProc.hasCompleteFile = peerInfo.isHasCompleteFile();

		BitField.setBitfield(peerProc.hasCompleteFile,peerProc.totalPieces);
		
		LogWritter.startLogger(peerProc.peerID);

		if(!peerProc.hasCompleteFile) {
			
			hm = new HashMap<Integer, Piece>();
			
			ServerListener peerListener = new ServerListener(peerProc.port, peerProc.peerID, peerProc.totalPieces, peerProc.hasCompleteFile, peerProc.fileSize, peerProc.pieceSize);
			peerListener.start();

			ClientListener connect = new ClientListener(peerProc.peerID, peerProc.totalPieces, peerProc.hasCompleteFile, peerProc.fileSize, peerProc.pieceSize);
			connect.start();
		}
	
		else if(peerProc.hasCompleteFile) {
			FileParser reader = new FileParser(peerProc.peerID, peerProc.pieceSize, fileName);
			hm = reader.readFile();
			
			ServerListener peerListener = new ServerListener(peerProc.port, peerProc.peerID, peerProc.totalPieces, peerProc.hasCompleteFile, peerProc.fileSize, peerProc.pieceSize);
			peerListener.start();
		}
	}
}
