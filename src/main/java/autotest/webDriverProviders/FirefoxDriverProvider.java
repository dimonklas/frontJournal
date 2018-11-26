package autotest.webDriverProviders;

import com.codeborne.selenide.WebDriverProvider;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import autotest.ConfigurationVariables;

import java.io.File;
import java.util.Arrays;


/**
 * Запускает браузер Firefox с настроенным профилем.
 * Плагины браузера(Add-ons) автоматом добавлятся, если поместисть файлы (*.xpi) в /src/main/resources/extensions
 * Драйвер браузера автоматически загружается и прописывается в classPath
 */

public class FirefoxDriverProvider implements WebDriverProvider {

    private final ConfigurationVariables CV = ConfigurationVariables.getInstance();

    @Override
    public WebDriver createDriver(DesiredCapabilities capabilities) {
        WebDriverManager.firefoxdriver().setup();

        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "target/geckodriverlog.txt");

        FirefoxProfile firefoxProfile = new FirefoxProfile();
//        firefoxProfile.setAcceptUntrustedCertificates(true);
//        firefoxProfile.setAssumeUntrustedCertificateIssuer(true);

        firefoxProfile.setPreference("javascript.enabled", true);
        firefoxProfile.setPreference("geo.enabled", false);

        switch (CV.locale) {
            case "UA" : firefoxProfile.setPreference("intl.accept_languages", "uk,ru,en-us,en");
                break;
            case "RU" : firefoxProfile.setPreference("intl.accept_languages", "ru,uk,en-us,en");
                break;
            case "EN" : firefoxProfile.setPreference("intl.accept_languages", "en-us,en,ru,uk");
                break;
            default: firefoxProfile.setPreference("intl.accept_languages", "ru,uk,en-us,en");
        }



        firefoxProfile.setPreference("browser.cache.disk.enable", false);
        firefoxProfile.setPreference("browser.cache.memory.enable", false);
        firefoxProfile.setPreference("browser.cache.offline.enable", false);
        firefoxProfile.setPreference("browser.helperApps.alwaysAsk.force", false);
        firefoxProfile.setPreference("browser.download.folderList",2);
        firefoxProfile.setPreference("browser.download.useDownloadDir",true);
        firefoxProfile.setPreference("browser.download.dir",CV.downloadsDir);
        firefoxProfile.setPreference("browser.download.manager.showWhenStarting",false);
        firefoxProfile.setPreference("browser.download.panel.shown",false);
        firefoxProfile.setPreference("browser.download.manager.alertOnEXEOpen", false);
        firefoxProfile.setPreference("browser.download.manager.focusWhenStarting", false);
        firefoxProfile.setPreference("browser.download.manager.useWindow", true);
        firefoxProfile.setPreference("browser.download.manager.closeWhenDone", true);

        firefoxProfile.setPreference("network.http.use-cache", false);
        firefoxProfile.setPreference("network.proxy.type", 0);

        firefoxProfile.setPreference("media.gmp-manager.cert.requireBuiltIn",false);
        firefoxProfile.setPreference("media.gmp-manager.cert.checkAttributes",false);
        firefoxProfile.setPreference("media.gmp-provider.enabled",false);
        firefoxProfile.setPreference("media.gmp-widevinecdm.enabled",false);
        firefoxProfile.setPreference("media.gmp-widevinecdm.visible",false);
        firefoxProfile.setPreference("media.gmp.trial-create.enabled",false);

        firefoxProfile.setPreference("app.update.enabled", false);
        firefoxProfile.setPreference("app.update.service.enabled", false);
        firefoxProfile.setPreference("app.update.auto", false);
        firefoxProfile.setPreference("app.update.staging.enabled", false);
        firefoxProfile.setPreference("app.update.silent", false);

        firefoxProfile.setPreference("extensions.update.autoUpdate", false);
        firefoxProfile.setPreference("extensions.update.autoUpdateEnabled", false);
        firefoxProfile.setPreference("extensions.update.enabled", false);
        firefoxProfile.setPreference("extensions.update.autoUpdateDefault", false);
        firefoxProfile.setPreference("extensions.blocklist.enabled", false);

        addExtensions(firefoxProfile);

        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(firefoxProfile);
        options.setAcceptInsecureCerts(true);
//        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
//        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.);
//        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

        FirefoxDriver driver = new FirefoxDriver(options);

        System.setProperty("browser", options.getBrowserName() + " " + driver.getCapabilities().getCapability("browserVersion").toString());
        System.setProperty("driver.version", WebDriverManager.firefoxdriver().getDownloadedVersion());

        return driver;
    }


     private void addExtensions(FirefoxProfile firefoxProfile) {
         File[] fileList = new File("src/main/resources").listFiles();
         Arrays.stream(fileList).filter(file -> file.isFile())
                                .filter(file -> file.getName().endsWith(".xpi"))
                                .forEach(file -> firefoxProfile.addExtension(file));
     }

}