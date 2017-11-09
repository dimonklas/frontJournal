package setup;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class SetupAndTeardown {
    LoginPage loginPage;
    ConfVars confVars = ConfVars.getInstance();

    @BeforeSuite
    protected void setUp() {
        Configuration.baseUrl = confVars.BASE_URL;
        Configuration.browser = confVars.CURRENT_BROWSER;
        Configuration.collectionsTimeout=10000;
        Configuration.timeout = 6000;
    }

    @BeforeMethod
    protected void login() throws Exception {
        loginPage = open("", LoginPage.class);
        loginPage.login(confVars.USER_LOGIN, confVars.USER_PASSWORD);
    }
}
