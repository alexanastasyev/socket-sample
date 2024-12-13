package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import static chat.Logger.logError;
import static chat.Logger.logInfo;


public class SocketClient {
    private static final String HOST = "localhost";
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

    public void start() throws IOException {
        int port = getPortFromInput();
        connectToServer(port);
    }

    private void connectToServer(int port) throws IOException {
        try (Socket socket = new Socket(HOST, port)) {
            logInfo("[INFO]: Подключено к серверу чата!");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            startServerListening(in);
            startConsoleReading(out);
        }
    }

    private void startConsoleReading(PrintWriter out) {
        Thread readingThread = new Thread(() -> sendMessage(out));
        readingThread.start();
        try {
            readingThread.join();
        } catch (InterruptedException e) {
            logInfo("Close connection...");
        }
    }

    private void sendMessage(PrintWriter out) {
        String message;
        try {
            while ((message = consoleInput.readLine()) != null && !closed.get()) {
                if ("exit".equalsIgnoreCase(message)) {
                    break;
                }
                out.println(message);
            }
        } catch (IOException e) {
            logError("[ERROR]: Ошибка клиента: " + e.getMessage());
        }
    }

    private void startServerListening(BufferedReader in) {
        new Thread(() -> receiveMessage(in)).start();
    }

    private void receiveMessage(BufferedReader in) {
        try {
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                receiveMessage("[SERV]: %s%n".formatted(serverMessage));
            }
        } catch (Exception e) {
            logError("[INFO]: Ошибка чтения сообщений: " + e.getMessage());
        } finally {
            closed.set(true);
            try {
                System.in.close();
            } catch (IOException e) {
                logError("SYSTEM ERROR");
            }
        }
    }

    private static void receiveMessage(String serverMessage) {
        logInfo(serverMessage);
    }

    private int getPortFromInput() throws IOException {
        logInfo("[INFO] Ожидание команд...");
        int port;
        while (true) {
            String message = consoleInput.readLine();
            if (message.startsWith("start")) {
                port = Integer.parseInt(message.split(" ")[1]);
                break;
            } else if (message.startsWith("exit")) {
                throw new IOException();
            }
        }
        return port;
    }
}
