package core.helper;


import ch.qos.logback.classic.LoggerContext;
import com.github.sbabcoc.logback.testng.ReporterAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.File;
import java.util.Base64;

public class LogHelper {

    private static LogHelper instance = null;

    static {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        ReporterAppender reporterAppender = new ReporterAppender();
        reporterAppender.setContext(lc);
        reporterAppender.start();
    }

    private LogHelper() {
    }

    public static LogHelper getInstance() {
        if (instance == null) {
            instance = new LogHelper();
        }
        return instance;
    }

    public static Logger getLogger() {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        /*
         * stackTrace[0] is for Thread.currentThread().getStackTrace() stackTrace[1] is for this method log()
         */
        String className = stackTrace[2].getClassName();
        return LoggerFactory.getLogger(className);
    }

    public void error(String var1, Object... var2) {
        getLogger().error(var1, var2);
    }

    public void fail(String var1, Object... var2) {
        getLogger().error(var1, var2);
        Assert.fail(var1);
    }

    public void debug(String var1, Object... var2) {
        getLogger().debug(var1, var2);
    }

    public void info(String var1, Object... var2) {
        getLogger().info(var1, var2);
    }

    public void logFile(File file, String message) {
        getLogger().info("RP_MESSAGE#FILE#{}#{}", file.getAbsolutePath(), message);
    }

    public void logScreenshot(byte[] bytes, String message) {
        Logger binaryDataLogger = LoggerFactory.getLogger("binary_data_logger");
        String encode = Base64.getEncoder().encodeToString(bytes);
        binaryDataLogger.info("RP_MESSAGE#BASE64#{}#{}", encode, message);
    }
}