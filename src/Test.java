import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class Test {

    public static void main(String[] args) throws IOException {
        PeerProcess p1 = new PeerProcess("1001");

        p1.beginRun();
        System.out.println("Runnable");

    }
}
