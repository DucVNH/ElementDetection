package core.helper;
import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static core.helper.Constants.SCREENSHOT_REPO;

public class ScreenshotUtils {

    private ScreenshotUtils() {
    }

    private static final LogHelper logger = LogHelper.getInstance();

    public static final String JS_RETRIEVE_DEVICE_PIXEL_RATIO = "var pr = window.devicePixelRatio; if (pr != undefined && pr != null)return pr; else return 1.0;";

    public static File makeFullScreenshot(WebDriver driver) throws IOException {
        // scroll up first
        scrollVerticallyTo(driver, 0);
        byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        int capturedWidth = image.getWidth();
        int capturedHeight = image.getHeight();

        double devicePixelRatio = getDevicePixelRatio(driver);

        int scrollHeight = getScrollHeight(driver);

        File file = File.createTempFile("screenshot", ".png");

        int adaptedCapturedHeight = (int) (((double) capturedHeight) / devicePixelRatio);

        BufferedImage resultingImage;

        if (Math.abs(adaptedCapturedHeight - scrollHeight) > 40) {

            int times = scrollHeight / adaptedCapturedHeight;
            int leftover = scrollHeight % adaptedCapturedHeight;

            final BufferedImage tiledImage = new BufferedImage(capturedWidth, (int) (((double) scrollHeight) * devicePixelRatio), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2dTile = tiledImage.createGraphics();
            g2dTile.drawImage(image, 0, 0, null);


            int scroll = 0;
            for (int i = 0; i < times - 1; i++) {
                scroll += adaptedCapturedHeight;
                scrollVerticallyTo(driver, scroll);
                BufferedImage nextImage = ImageIO.read(new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
                g2dTile.drawImage(nextImage, 0, (i + 1) * capturedHeight, null);
            }
            if (leftover > 0) {
                scroll += adaptedCapturedHeight;
                scrollVerticallyTo(driver, scroll);
                BufferedImage nextImage = ImageIO.read(new ByteArrayInputStream(((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES)));
                BufferedImage lastPart = nextImage.getSubimage(0,
                        nextImage.getHeight() - (int) (((double) leftover) * devicePixelRatio),
                        nextImage.getWidth(),
                        leftover);
                g2dTile.drawImage(lastPart, 0, times * capturedHeight, null);
            }
            scrollVerticallyTo(driver, 0);

            resultingImage = tiledImage;
        } else {
            resultingImage = image;
        }

        resultingImage = resizeScreenshotIfNeeded(driver, resultingImage);

        ImageIO.write(resultingImage, "png", file);
        return file;
    }

    private static void scrollVerticallyTo(WebDriver driver, int scroll) {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, " + scroll + ");");
        try {
            waitUntilItIsScrolledToPosition(driver, scroll);
        } catch (InterruptedException | ScriptTimeoutException e) {
            logger.info("Interrupt error during scrolling occurred. " + e);
            Thread.currentThread().interrupt();
        }
    }

    private static void waitUntilItIsScrolledToPosition(WebDriver driver, int scrollPosition) throws InterruptedException {
        Thread.sleep(200);
        int time = 250;
        boolean isScrolledToPosition = false;
        while (time >= 0 && !isScrolledToPosition) {
            Thread.sleep(50);
            time -= 50;
            isScrolledToPosition = Math.abs(obtainVerticalScrollPosition(driver) - scrollPosition) < 3;
        }
    }

    private static int obtainVerticalScrollPosition(WebDriver driver) {
        Long scrollLong = (Long) ((JavascriptExecutor) driver)
                .executeScript("return (window.pageYOffset !== undefined) ? window.pageYOffset : (document.documentElement || document.body.parentNode || document.body).scrollTop;");
        return scrollLong.intValue();
    }

    public static BufferedImage resizeScreenshotIfNeeded(WebDriver driver, BufferedImage screenshotImage) {
        Double devicePixelRatio = getDevicePixelRatio(driver);

        if (devicePixelRatio > 1.0 && screenshotImage.getWidth() > 0) {
            Long screenSize = getScreenSize(driver);

            Double estimatedPixelRatio = ((double) screenshotImage.getWidth()) / ((double) screenSize);

            if (estimatedPixelRatio > 1.0) {

                int newWidth = (int) (screenshotImage.getWidth() / estimatedPixelRatio);
                int newHeight = (int) (screenshotImage.getHeight() / estimatedPixelRatio);

                Image tmp = screenshotImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

                Graphics2D g2d = scaledImage.createGraphics();
                g2d.drawImage(tmp, 0, 0, null);
                g2d.dispose();

                return scaledImage;
            } else {
                return screenshotImage;
            }
        } else {
            return screenshotImage;
        }
    }

    private static Double getDevicePixelRatio(WebDriver driver) {
        return ((Number) ((JavascriptExecutor) driver).executeScript(JS_RETRIEVE_DEVICE_PIXEL_RATIO)).doubleValue();
    }

    private static int getScrollHeight(WebDriver driver) {
        long longScrollHeight = (Long) ((JavascriptExecutor) driver).executeScript("return Math.max(" +
                "document.body.scrollHeight, document.documentElement.scrollHeight," +
                "document.body.offsetHeight, document.documentElement.offsetHeight," +
                "document.body.clientHeight, document.documentElement.clientHeight);"
        );
        return (int) longScrollHeight;
    }

    private static Long getScreenSize(WebDriver driver) {
        return (Long) ((JavascriptExecutor) driver).executeScript("return Math.max(" +
                "document.body.scrollWidth, document.documentElement.scrollWidth," +
                "document.body.offsetWidth, document.documentElement.offsetWidth," +
                "document.body.clientWidth, document.documentElement.clientWidth);"
        );
    }

    public static void screenshot(WebDriver driver) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        String timestamp = LocalDateTime.parse(dtf.format(now), dtf).toString().replace(':','_').replace('-','_');

        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String imgPath = SCREENSHOT_REPO + "screenshot_" + timestamp + ".png";
        logger.info(imgPath);
        File desImg = new File(imgPath);
    }

}
