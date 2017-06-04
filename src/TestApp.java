import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.net.DatagramSocketReceiver;
import com.iportbook.core.tools.net.DataSocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class TestApp extends ApplicationListener {
    private final int port;
    private DataSocket so;
    private Scanner scan;

    class ReaderTCP implements Runnable {
        @Override
        public void run() {
            try {
                while (so.read() != null)
                    continue;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReaderUDP implements Runnable {
        private static final int PORTUDP = 5656;
        @Override
        public void run() {
            try {
                DatagramSocketReceiver udp = new DatagramSocketReceiver(PORTUDP);
                while (true) {
                    udp.receive();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public TestApp(int port) {
        this.port = port;
    }

    @Override
    protected void onStart() throws Exception {
        this.so = new DataSocket("localhost", port);
        this.scan = new Scanner(System.in);
        new Thread(new ReaderTCP()).start();
        new Thread(new ReaderUDP()).start();
    }

    @Override
    protected void onLoop() throws Exception {
        String input = scan.nextLine();
        so.send(input.getBytes());
    }

    @Override
    protected void onEnd() throws Exception {
        scan.close();
        so.close();
    }

    void write(String newData, SocketChannel channel) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(200);
        buf.clear();
        buf.put(newData.getBytes());
        buf.flip();
        while (buf.hasRemaining()) {
            channel.write(buf);
        }
    }

    public static void main(String[] args) {
        new Thread(new TestApp(Integer.parseInt(args[0]))).start();
    }
}
