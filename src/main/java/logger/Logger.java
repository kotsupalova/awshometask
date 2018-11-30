package logger;

/**
 * The wrapper class for log4j logger
 */

public class Logger {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getRootLogger();

    public static void info(String message) {
        logger.info(message);
    }

    public static void info(String message, Throwable throwable) {
        logger.info(message, throwable);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }


}
