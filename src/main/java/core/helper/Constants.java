package core.helper;

import java.io.File;
import java.math.BigDecimal;
import java.util.Hashtable;

import static core.helper.BrowserType.CHROME;

public class Constants {
    public static final Double MEGABYTE = (9.537)*Math.pow(10,-7);
    private static Constants instance = null;

    private Constants() {
        // Do nothing
    }

    public static Constants getInstance() {
        if (instance == null) {
            instance = new Constants();
        }
        return instance;
    }

    public static final int DRAG_CONTROL_BAR_TIMEOUT = 20;

    // parameter keys

    public static final String BROWSER_NAME_PARAMETER = "browserName";
    public static final String SELENIUM_SERVER_PARAMETER = "seleniumServer";
    public static final String DISABLE_SCREENSHOT_PARAMETER = "disableScreenshot";
    public static final String EXECUTION_PROFILE = "profile";

    // default value

    public static final String DEFAULT_BROWSER = CHROME;
    public static final String DEFAULT_SELENIUM_SERVER = "http://localhost:4444";
    public static final String DEFAULT_PARAMETERS_FILE = "default.properties";
    public static final String DEFAULT_EXECUTION_PROFILE_PATH = "profiles" + File.separator;
    private static final String USERDIR = "user.dir";
    private static final String RESOURCES_DIR = System.getProperty(USERDIR) + File.separator + "src" + File.separator + "test" + File.separator + "resources";
    public static final String SELENIUM_SERVER_BAT = RESOURCES_DIR + File.separator + "seleniumserver"  + File.separator + "standalone.bat";
    public static final String SELENIUM_SERVER_CLOSE_BAT = RESOURCES_DIR + File.separator + "seleniumserver"  + File.separator + "close_CMD.bat";

    public static final String DEFAULT_TESTDATA_PATH = RESOURCES_DIR + File.separator + "data" + File.separator + "TestData.json";
    public static final String DEFAULT_TESTOUTPUT_PATH = System.getProperty(USERDIR) + File.separator + "test-output" + File.separator + "snapshots";
    public static final String DEFAULT_SELENIUM_SERVER_URI = "/wd/hub";
    public static final String DEFAULT_SELENIUM_SERVER_ADMIN_URI = "/grid/admin";
    public static final String DOWNLOAD_DIRECTORY = "";
    public static final String SCREENSHOT_DIR = "";

    public static final String DEFAULT_APPIUM_SERVER_URI = "/wd/hub";
    public static final String DEFAULT_APPIUM_SERVER = "0.0.0.0";
    public static final int DEFAULT_APPIUM_PORT = 3434;

    public static final int TIMEOUT_1S = 1;
    public static final int TIMEOUT_3S = 3;
    public static final int TIMEOUT_5S = 5;
    public static final int TIMEOUT_10S = 10;
    public static final int TIMEOUT_20S = 20;
    public static final int TIMEOUT_120S = 120;

    public static final String SCREENSHOT_REPO = RESOURCES_DIR + File.separator + "screenshot" + File.separator;
}
