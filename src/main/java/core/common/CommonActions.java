package core.common;

import core.helper.LogHelper;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;

import java.io.File;
import java.util.Objects;

public class CommonActions {

    private static final Logger logger = LogHelper.getLogger();

    private CommonActions() {
        throw new IllegalStateException("CommonActions class");
    }

    public static void holdOn(int timeoutInSecond) {
        holdOnMs(timeoutInSecond * 1000L);
    }

    public static void holdOnMs(long timeoutInMs) {
        try {
            Thread.sleep(timeoutInMs);
        } catch (InterruptedException e) {
            logger.warn("Interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    public static String getExecutedBrowser(WebDriver driver) {
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        return cap.getBrowserName().toLowerCase();
    }

    public static <T> String getFileResourcePath(Class<T> clazz, String filePath) {
        return Objects.requireNonNull(clazz.getClassLoader().getResource(filePath.replace("/", File.separator))).getFile();
    }

}
