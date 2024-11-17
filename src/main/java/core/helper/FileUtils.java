package core.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

    private FileUtils() {
        // Do nothing
    }

    private static final LogHelper logger = LogHelper.getInstance();
    private static final String RESOURCE_TEST_PATH = "src/test/resources";
    private static final String ENGLISH = "English";

    public static String convertRelativePathIntoAbsPath(String relativePath) {
        Path path = Paths.get(relativePath);
        logger.info("Absolute Path: " + path.toAbsolutePath());
        return String.valueOf(path.toAbsolutePath());
    }

    public static String generateFileName(String fileExtension) {
        return DateTimeHelper.getTimeStamp()
                .replace("/", "")
                .replace(":", "")
                .trim() + fileExtension;
    }


    public static <T> String getFileResource(Class<T> clazz, String filePath) {
        URL url = clazz.getClassLoader().getResource(filePath);
        assert url != null;
        Path dest = null;
        try {
            dest = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            ExceptionHelper.rethrow(e);
        }
        assert dest != null;
        return dest.toAbsolutePath().toString();
    }

    public static String getAbsolutePathTestResourceFile(String relativePath) {
        File file = new File(RESOURCE_TEST_PATH);
        String absolutePath = file.getAbsolutePath();
        return String.format("%s/%s", absolutePath, relativePath);
    }

    public static List<File> getAllFilesInDirectory(String dir) {
        List<File> files = new ArrayList<>();
        try (Stream<Path> pathStream = Files.walk(Paths.get(dir))) {
            files = pathStream.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return files;
    }

    public static void writeText(String fileName, String textToWrite, boolean isAppend) {
        try (FileOutputStream outputStream = new FileOutputStream(fileName, isAppend)) {
            byte[] strToBytes = textToWrite.getBytes();
            outputStream.write(strToBytes);
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    public static void createDirectoryIfNotExist(String directoryPath) {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                logger.info("Directory created: {}", directoryPath);
            } catch (IOException e) {
                logger.fail(e.getMessage());
            }
        }
    }

    // Return file size in MegaBytes
    public static String getFileSize(String strFilePath){
        // Get file from file name
        File file = new File(strFilePath);

        // Get length of file in bytes
        double fileSizeInBytes = file.length();
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        double fileSizeInKB = fileSizeInBytes / 1024.0;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        double fileSizeInMB = fileSizeInKB / 1024.0;

        if(fileSizeInBytes < 1024){
            return String.format("%.1f", fileSizeInBytes) + "B";
        } else if(fileSizeInKB < 1024){
            return String.format("%.1f", fileSizeInKB) + "KB";
        } else{
            return String.format("%.1f", fileSizeInMB) + "MB";
        }
    }

    public static boolean fileExists(String strFilePath){
        File file = new File(strFilePath);
        return file.exists();
    }

    public static void waitFileExist(String strFilePath, int timeout) throws InterruptedException {
        int countdown = 0;
        while(!fileExists(strFilePath)){
            Thread.sleep(1000);
            countdown += 1;
            if(countdown == timeout){
                logger.info("After waiting for " + timeout + " seconds, the file does not exist.");
                break;
            }
        }
    }

    public static String[] getListFilesInFolder(File folderPath){
        //List of all files and directories
        logger.debug("Folder path " + folderPath.getAbsolutePath());
        String contents[] =  folderPath.list();
        logger.info("List of files and directories in the specified directory:");
        for(int i=0; i<contents.length; i++) {
            contents[i] = folderPath.getAbsolutePath() + File.separator + contents[i];
            logger.info("List Expected files {}",contents[i]);
        }
        return contents;
    }
}
