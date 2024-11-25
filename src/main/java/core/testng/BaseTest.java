package core.testng;

import core.entity.EnvironmentInfo;
import core.helper.Constants;
import core.helper.ExceptionHelper;
import core.page.BasePage;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import java.io.IOException;

import static core.webdriver.WebDriverInit.initWebDriver;

public abstract class BaseTest extends AbstractBaseTest {
    Process process;
    public EnvironmentInfo environmentInfo = new EnvironmentInfo();


    @BeforeSuite
    public void beforeSuite(ITestContext context) {
        getProfileProperties(context);
        logger.info("Before Suite Setup ...... ");
    }

    @BeforeMethod(alwaysRun = true)
    @Override
    public void initDriver(Object[] args) {
        super.initDriver(args);
        String username = profile("EMAIL");
        String password = profile("PASSWORD");
        environmentInfo.setUsername(username);
        environmentInfo.setPassword(password);
    }


    @AfterSuite(alwaysRun = true)
    public void logLaunchUrl() {
        logger.info("After Suite Setup ...... ");
    }
    @BeforeTest
    public void beforeTest(ITestContext context) {
        logger.info("Before Test Setup ...... ");

        try {
            logger.info("Initializing parameters.");
            loadTestParameters(context);

            setTestBrowser();
            setSeleniumServer();

            String name = parametersTest.get(Constants.BROWSER_NAME_PARAMETER);

            logger.info("Initializing WebDriver {}", name);
            webDriver = initWebDriver();
            webDriver.manage().window().maximize();

            logger.info("Initializing Pages.");
            initPages();
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error("The format of the testcase is not correct. Please help to check.");
            ExceptionHelper.rethrow(e);
        } catch (Exception e) {
            ExceptionHelper.rethrow(e);
        }

    }
    @AfterTest
    public void afterTest(ITestContext context) {
        logger.info("After Test Setup ...... ");
        logger.info("===============================================");
        if (BasePage.totalExecutions > 0) {
            double timeInSec = (double) BasePage.totalExecutionTime / 1_000_000_000;
            double averageTime = timeInSec / BasePage.totalExecutions;
            logger.info("Total number of web elements located using attributes: " + BasePage.totalExecutions);
            logger.info("Total time taken to locate web elements using attributes: " + timeInSec);
            logger.info("Average time to locate an element: " + averageTime + " seconds");
        } else {
            logger.info("No executions were recorded.");
        }
        logger.info("===============================================");
    }
    @BeforeGroups
    public void beforeGroups(ITestContext context) {
        logger.info("Before Group Setup ...... ");

    }
    @AfterGroups
    public void afterGroups(ITestContext context) {
        logger.info("After Group Setup ...... ");

    }
    @BeforeClass
    public void beforeClass(ITestContext context) {
        logger.info("Before Class Setup ...... ");

    }
    @AfterGroups
    public void afterClass(ITestContext context) {
        logger.info("After Class Setup ...... ");
    }


    @BeforeMethod()
    @Override
    public void beforeTest(ITestContext context, ITestResult result) {
        logger.info("Before Method Setup ...... ");
    }

    @AfterMethod
    public void afterMethod() throws IOException {
        logger.info("After Method Setup ...... ");
    }


}