package Process;

import java.net.Socket;
import java.util.ListIterator;
import java.util.Random;

import peer.HasCompleteFile;
import peer.MessageBody;
import peer.Peer;
import peer.PeerProcess;
import peer.message.BitField;
import peer.message.Interested;
import peer.message.NotInterested;
import peer.message.Request;
import fileManager.LogWritter;
import fileManager.MergeFile;

public class RequestPiece extends Thread {

	private int peer_ID;
	private int myPeer_ID;
	private int totalpieces;
	private boolean haveAllPieces;
	private long fileSize; 
	private long pieceSize;
	private int flag = 0;
	Socket socket;
	
	public RequestPiece(int peer_ID, int totalPieces, boolean haveAllPieces, long fileSize, long pieceSize) {
		this.peer_ID = peer_ID;
		this.totalpieces = totalPieces;
		this.haveAllPieces = haveAllPieces;
		this.fileSize = fileSize;
		this.pieceSize = pieceSize;
	}
	

	@Override
	public void run() {
		
		if(haveAllPieces == false) {
			
			Peer p = null;
			byte[] field;
			int getPiece;
			
			synchronized(PeerProcess.peers) {
				
				ListIterator<Peer> it = PeerProcess.peers.listIterator();
				
				while(it.hasNext()) {
					p = (Peer)it.next();
					
					if(p.getPeerID() == peer_ID) {
						myPeer_ID = p.getmyPeerID();
						socket = p.getSocket();
						break;
					}
				}

			}

			while(true) {
				
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					System.err.println(e);
				}
				
				boolean completeFile = hasCompleteFile();
				
				if(completeFile) {
				
					if(!LogWritter.fileFlag) {
						LogWritter.fileFlag = true;
												
						System.out.println("Download complete");
						LogWritter.downloadComplete();
						
						MergeFile assemble = new MergeFile();
						assemble.reassemble(totalpieces, myPeer_ID, fileSize, pieceSize);
						
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							System.err.println(e);
						}
					}
					
					break;
				}
		
				else {
					
					if(p.isInterested()) {
						field = p.getBitfield();
						getPiece = getPieceInfo(field, BitField.bitfield);
						if(getPiece == 0) {
							p.setInterested(false);
							NotInterested not = new NotInterested();
							synchronized (PeerProcess.messageBody) {
								MessageBody m = new MessageBody();
								m.setSocket(socket);
								m.setMessage(not.not_interested);
								PeerProcess.messageBody.add(m);
							}
							flag = 1;
						}
						
						else {
							Request req = new Request(getPiece);
							synchronized (PeerProcess.messageBody) {
								MessageBody m = new MessageBody();
								m.setSocket(socket);
								m.setMessage(req.request);
								PeerProcess.messageBody.add(m);
							}
						}
					}
					
					else {
						
						field = p.getBitfield();
						getPiece = getPieceInfo(field, BitField.bitfield);
						
						if(getPiece == 0) {
							if(flag == 0) {
								NotInterested not = new NotInterested();
								
								synchronized (PeerProcess.messageBody) {
									MessageBody m = new MessageBody();
									m.setSocket(socket);
									m.setMessage(not.not_interested);
									PeerProcess.messageBody.add(m);
								}
							}
						}
						
						else {
							p.setInterested(true);
							flag = 0;
							
							Interested interested = new Interested();
							
							synchronized (PeerProcess.messageBody) {
								MessageBody m = new MessageBody();
								m.setSocket(socket);
								m.setMessage(interested.interested);
								PeerProcess.messageBody.add(m);
							}
							Request req = new Request(getPiece);
							synchronized (PeerProcess.messageBody) {
								MessageBody m = new MessageBody();
								m.setSocket(socket);
								m.setMessage(req.request);
								PeerProcess.messageBody.add(m);
							}
						}
						
					}
				}
			}
		}
		
		byte[] downLoadedCompleteFile = new byte[5];
		
		for (int i = 0; i < downLoadedCompleteFile.length - 1; i++) {
			downLoadedCompleteFile[i] = 0;
		}
		downLoadedCompleteFile[4] = 8;
		
		sendHasDownloadedCompleteFile(downLoadedCompleteFile);
	
		while(true) {
			boolean check = checkAllPeerFileDownloaded();
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.err.println(e);
			}
			
			if(check == true && PeerProcess.messageBody.isEmpty())
				break;
		}
		
		if(!LogWritter.fileCompleteFlag)
		{
			LogWritter.fileCompleteFlag = true;
		
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println(e);
			}
				
			LogWritter.closeLogger();
		}
	
		System.exit(0);
		
	}


	private boolean checkAllPeerFileDownloaded() {
		
		boolean flagAll = true;
		
		ListIterator<HasCompleteFile> it = PeerProcess.hasDownloadedCompleteFile.listIterator();
		
		while(it.hasNext()) {
		
			HasCompleteFile peer = (HasCompleteFile)it.next();
			if(peer.isHasDownLoadedCompleteFile()) {
				flagAll = false;
				break;
			}
		}
		
		return flagAll;
	}


	private void sendHasDownloadedCompleteFile(byte[] downLoadedCompleteFile) {
		
		ListIterator<HasCompleteFile> it = PeerProcess.hasDownloadedCompleteFile.listIterator();
		
		while(it.hasNext()) {
		
			HasCompleteFile peer = (HasCompleteFile)it.next();
			
			synchronized (PeerProcess.messageBody) {
				MessageBody m = new MessageBody();
				m.setSocket(peer.getSocket());
				m.setMessage(downLoadedCompleteFile);
				PeerProcess.messageBody.add(m);
			}
		}
		
	}


	private boolean hasCompleteFile() {
		
		int flagComp = 1;
		
		byte[] field = BitField.bitfield;
		
		for (int i = 5; i < field.length - 1; i++) {
			if(field[i] != -1) {
				flagComp = 0;
				break;
			}
		}
		
		if(flagComp == 1) {
			
			int remaining = totalpieces % 8;
			int a = field[field.length - 1];
			String a1 = Integer.toBinaryString(a & 255 | 256).substring(1);
			char[] a2 = a1.toCharArray();
			int[] a3 = new int[8];
			
			for (int j = 0; j < a2.length; j++) {
				a3[j] = a2[j] - 48;
			}
			
			for (int j = 0; j < remaining; j++) {
				if(a3[j] == 0) {
					flagComp = 0;
					break;
				}
			}
		}

		return (flagComp == 1);
	}




	private int getPieceInfo(byte[] field, byte[] bitfield) {
		
		int[] temp = new int[totalpieces];
		int k = 0;
		int total_missing_pieces = 0;
		int remaining  = totalpieces % 8;

		for (int i = 5; i < bitfield.length; i++) {
			
			int a = bitfield[i];
			int b = field[i];
			
			String a1 = Integer.toBinaryString(a & 255 | 256).substring(1);
			char[] a2 = a1.toCharArray();
			int[] a3 = new int[8];
					
			for (int j = 0; j < a2.length; j++) {
				a3[j] = a2[j] - 48;
			}
			
			String b1 = Integer.toBinaryString(b & 255 | 256).substring(1);
			char[] b2 = b1.toCharArray();
			int[] b3 = new int[8];
					
			for (int j = 0; j < b2.length; j++) {
				b3[j] = b2[j] - 48;
			}				
			

			if(i < bitfield.length - 1) {
				
				for (int j = 0; j < b3.length; j++) {
					if(a3[j] == 0 && b3[j] == 1) {
						temp[k] = 0;
						k++;
						total_missing_pieces++;
					}
					
					if(a3[j] == 0 && b3[j] == 0) {
						temp[k] = 1;
						k++;
					}
					
					if(a3[j] == 1) {
						temp[k] = 1;
						k++;
					}
				}
			}
			
			else {
				for (int j = 0; j < remaining; j++) {
					if(a3[j] == 0 && b3[j] == 1) {
						temp[k] = 0;
						k++;
						total_missing_pieces++;
					}
					
					if(a3[j] == 0 && b3[j] == 0) {
						temp[k] = 1;
						k++;
					}
					
					if(a3[j] == 1) {
						temp[k] = 1;
						k++;
					}
				}
			}
			
		}

		
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			System.err.println(e);
		}
		

		if(total_missing_pieces == 0)
			return 0;
		
		int[] selectFrom = new int[total_missing_pieces];
		
		int x = 0;
		for (int l = 0; l < temp.length; l++) {
			if(temp[l] == 0) {
				selectFrom[x] = l;
				x++;
			}
		}

		int index = select_random_piece(total_missing_pieces);
		int piece = selectFrom[index];
		

		return (piece + 1);
	}

	private int select_random_piece(int total_missing_pieces) {
		
		Random rand = new Random();
	    int randomNum = rand.nextInt(total_missing_pieces);

	    return randomNum;
	    
	}
		
}
