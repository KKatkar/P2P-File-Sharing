package peer;

import java.net.Socket;

public class HasCompleteFile {

	private Socket socket;
	private boolean hasDownLoadedCompleteFile;
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public boolean isHasDownLoadedCompleteFile() {
		return hasDownLoadedCompleteFile;
	}
	
	public void setHasDownLoadedCompleteFile(boolean hasDownLoadedCompleteFile) {
		this.hasDownLoadedCompleteFile = hasDownLoadedCompleteFile;
	}
}
