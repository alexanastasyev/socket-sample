package chat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Logger {
    public static void logInfo(String message) {
        System.out.printf("%s: %s%n", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), message);
    }

    public static void logError(String message) {
        System.err.printf("%s: %s%n", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), message);
    }
}
