package peer;

import java.net.Socket;


public class Peer {
	
	private int myPeerID;
	private int PeerID;
	private Socket socket;
	private byte[] bitfield;
	private boolean interested;
	
	public int getPeerID() {
		return PeerID;
	}
	
	public void setPeerID(int PeerID) {
		this.PeerID = PeerID;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public byte[] getBitfield() {
		return bitfield;
	}

	public void setBitfield(byte[] bitfield) {
		this.bitfield = bitfield;
	}

	public boolean isInterested() {
		return interested;
	}

	public void setInterested(boolean interested) {
		this.interested = interested;
	}

	public int getmyPeerID() {
		return myPeerID;
	}

	public void setmyPeerID(int myPeerID) {
		this.myPeerID = myPeerID;
	}
	
}
