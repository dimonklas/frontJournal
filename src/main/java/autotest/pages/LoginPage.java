package autotest.pages;

import autotest.utils.RestUtils;
import autotest.utils.Utils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

     private Logger log = Logger.getLogger(LoginPage.class);

     /*
     * Аутентификация на ЕСА (первый фактор)
     * */
     public void login(String login, String password) {
          $(By.cssSelector(".spinner")).shouldNot(exist.because("Ждем пока пропадет прелоадер"));
          $(By.name("login")).shouldBe(visible, enabled).setValue(login);
          $(By.name("password")).shouldBe(visible, enabled).setValue(password);
          $(By.id("firstAuth")).shouldBe(visible, enabled.because("Кнопка подтверждения пароля")).click();
          if ($(By.cssSelector(".warning")).isDisplayed()) $(By.cssSelector("div[ng-switch-when='SHOW_MESSAGE'] button")).click();
          $(By.id("region")).shouldBe(visible, enabled.because("Кнопка подтверждения выбора региона")).click();
          $(By.cssSelector(".spinner")).shouldNot(exist.because("Ждем пока пропадет прелоадер"));

          secondAuth();
     }

     /*
     * Аутентификация на ЕСА (второй фактор)
     * */
     private void secondAuth() {
          if($(By.name("SECOND_AUTH")).isDisplayed()) {
               String totp = Utils.xPath("/doc/password", new RestUtils().getTotp());

               $(By.xpath(".//*[contains(text(),'з АТМ/ТСО')]")).shouldBe(visible.because("Кнопка 'Пароль из АТМ/ТСО'")).click();
               $(By.name("tss")).shouldBe(visible, enabled.because("Поле ввода пароля из АТМ/ТСО")).setValue(totp);
               $(By.id("secondAuthTss")).shouldBe(visible, enabled.because("Кнопка подтверждения пароля из АТМ/ТСО")).click();
          }
     }

     public boolean needAuth(){
          return $(By.name("login")).isDisplayed();
     }
}