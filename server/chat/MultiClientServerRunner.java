package chat;

import chat.SocketServer;

public class MultiClientServerRunner {
    public static void main(String[] args) {
        new SocketServer().start();
    }
}
