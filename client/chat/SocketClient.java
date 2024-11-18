package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {
    private static final String HOST = "server";
    private static final int PORT = 12345;

    public void start() {
        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("[INFO]: Подключено к серверу чата!");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.printf("[SERV]: %s%n", serverMessage);
                    }
                } catch (IOException e) {
                    System.err.println("[INFO]: Ошибка чтения сообщений: " + e.getMessage());
                }
            }).start();

            String message;
            while ((message = consoleInput.readLine()) != null) {
                out.println(message);
                if ("exit".equalsIgnoreCase(message)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("[INFO]: Ошибка клиента: " + e.getMessage());
        }
    }
}
