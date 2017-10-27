package listeners;

import com.codeborne.selenide.Screenshots;
import com.google.common.io.Files;
import io.qameta.allure.Attachment;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AllureListener implements ITestListener {


    @Override
    public void onTestStart(ITestResult iTestResult) {

    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        try {
            screenshot();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        try {
            screenshot();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {

    }

    @Attachment(value = "Failure screenshot", type = "image/png")
    private byte[] screenshot() throws IOException {
        File screenshot = new File(Screenshots.takeScreenShot(currencyDate()));
        return Files.toByteArray(screenshot);
    }

    private String currencyDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatDate = new SimpleDateFormat("ddMMyyyyHHmmssS");
        return formatDate.format(cal.getTime());
    }
}
