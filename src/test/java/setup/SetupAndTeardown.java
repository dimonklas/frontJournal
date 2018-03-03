package setup;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import pages.LoginPage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class SetupAndTeardown {
    LoginPage loginPage;
    ConfVars confVars = ConfVars.getInstance();
    String browserName;
    String browserVersion;
    String platform;
    String driverVersion;

    @BeforeSuite
    protected void setUp() {
        Configuration.baseUrl = confVars.BASE_URL;
        Configuration.browser = confVars.CURRENT_BROWSER;
        Configuration.collectionsTimeout=10000;
        Configuration.timeout = 6000;
    }

    @BeforeMethod
    protected void login() throws Exception {
        if(browserName == null && browserVersion == null && platform == null && driverVersion == null) setEnvironment();
        loginPage = open("", LoginPage.class);
        loginPage.login(confVars.USER_LOGIN, confVars.USER_PASSWORD);
    }

    @AfterMethod(alwaysRun = true)
    public void createEnvironmentPropertiesFile() {
        if(!new File("./build/allure-results/environment.properties").exists()) {
            File file = new File("./build/allure-results/environment.propertiesffffffffffffffffff");
            List<String> lines = Arrays.asList(
                    "Platform=" + platform,
                    "Browser=" + browserName,
                    "Browser.Version=" + browserVersion,
                    "DriverVersion=" + driverVersion
            );
            Path filePath = Paths.get(file.getAbsolutePath());
            try {
                Files.write(filePath, lines, Charset.forName("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setEnvironment() {
        Capabilities cap = ((RemoteWebDriver) getWebDriver()).getCapabilities();
        browserName = cap.getBrowserName().substring(0, 1).toUpperCase() + cap.getBrowserName().substring(1).toLowerCase();
        browserVersion = cap.getVersion();
        platform = cap.getPlatform().toString();
        if(browserName.toLowerCase().equals("chrome")) {
            driverVersion = ChromeDriverManager.getInstance().getDownloadedVersion();
        } else {
            driverVersion = FirefoxDriverManager.getInstance().getDownloadedVersion();
        }
    }
}
