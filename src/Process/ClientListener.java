package Process;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ListIterator;

import peer.Handshake;
import peer.HasCompleteFile;
import peer.Peer;
import peer.PeerProcess;
import peer.message.BitField;
import fileManager.LogWritter;
import fileManager.PeerInfoParser;


public class ClientListener extends Thread {
	
	private String peerIP;
	private int port;
	private int myPeerID;
	private ArrayList<String[]> client = new ArrayList<String[]>();
	private int totalPieces;
	private boolean haveAllPieces;
	private long fileSize; 
	private long pieceSize;
	
	public ClientListener(int peer_ID, int totalPieces, boolean haveAllPieces, long fileSize, long pieceSize) {
		myPeerID = peer_ID;
		this.totalPieces = totalPieces;
		this.haveAllPieces = haveAllPieces;
		this.fileSize = fileSize;
		this.pieceSize = pieceSize;
	}
	
	@Override
	public void run() {
		
		PeerInfoParser peerInfo = new PeerInfoParser(myPeerID);
		client = peerInfo.getPeerInfo();
		
		ListIterator<String[]> it = client.listIterator();
		
		while(it.hasNext()) {
			String[] value = it.next();
			peerIP = value[1];
			port = Integer.parseInt(value[2]);
			
			try {
				Socket socket = new Socket(peerIP, port);
				
				//Handshake
				Handshake send = new Handshake(myPeerID);
				sendHandShake(socket, send.handshake);
				
				byte[] received = receiveHandShake(socket);
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
				int ID = Integer.parseInt(s);
				
				if(header.equals("P2PFILESHARINGPROJ0000000000")) {
					
					boolean flag = false;
					ListIterator<Integer> iter = PeerProcess.allPeerID.listIterator();
					
					while(iter.hasNext()) {
						
						int num = iter.next().intValue(); 
						if(num != myPeerID) {
							if(num == ID) {
								flag = true;
								break;
							}
						}
					}
					
					if(flag) {
						
						Peer p = new Peer();
						p.setmyPeerID(myPeerID);
						p.setSocket(socket);
						p.setPeerID(Integer.parseInt(value[0]));
						
						//receive bitfield
						byte[] field = receiveBitfield(socket);
						p.setBitfield(field);
						
						//send bitfield
						sendBitfield(socket);
						p.setInterested(false);
						
						synchronized (PeerProcess.peers) {
							PeerProcess.peers.add(p);
							try {
								Thread.sleep(1);
							} catch (InterruptedException e) {
								System.err.println(e);
							}
						}
						
						HasCompleteFile completeFile = new HasCompleteFile();
						completeFile.setSocket(socket);
						completeFile.setHasDownLoadedCompleteFile(false);
						
						PeerProcess.hasDownloadedCompleteFile.add(completeFile);
						
						System.out.println("Connection request sent to " + Integer.parseInt(value[0]));
						System.out.println();
						LogWritter.makeTCPConnection(Integer.parseInt(value[0]));
						
						SendMessage sendMessage = new SendMessage();
						sendMessage.start();

						RequestPiece pieceReq = new RequestPiece(Integer.parseInt(value[0]), totalPieces, haveAllPieces, fileSize, pieceSize);
						pieceReq.start();
						
						RecieveMessage messageReceiver = new RecieveMessage(socket, pieceSize);
						messageReceiver.start();

					}
					else {
						System.out.println("Unexpected peer connection");
					}
				}
				
				
			}  
			catch (UnknownHostException e) {
				System.err.println(e);
			} catch (IOException e) {
				System.err.println(e);
			}
			
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
