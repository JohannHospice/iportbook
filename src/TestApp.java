import com.iportbook.core.tools.ApplicationListener;
import com.iportbook.core.tools.net.SocketHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class TestApp extends ApplicationListener {
    private final int port;
    private SocketHandler so;
    private Scanner scan;

    class Reader extends ApplicationListener {

        @Override
        protected void onStart() throws Exception {

        }

        @Override
        protected void onLoop() throws Exception {
            so.receive();
        }

        @Override
        protected void onEnd() throws Exception {

        }
    }

    public TestApp(int port) {
        this.port = port;
    }

    @Override
    protected void onStart() throws Exception {
        this.so = new SocketHandler("localhost", port);
        this.scan = new Scanner(System.in);

        new Thread(new Reader()).start();
    }

    @Override
    protected void onLoop() throws Exception {
        so.send(scan.nextLine());
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
