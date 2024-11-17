package core.helper;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

public class HttpHelper {

    private HttpHelper() {
        // Do nothing
    }

    private static final LogHelper logger = LogHelper.getInstance();

    private static final int DEFAULT_CONNECT_TIMEOUT = Integer.MAX_VALUE;

    private static final int DEFAULT_SOCKET_TIMEOUT = Integer.MAX_VALUE;

    private static final String PROTOCOL = "TLSv1.2";

    public static boolean isLinkActive(String txtUrl, String requestMethod) {
        boolean isLinkActive = false;
        try {
            HttpURLConnection httpURLConnect = (HttpURLConnection) new URL(txtUrl).openConnection();

            httpURLConnect.setConnectTimeout(60000);
            httpURLConnect.setRequestMethod(requestMethod);
            httpURLConnect.connect();

            if (httpURLConnect.getResponseCode() == HttpURLConnection.HTTP_OK) {
                isLinkActive = true;
            } else {
                logger.info(txtUrl + " is not work with " + requestMethod + httpURLConnect.getResponseCode());
                isLinkActive = false;
            }
        } catch (SocketTimeoutException ste) {
            logger.info(txtUrl + " is not work with timeout using " + requestMethod);
            isLinkActive = false;
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return isLinkActive;
    }

    public static void saveFile(String urlToDownload, String path, String cookie) {
        URLConnection urlConnection = null;
        try {
            urlConnection = new URL(urlToDownload).openConnection();
            if (!cookie.isEmpty()) {
                urlConnection.setRequestProperty("Cookie", cookie);
            }

            try (OutputStream output = new FileOutputStream(path);
                 InputStream input = new BufferedInputStream(urlConnection.getInputStream())) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(dataBuffer)) != -1) {
                    output.write(dataBuffer, 0, bytesRead);
                }
                output.flush();
                output.close();
                input.close();
                logger.info("File saved: {}", path);
            }
        } catch (IOException e) {
            logger.fail(e.getMessage());
        } finally {
            if (urlConnection instanceof HttpURLConnection) {
                ((HttpURLConnection) urlConnection).disconnect();
            }
        }
    }
}