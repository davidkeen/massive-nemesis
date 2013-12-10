package com.davidkeen.test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

/**
 * Writes JDK log messages to SLF4J.
 * Adapted from: http://wiki.apache.org/myfaces/Trinidad_and_Common_Logging
 */
public class JavaLoggingToSlf4jRedirector {

    static JDKLogHandler activeHandler;

    /**
     * Activates this feature.
     */
    public static void activate() {
        try {
            java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");

            // remove old handlers
            for (Handler handler : rootLogger.getHandlers()) {
                rootLogger.removeHandler(handler);
            }

            // add our own
            activeHandler = new JDKLogHandler();
            activeHandler.setLevel(Level.ALL);
            rootLogger.addHandler(activeHandler);
            rootLogger.setLevel(Level.ALL);

            // done, let's check it right away!!!
            java.util.logging.Logger.getLogger(JavaLoggingToSlf4jRedirector.class.getName())
                .info("activated: sending JDK log messages to SLF4J");
        } catch (Exception exc) {
            org.slf4j.LoggerFactory.getLogger(JavaLoggingToSlf4jRedirector.class).error("activation failed", exc);
        }
    }

    public static void deactivate() {
        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.removeHandler(activeHandler);

        java.util.logging.Logger.getLogger(JavaLoggingToSlf4jRedirector.class.getName()).info("deactivated");
    }

    protected static class JDKLogHandler extends Handler {
        private Map<String, org.slf4j.Logger> cachedLogs = new ConcurrentHashMap<>();

        private org.slf4j.Logger getLog(String logName) {
            org.slf4j.Logger logger = cachedLogs.get(logName);
            if (logger == null) {
                logger = org.slf4j.LoggerFactory.getLogger(logName);
                cachedLogs.put(logName, logger);
            }
            return logger;
        }

        @Override
        public void publish(LogRecord record) {
            org.slf4j.Logger log = getLog(record.getLoggerName());
            String message = record.getMessage();
            Throwable exception = record.getThrown();
            Level level = record.getLevel();
            if (level == Level.SEVERE) {
                log.error(message, exception);
            } else if (level == Level.WARNING) {
                log.warn(message, exception);
            } else if (level == Level.INFO) {
                log.info(message, exception);
            } else if (level == Level.CONFIG) {
                log.debug(message, exception);
            } else {
                log.trace(message, exception);
            }
        }

        @Override
        public void flush() {
            // nothing to do
        }

        @Override
        public void close() {
            // nothing to do
        }
    }
}