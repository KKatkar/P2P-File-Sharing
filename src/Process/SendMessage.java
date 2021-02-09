package Process;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import peer.MessageBody;
import peer.PeerProcess;

public class SendMessage extends Thread {

	@Override
	public void run() {
		
		while(true) {
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.err.println(e);
			}

			if(!PeerProcess.messageBody.isEmpty()) {
				
				synchronized (PeerProcess.messageBody) {
					MessageBody m = PeerProcess.messageBody.poll();
					Socket socket = m.getSocket();
					byte[] message = m.getMessage();
					sendMessage(socket, message);
				}
			}
		}
	}
	public void sendMessage(Socket socket, byte[] message) {
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			synchronized (socket) {
				out.writeObject(message);
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
