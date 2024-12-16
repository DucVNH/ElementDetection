package core.helper;

import java.io.File;
import java.math.BigDecimal;
import java.util.Hashtable;

import static core.helper.BrowserType.CHROME;

public class Constants {
    private static Constants instance = null;

    private Constants() {
        // Do nothing
    }

    // parameter keys

    public static final String BROWSER_NAME_PARAMETER = "browserName";
    public static final String SELENIUM_SERVER_PARAMETER = "seleniumServer";
    public static final String EXECUTION_PROFILE = "profile";

    // default value

    public static final String DEFAULT_BROWSER = CHROME;
    public static final String DEFAULT_SELENIUM_SERVER = "http://localhost:4444";
    public static final String DEFAULT_PARAMETERS_FILE = "default.properties";
    public static final String DEFAULT_EXECUTION_PROFILE_PATH = "profiles" + File.separator;
    private static final String USERDIR = "user.dir";
    private static final String RESOURCES_DIR = System.getProperty(USERDIR) + File.separator + "src" + File.separator + "test" + File.separator + "resources";
    public static final String DEFAULT_TESTDATA_PATH = RESOURCES_DIR + File.separator + "data" + File.separator + "TestData.json";
}
