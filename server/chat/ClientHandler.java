package chat;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static chat.Logger.logError;
import static chat.Logger.logInfo;

public class ClientHandler implements Runnable {
    private static final Set<PrintWriter> CLIENT_WRITERS = ConcurrentHashMap.newKeySet();
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            CLIENT_WRITERS.add(out);

            String message;
            while ((message = in.readLine()) != null) {
                logInfo("[CHAT]: " + message);
                if ("exit".equalsIgnoreCase(message)) {
                    break;
                }

                for (PrintWriter writer : CLIENT_WRITERS) {
                    if (writer != out) {
                        writer.println(message);
                    }
                }
                out.println("Message '%s' received".formatted(message));
            }
        } catch (IOException e) {
            logInfo("[INFO]: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            CLIENT_WRITERS.removeIf(PrintWriter::checkError);
        }
    }
}