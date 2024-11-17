package core.helper;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageUtils {

    private ImageUtils() {
    }

    private static long difference = 0;

    private static final LogHelper logger = LogHelper.getInstance();

    public static void getImageFromBase64(String base64code, String pathFile, String fileName) throws IOException {
        //Parse the URI to get only the base64 part
        String base64 = base64code.split("base64,")[1];

        // decode into String URL from encoded format
        byte[] imageByte = Base64.getDecoder().decode(base64);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);

        BufferedImage image = ImageIO.read(bis);
        ImageIO.write(image, "png", new File(pathFile, fileName));
    }

    public static double compareTwoImage(String baseImagePath, String changeImagePath) throws IOException {
        File file = new File(changeImagePath);
        double percentage = getDiffPercentAndCreateCompareImage(baseImagePath, changeImagePath, file.getParent());
        return percentage;
    }

    public static double compareTwoImage(String baseImagePath, String changeImagePath, String resultDir) throws IOException {
        double percentage = getDiffPercentAndCreateCompareImage(baseImagePath, changeImagePath, resultDir);

        if(percentage > 0){
            File baseImage = new File(baseImagePath);
            File changeImage = new File(changeImagePath);
        logger.logFile(baseImage,"Expected image");
        logger.logFile(changeImage,"Actual image");
        }
        return percentage;
    }

    public static double compareImageToExpectedSet(String[] expectedImagePath, String actualImageSetPath, String resultDir) throws IOException {
        double[] percentage = new double[expectedImagePath.length];
        double smallest = 100.0;
        int smallestPos = expectedImagePath.length;

        for (int i = 0; i < expectedImagePath.length; i++){
            percentage[i] = getDiffPercent(expectedImagePath[i], actualImageSetPath);
            if(percentage[i] < smallest){
                smallest = percentage[i];
                smallestPos = i;
            }
        }
        if(smallest > 0){
            File baseImage = new File(expectedImagePath[smallestPos]);
            File changeImage = new File(actualImageSetPath);
            logger.logFile(baseImage,"Expected image");
            logger.logFile(changeImage,"Actual image");
        }

        return getDiffPercentAndCreateCompareImage(expectedImagePath[smallestPos], actualImageSetPath, resultDir);
    }

    private static double getDiffPercentAndCreateCompareImage(String basePath, String changePath, String resultDir) throws IOException {
        File baseImage = new File(basePath);
        File changeImage = new File(changePath);
//        logger.logFile(baseImage,"Expected image");
//        logger.logFile(changeImage,"Actual image");
        BufferedImage imgBase = ImageIO.read(baseImage);
        BufferedImage imgChange = ImageIO.read(changeImage);

        // reset value of 'different' variable before comparison
        difference = 0;

        int widthBase = imgBase.getWidth();
        int widthChange = imgChange.getWidth();
        int heightBase = imgBase.getHeight();
        int heightChange = imgChange.getHeight();

        if ((widthBase != widthChange) || (heightBase != heightChange)) {
            logger.error("Error: baseline: {} x {} - compared {} x {}", widthBase, heightBase, widthChange, heightChange);
            logger.error("Resize changeImage to baseline size");
            imgChange = resizeImage(imgChange, widthBase, heightBase);
        }

        BufferedImage imgResult = new BufferedImage(widthBase, heightBase, BufferedImage.TYPE_4BYTE_ABGR);
        compareRGBAllPixel(imgResult, imgBase, imgChange);

        // Total number of red pixels = width * height; Total number of blue pixels = width * height
        // Total number of green pixels = width * height; So total number of pixels = width * height * 3
        double totalPixels = (double) widthBase * heightBase * 3;

        // Normalizing the value of different pixel for accuracy(average pixels per color component)
        double avgDifferentPixels = (double) difference / totalPixels;

        // There are 255 values of pixels in total
        double percentage = (avgDifferentPixels / 255) * 100;

        logger.info("Difference Percentage--> {}", percentage);

        Path resultDirPath = Paths.get(resultDir);
        if (!Files.exists(resultDirPath)) {
            Files.createDirectories(resultDirPath);
        }

        File file = new File(changePath);
        int pos = file.getName().lastIndexOf('.');

        String fileName = String.format("%s_Result_%s.png", file.getName().substring(0, pos), String.format("%1$,.4f", percentage).replace(".", "_"));
        String path = resultDir + File.separator + fileName;
        createPngImage(imgResult, path);
        logger.info("Created image: {}", path);

        return percentage;
    }

    private static double getDiffPercent(String basePath, String changePath) throws IOException {
        File baseImage = new File(basePath);
        File changeImage = new File(changePath);
        BufferedImage imgBase = ImageIO.read(baseImage);
        BufferedImage imgChange = ImageIO.read(changeImage);

        // reset value of 'different' variable before comparison
        difference = 0;

        int widthBase = imgBase.getWidth();
        int widthChange = imgChange.getWidth();
        int heightBase = imgBase.getHeight();
        int heightChange = imgChange.getHeight();

        if ((widthBase != widthChange) || (heightBase != heightChange)) {
            imgChange = resizeImage(imgChange, widthBase, heightBase);
        }

        BufferedImage imgResult = new BufferedImage(widthBase, heightBase, BufferedImage.TYPE_4BYTE_ABGR);
        compareRGBAllPixel(imgResult, imgBase, imgChange);

        // Total number of red pixels = width * height; Total number of blue pixels = width * height
        // Total number of green pixels = width * height; So total number of pixels = width * height * 3
        double totalPixels = (double) widthBase * heightBase * 3;

        // Normalizing the value of different pixel for accuracy(average pixels per color component)
        double avgDifferentPixels = (double) difference / totalPixels;

        // There are 255 values of pixels in total
        double percentage = (avgDifferentPixels / 255) * 100;

        return percentage;
    }

    public static void createPngImage(BufferedImage image, String fileName) throws IOException {
        File imageFile = new File(fileName);
        ImageIO.write(image, "png", imageFile);
        logger.logFile(imageFile,"see attached image");
    }

    private static void compareRGBAllPixel(BufferedImage imgResult, BufferedImage imgBase, BufferedImage imgChange) {
        for (int y = 0; y < imgBase.getHeight(); y++) {
            for (int x = 0; x < imgBase.getWidth(); x++) {
                int rgbA = imgBase.getRGB(x, y);
                int rgbB = imgChange.getRGB(x, y);

                if (rgbA == rgbB) {
                    imgResult.setRGB(x, y, rgbB);
                } else {
                    int redA = (rgbA >> 16) & 0xff;
                    int greenA = (rgbA >> 8) & 0xff;
                    int blueA = (rgbA) & 0xff;
                    int redB = (rgbB >> 16) & 0xff;
                    int greenB = (rgbB >> 8) & 0xff;
                    int blueB = (rgbB) & 0xff;

                    difference += Math.abs(redA - redB) + Math.abs(greenA - greenB) + Math.abs(blueA - blueB);

                    int modifiedRGB = new Color(255, 0, 0).getRGB();
                    imgResult.setRGB(x, y, modifiedRGB);
                }
            }
        }
    }

    public static void convertPNGToJPG(File input, File output) {
        try {
            BufferedImage image = ImageIO.read(input);
            BufferedImage result = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
            ImageIO.write(result, "jpg", output);

        } catch (IOException e) {
            logger.fail(e.getMessage());
        }
    }

    public static void compressJPGFile(File input, File output) {
        try {
            BufferedImage image = ImageIO.read(input);
            OutputStream out = new FileOutputStream(output);

            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(out);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.5f);
            }

            writer.write(null, new IIOImage(image, null, null), param);

            out.close();
            ios.close();
            writer.dispose();
        } catch (IOException e) {
            logger.fail(e.getMessage());
        }
    }

    public static BufferedImage resizeImage(final Image image, int width, int height) {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return bufferedImage;
    }

    public static BufferedImage getImageFromClipboard() {
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                return (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
            }
            catch (UnsupportedFlavorException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            logger.error("getImageFromClipboard: That wasn't an image!");
        }
        return null;
    }
}