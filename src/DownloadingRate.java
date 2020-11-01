import java.util.Objects;

public class DownloadingRate {
    private Peer peer;
    private double downloadingRate;
    private PeerProcess peerProc;

    public DownloadingRate(PeerProcess peerProc, Peer peer, double downloadingRate){
        this.setDownloadingRate(downloadingRate);
        this.setPeer(peer);
        this.setPeerProc(peerProc);
    }

    public void setPeer(Peer peer) {
        this.peer = peer;
    }

    public Peer getPeer() {
        return peer;
    }

    public void setDownloadingRate(double downloadingRate) {
        this.downloadingRate = downloadingRate;
    }

    public double getDownloadingRate() {
        return downloadingRate;
    }

    public void setPeerProc(PeerProcess peerProc) {
        this.peerProc = peerProc;
    }

    public PeerProcess getPeerProc() {
        return peerProc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DownloadingRate that = (DownloadingRate) o;
        if (this.getPeerProc().equals(that.getPeerProc()))
            return false;
        if (this.getPeer() == null){
            if(that.getPeer()!= null)
                return false;
        } else if(!this.getPeer().equals(that.getPeer()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31*result + this.getPeerProc().hashCode();
        result = 31*result + ((this.getPeer() == null) ? 0:this.getPeerProc().hashCode());
        return result;
    }
}
