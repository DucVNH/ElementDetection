package core.helper;

import org.slf4j.Logger;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class ParameterHelper {

    private ParameterHelper() {
    }

    private static final Logger logger = LogHelper.getLogger();

    private static Properties properties;

    static {
        try {
            loadParameters();
        } catch (Exception e) {
            ExceptionHelper.rethrow(e);
        }
    }
    private static void loadParameters() {
        properties = new Properties();
        String filePath = Constants.DEFAULT_EXECUTION_PROFILE_PATH + Constants.DEFAULT_PARAMETERS_FILE;
        try (InputStream inputStream = ParameterHelper.class.getClassLoader().getResourceAsStream(filePath)) {
            properties.load(inputStream);
        } catch (Exception ex) {
            logger.error("Fail to load properties from the default profile: {}", filePath, ex);
        }
        properties.putAll(System.getenv());
        properties.putAll(System.getProperties());
    }

    public static String getParameterDefaultValue(String key) {
        return properties.getProperty(key);
    }

    public static Set<String> getParameterNames() {
        return properties.stringPropertyNames();
    }

    public static Properties getProperties() {
        return properties;
    }
}