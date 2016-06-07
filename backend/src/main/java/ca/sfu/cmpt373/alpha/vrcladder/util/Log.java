package ca.sfu.cmpt373.alpha.vrcladder.util;

public class Log {
    private enum LogMode {
        DEBUG, NONE
    }

    private static final LogMode LOG_MODE = LogMode.DEBUG;

    public static void info(String message) {
        if (LOG_MODE == LogMode.DEBUG) {
            System.out.println(message);
        }
    }
}
