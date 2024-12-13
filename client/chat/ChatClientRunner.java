package chat;

import java.io.IOException;

public class ChatClientRunner {
    public static void main(String[] args) throws IOException {
        new SocketClient().start();
    }
}
