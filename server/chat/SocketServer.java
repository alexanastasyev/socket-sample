package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    private static final int PORT = 12345;

    public void start() {
        System.out.println("[INFO]: Сервер запущен, ожидаем подключения...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[INFO]: Клиент подключился: " + clientSocket.getInetAddress());

                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("[INFO]: Ошибка сервера: " + e.getMessage());
        }
    }
}
