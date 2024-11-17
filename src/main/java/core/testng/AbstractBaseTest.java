package core.testng;

import core.helper.Constants;
import core.helper.LogHelper;
import core.helper.ParameterHelper;
import core.page.BasePage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.util.Strings;
import org.testng.xml.XmlClass;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AbstractBaseTest {
    protected static final LogHelper logger = LogHelper.getInstance();
    protected static final Properties profileProperties = ParameterHelper.getProperties();
    protected Map<String, String> parametersSuite;
    protected Map<String, String> parametersTest;
    protected static WebDriver webDriver;

    public WebDriver createDriver(Object[] args) {
        return this.webDriver;
    }

    public void getProfileProperties(ITestContext context) {
        parametersSuite = context.getSuite().getXmlSuite().getAllParameters();
        String key = Constants.EXECUTION_PROFILE;
        String value = parametersSuite.get(key);
        if (Strings.isNullOrEmpty(value)) {
            logger.info("Cannot find the {} parameter in the testsuite: {}. Getting {} parameter from the {} ......", key, context.getSuite().getName(), key, Constants.DEFAULT_PARAMETERS_FILE);
            value = profileProperties.getProperty(Constants.EXECUTION_PROFILE);
        }
        if (Strings.isNullOrEmpty(value)) {
            logger.info("Cannot find the {} parameter in the {}", key, Constants.DEFAULT_PARAMETERS_FILE);
        } else {
            logger.info("The profile name is being used: {}", value);
            String filePath = Constants.DEFAULT_EXECUTION_PROFILE_PATH + value;
            try (InputStream inputStream = ParameterHelper.class.getClassLoader().getResourceAsStream(filePath)) {
                profileProperties.load(inputStream);
            } catch (Exception ex) {
                logger.error("Fail to load properties from {}", value, ex);
            }
        }
    }

    protected JSONObject getTestData() throws IOException, ParseException {
        Reader reader = new FileReader(Constants.DEFAULT_TESTDATA_PATH);
        JSONParser parser = new JSONParser();
        JSONObject result = (JSONObject) parser.parse(reader);
        if (result == null) {
            logger.error("testData is null!!!");
        }
        return result;
    }



    @BeforeMethod
    public void beforeTest(ITestContext context, ITestResult result) {
    }

    protected void loadTestParameters(ITestContext context) {
        parametersTest = context.getCurrentXmlTest().getAllParameters();
        ParameterHelper.getParameterNames().forEach(key -> {
            String value = parametersTest.get(key);
            if (Strings.isNullOrEmpty(value)) {
                value = ParameterHelper.getParameterDefaultValue(key);
                parametersTest.put(key, value);
            }
        });
        
    }

    protected void setTestBrowser() {
        String key = Constants.BROWSER_NAME_PARAMETER;
        String value = parametersTest.get(key);
        if (Strings.isNullOrEmpty(value)) {
            value = Constants.DEFAULT_BROWSER;
            parametersTest.put(key, value);
        }
    }

    protected void setSeleniumServer() {
        String key = Constants.SELENIUM_SERVER_PARAMETER;
        String value = parametersTest.get(key);
        if (Strings.isNullOrEmpty(value)) {
            value = Constants.DEFAULT_SELENIUM_SERVER;
            parametersTest.put(key, value);
        }
    }


    // This init Page Object of a class, write a long time ago,
    // although it is a Sonar issue, changing this caused big impact
    @SuppressWarnings("squid:S3011")
    protected void initPages() throws IllegalAccessException, InstantiationException {
        List<BasePage> pages = new ArrayList<>();
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            Class<?> fieldType = field.getType();
            if (BasePage.class.isAssignableFrom(fieldType)) {
                field.setAccessible(true);
                BasePage page = (BasePage) field.get(this);
                if (page == null) {
                    try {
                        page = (BasePage) fieldType.getDeclaredConstructor().newInstance();
                    } catch (InvocationTargetException | NoSuchMethodException e) {
                        logger.fail(e.getMessage());
                    }
                    field.set(this, page);
                }
                field.setAccessible(false);
                logger.info("{}", page);
                pages.add(page);
            }
        }
        pages.forEach(page -> page.init(webDriver));
    }


    public String profile(String key) {
        return profileProperties.getProperty(key);
    }

    @AfterTest
    public synchronized void afterTest(ITestResult result) {
        webDriver.quit();
        webDriver.close();
        webDriver = null;
    }

    public void loadUrl(String url) {
        logger.info("Go to to url " + url);
        webDriver.get(url);
    }

    public void navigateToUrl(String url) {
        webDriver.navigate().to(url);
    }

    protected boolean isSuiteRunOnMobile(ITestContext context) {
        int numberOfDesktopTC = 0;
        int numberOfMobileTC = 0;
        List<XmlClass> classList = context.getCurrentXmlTest().getXmlClasses();

        for (XmlClass xmlClass : classList) {
            String classSimpleName = xmlClass.getSupportClass().getSuperclass().getSimpleName();
            if (classSimpleName.equals(("BaseTest"))) {
                numberOfDesktopTC++;
            } else {
                numberOfMobileTC++;
            }
        }
        return isAllTestCasesForMobile(numberOfDesktopTC, numberOfMobileTC);
    }

    private boolean isAllTestCasesForMobile(int numberOfDesktopTC, int numberOfMobileTC) {
        boolean result = false;
        if (numberOfDesktopTC > 0 && numberOfMobileTC > 0) {
            logger.fail("Error: The test suite has some desktop test cases and some mobile test cases! Please fix it.");
        } else if (numberOfDesktopTC > 0 && numberOfMobileTC == 0) {
            logger.info("This suite for desktop");
        } else if (numberOfDesktopTC == 0 && numberOfMobileTC > 0) {
            logger.info("This suite for mobile");
            result = true;
        } else {
            logger.fail("Error: Something wrong in test suite. Please double check!");
        }
        return result;
    }

    public WebDriver getwebDriver() {
        if (webDriver == null) {
            logger.fail("The webDriver is not instantiated yet");
        }
        return webDriver;
    }

    public void fullScreenMode() {
        logger.info("Full screen mode");
        webDriver.manage().window().fullscreen();
    }

    public void changeProfile(String profile) {
        profile = profile.toLowerCase();
        String currentProfile = profileProperties.getProperty(Constants.EXECUTION_PROFILE);
        if (!currentProfile.contains(profile)) {
            String fileName = profile.endsWith(".properties") ? profile : profile + ".properties";
            String filePath = Constants.DEFAULT_EXECUTION_PROFILE_PATH + fileName;
            try (InputStream inputStream = ParameterHelper.class.getClassLoader().getResourceAsStream(filePath)) {
                profileProperties.load(inputStream);
                profileProperties.setProperty(Constants.EXECUTION_PROFILE, fileName);
            } catch (Exception ex) {
                logger.error("Fail to load properties from {}", profile, ex);
            }
        }
    }

    protected void initDriver(Object[] args) {
        webDriver = createDriver(args);
    }
}
