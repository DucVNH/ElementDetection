package core.testng;


import core.helper.LogHelper;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.IExecutionListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestExecutionListener implements IExecutionListener, ITestListener {

    private static final LogHelper logger = LogHelper.getInstance();

    @Override
    public void onExecutionStart() {
        // Do nothing
    }

    @Override
    public void onExecutionFinish() {
        logger.info("Clean up WebDrivers.");
        // TODO
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        // Do nothing
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        // Do nothing
    }

    @Override
    public synchronized void onTestFailure(ITestResult iTestResult) {
        Object currentClass = iTestResult.getInstance();
        String methodName = iTestResult.getMethod().getMethodName().trim();
        logger.info(String.format("%s is failed", methodName));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        // Do nothing
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        // Do nothing
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        // Do nothing
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        // Do nothing
    }
}