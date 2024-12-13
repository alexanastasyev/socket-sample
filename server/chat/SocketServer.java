package chat;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static chat.Logger.logError;
import static chat.Logger.logInfo;

public class SocketServer {
    private static final int PORT = 12345;
    private final AtomicInteger currentClientId = new AtomicInteger();
    private final Map<Integer, Closeable> activeClients = new ConcurrentHashMap<>();

    public void start() throws IOException {
        logInfo("[INFO]: Сервер запущен, ожидаем подключения...");
        new Thread(this::startServer).start();
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
        String message;
        while ((message = consoleInput.readLine()) != null) {
            if (message.equals("exit")) {
                return;
            }
            checkForKillClient(message);
        }
    }

    private void checkForKillClient(String message) throws IOException {
        if (message.startsWith("kill")) {
            int clientId = Integer.parseInt(message.split(" ")[1]);
            disconnectClient(clientId);
        }
    }

    private void disconnectClient(int clientId) throws IOException {
        activeClients.remove(clientId).close();
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                int clientId = currentClientId.getAndIncrement();

                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                activeClients.put(clientId, clientSocket);
                clientThread.start();
                logInfo("[INFO]: Клиент подключился. Client id: %s".formatted(clientId));
            }
        } catch (IOException e) {
            logError("[INFO]: Ошибка сервера: " + e.getMessage());
        }
    }
}
