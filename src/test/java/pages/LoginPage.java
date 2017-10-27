package pages;
import com.codeborne.selenide.Condition;
import org.openqa.selenium.*;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class LoginPage {
     private By loginInput = By.name("login");
     private By passwordInput = By.name("password");
     private By submitButton = By.id("firstAuth");
     private By proxyWarningText = By.cssSelector(".warning");
     private By continueButton = By.cssSelector("div[ng-switch-when='SHOW_MESSAGE'] button");
     private By regionSubmitButton = By.id("region");
     private By spinner = By.cssSelector(".spinner");
     private By titleText = By.cssSelector(".search-params-title span");

     public void login(String login, String password) throws InterruptedException {
          $(spinner).shouldNot(Condition.exist);
          if(getLoginInputCount() == 1) {
               $(loginInput).shouldBe(Condition.enabled).setValue(login);
               $(passwordInput).setValue(password);
               $(submitButton).click();
               if(getProxyTextCount() == 1) {
                    $(continueButton).click();
               }
               $(regionSubmitButton).click();
               $(titleText).shouldBe(Condition.visible);
          }

     }

     private int getProxyTextCount() {
          return $$(proxyWarningText).size();
     }

     private int getLoginInputCount() {
          return $$(loginInput).size();
     }
}
