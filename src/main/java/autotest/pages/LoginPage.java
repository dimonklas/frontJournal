package autotest.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import autotest.utils.RestUtils;
import autotest.utils.Utils;

import javax.xml.xpath.XPathExpressionException;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class LoginPage {

     private Logger log = Logger.getLogger(LoginPage.class);

     private By loginInput = By.name("login");
     private By passwordInput = By.name("password");
     private By submitButton = By.id("firstAuth");
     private By proxyWarningText = By.cssSelector(".warning");
     private By continueButton = By.cssSelector("div[ng-switch-when='SHOW_MESSAGE'] button");
     private By regionSubmitButton = By.id("region");
     private By spinner = By.cssSelector(".spinner");
     private By titleText = By.cssSelector(".search-params-title span");

     private SelenideElement
             secondAuthForm = $(By.xpath("//div[@name='SECOND_AUTH']")),
             atmTsoPasswordButton = $(By.xpath("//div[text()='Пароль из АТМ/ТСО']")),
             tsoPasswordField = $(By.xpath("//input[@name='tss']")),
             continueButton3 = $(By.xpath("//button[@id='secondAuthTss']"));

     public void login(String login, String password) {
          $(spinner).shouldNot(Condition.exist);
          if(getLoginInputCount() == 1) {
               $(loginInput).shouldBe(Condition.enabled).setValue(login);
               $(passwordInput).setValue(password);
               $(submitButton).click();
               if(getProxyTextCount() == 1) {
                    $(continueButton).click();
               }
               $(regionSubmitButton).click();
//               $(titleText).shouldBe(Condition.visible);
               secondAuth();
          }

     }

     private void secondAuth() {
          try{
               secondAuthForm.shouldBe(visible);
          } catch (Throwable t) {
               //подождали появления формы второго уровня аутентификации
          }

          if(secondAuthForm.isDisplayed()) {
               String totp = "";
               try{
                    totp = Utils.xPath("/doc/password", new RestUtils().getTotp());
               } catch (XPathExpressionException ex) {
                    ex.printStackTrace();
               }

               atmTsoPasswordButton.shouldBe(visible, enabled).click();
               tsoPasswordField.shouldBe(visible, enabled).clear();
               tsoPasswordField.setValue(totp);

               continueButton3.shouldBe(visible, enabled).click();
          }

     }


     private int getProxyTextCount() {
          return $$(proxyWarningText).size();
     }

     private int getLoginInputCount() {
          return $$(loginInput).size();
     }
}