import java.io.IOException;

public class MessageProcess implements Runnable{

    private PeerProcess peerProcess;

    public MessageProcess(PeerProcess peerProcess){
        super();
        this.setPeerProcess(peerProcess);
    }

    @Override
    public void run() {
        try {
            for (; !this.getPeerProcess().isEnd();) {
                for (; !this.getPeerProcess().getBlockMessages().isEmpty();) {
                    MessageWriter ms = this.getPeerProcess().getBlockMessages().take();
                    ms.write();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            this.getPeerProcess().setEnd(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PeerProcess getPeerProcess() {
        return peerProcess;
    }

    public void setPeerProcess(PeerProcess peerProcess) {
        this.peerProcess = peerProcess;
    }
}
