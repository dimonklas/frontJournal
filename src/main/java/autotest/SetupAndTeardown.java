package autotest;

import autotest.pages.LoginPage;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.open;
import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;


public class SetupAndTeardown {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();
    private final Logger LOG = Logger.getLogger(SetupAndTeardown.class);

    private String locale = CV.locale;
    private String browser = CV.CURRENT_BROWSER;


    @BeforeSuite(alwaysRun = true)
    public void SetUpBrowser() throws Exception {

        switch (locale) {
            case "UA" : Locale.setDefault(new Locale("uk", "ua"));
                LOG.info("Set locale to uk-UA");
                break;
            case "RU" : Locale.setDefault(new Locale("ru", "ua"));
                LOG.info("Set locale to ru-UA");
                break;
            case "EN" : Locale.setDefault(new Locale("en", "ua"));
                LOG.info("Set locale to en-UA");
                break;
            default: Locale.setDefault(new Locale("uk", "ua"));
                LOG.info("Locale is uk-UA");
        }

        switch (browser){
            case "firefox" :
                Configuration.browser = "autotest.webDriverProviders.FirefoxDriverProvider";
                break;
            case "chrome" :
                Configuration.browser = "autotest.webDriverProviders.ChromeDriverProvider";
                break;
            default:
                Configuration.browser = "autotest.webDriverProviders.FirefoxDriverProvider";
                break;
        }

        Configuration.startMaximized = true;
        Configuration.timeout = Long.parseLong(CV.timeout);
        baseUrl = CV.BASE_URL;
    }

    @BeforeMethod
    public void login() {
        LoginPage loginPage = new LoginPage();
        open(baseUrl);
        if (loginPage.needAuth()) loginPage.login(CV.USER_LOGIN, CV.USER_PASSWORD);
    }

    @BeforeSuite(alwaysRun = true)
    void emptyDownloadsDir()  {
        if (!new File(CV.downloadsDir).mkdir()) {
            File[] files = new File(CV.downloadsDir).listFiles();
            Arrays.stream(files).forEach(FileUtils::deleteQuietly);
        }
    }

    @BeforeSuite(alwaysRun = true)
    void deleteOldDirs() throws IOException {
        File buildReportsDir = new File("build/reports");
        if (buildReportsDir.exists())
            FileUtils.deleteDirectory(buildReportsDir);
    }

    @AfterSuite(alwaysRun = true)
    void closeBrowser() {
        if(WebDriverRunner.hasWebDriverStarted()) {
            WebDriverRunner.closeWebDriver();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void createEnvironmentProps() {
        try (FileOutputStream fos = new FileOutputStream("target/allure-results/environment.properties")){
            Properties props = new Properties();

            ofNullable(baseUrl).ifPresent(s -> props.setProperty("project.URL", CV.BASE_URL));
            ofNullable(getProperty("browser")).ifPresent(s -> props.setProperty("browser", s));
            ofNullable(getProperty("driver.version")).ifPresent(s -> props.setProperty("driver.version", s));
            ofNullable(getProperty("os.name")).ifPresent(s -> props.setProperty("os.name", s));
            ofNullable(getProperty("os.version")).ifPresent(s -> props.setProperty("os.version", s));
            ofNullable(getProperty("os.arch")).ifPresent(s -> props.setProperty("os.arch", s));

            props.store(fos, "See https://github.com/allure-framework/allure-app/wiki/Environment");

            fos.close();
        } catch (IOException e) {
            LOG.error("IO problem when writing allure properties file", e);
        }
    }
}
