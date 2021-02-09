package Process;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ListIterator;

import fileManager.LogWritter;
import peer.Handshake;
import peer.HasCompleteFile;
import peer.Peer;
import peer.PeerProcess;
import peer.message.BitField;

public class ServerListener extends Thread {
	
	private int listenPort;
	private int myPeer_ID;
	private int totalPieces;
	private boolean haveAllPieces;
	private long fileSize;
	private long pieceSize;
	
	public ServerListener(int listenPort, int peer_ID, int totalPieces, boolean haveAllPieces, long fileSize, long pieceSize) {
		this.listenPort = listenPort;
		myPeer_ID = peer_ID;
		this.totalPieces = totalPieces;
		this.haveAllPieces = haveAllPieces;
		this.fileSize = fileSize;
		this.pieceSize = pieceSize;
	}
	
	@Override
	public void run() {
		
		try {
			@SuppressWarnings("resource")
			ServerSocket listener = new ServerSocket(listenPort);
			
			while(true) {
				Socket socket = listener.accept();				
				int peer_ID;
				
				
				//Handshake
				
				byte[] received = receiveHandShake(socket);
				
				Handshake send = new Handshake(myPeer_ID);
				sendHandShake(socket, send.handshake);
				
				byte[] temp = new byte[28];
				for(int i = 0 ; i < 28 ; i++){
					temp[i] = received[i];
				}
				
				String header = new String(temp);
				
				int j = 0;
				byte[] ID_temp = new byte[4];
				for(int i = 28 ; i < 32 ; i++) {
					ID_temp[j] = received[i];
					j++;
				}
				
				String s = new String(ID_temp);
				peer_ID = Integer.parseInt(s);

				if(header.equals("P2PFILESHARINGPROJ0000000000")) {
					
					boolean flag = false;
					ListIterator<Integer> iter = PeerProcess.allPeerID.listIterator();
					
					while(iter.hasNext()) {
						
						int num = iter.next().intValue(); 
						if(num != myPeer_ID) 
							continue;
						else
							break;
					}
					
					while(iter.hasNext()) {
						
						int num = iter.next().intValue(); 
						if(num == peer_ID) 
							flag = true;
					}
					
					
					if(flag == true) {
						
						Peer p = new Peer();
						p.setmyPeerID(myPeer_ID);
						p.setSocket(socket);
						p.setPeerID(peer_ID);
						
						//send bitfield
						sendBitfield(socket);
						
						//receive bitfield
						p.setBitfield(receiveBitfield(socket));
						p.setInterested(false);
						
						PeerProcess.peers.add(p);
						
						HasCompleteFile completeFile = new HasCompleteFile();
						completeFile.setSocket(socket);
						completeFile.setHasDownLoadedCompleteFile(false);
						
						PeerProcess.hasDownloadedCompleteFile.add(completeFile);
						
						System.out.println("Connection request from " + peer_ID);
						System.out.println();
						LogWritter.madeTCPConnection(peer_ID);
						

						SendMessage sendMessage = new SendMessage();
						sendMessage.start();
						
						RequestPiece pieceReq = new RequestPiece(peer_ID, totalPieces, haveAllPieces, fileSize, pieceSize);
						pieceReq.start();
						
						RecieveMessage messageReceiver = new RecieveMessage(socket, pieceSize);
						messageReceiver.start();
						
					}
					else {
						System.out.println("Unexpected peer connection");
					}
				}
				
			}
			
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	
	private void sendBitfield(Socket socket) {
		
		try {
		
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(BitField.bitfield);
		
		} catch (IOException e) {
			System.err.println(e);
		}
		
	}


	private byte[] receiveBitfield(Socket socket) {
		
		byte[] bitfield = null;
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			bitfield = (byte[]) in.readObject();
		} catch (IOException e) {
			System.err.println(e);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		}
		
		return bitfield;
	}

	private void sendHandShake(Socket socket, byte[] handshake) {
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(handshake);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	

	private byte[] receiveHandShake(Socket socket) {
		
		byte[] handshake = null;
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			handshake = (byte[]) in.readObject();
		} catch (IOException e) {
			System.err.println(e);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		}
		
		return handshake;
	}
	
}
