package chat;

import java.io.IOException;

public class MultiClientServerRunner {
    public static void main(String[] args) throws IOException {
        new SocketServer().start();
    }
}
