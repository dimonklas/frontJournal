package autotest.utils;

import autotest.ConfigurationVariables;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.sleep;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasXPath;

public class RestUtils {
    private ConfigurationVariables confVars = ConfigurationVariables.getInstance();

    @Step("Добавить заявку 'IDENTPHYS' с референсом {ref} через сервис")
    public void addClaim(String date, String ref, String ldap) throws Exception {
        given().
            header("Content-Type", "text/xml;charset=UTF-8").
            body(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<modify sys=\"WAVE\" pass=\"qwerty\">" +
                    "<ref>" + ref + "</ref>" +
                    "<idp>IDENTPHYS</idp>" +
                    "<dc>" + date + "</dc>" +
                    "<dm>" + date + "</dm>" +
                    "<det>&lt;b&gt;уникальное слово - нотрдамиус&lt;/b&gt;&lt;br\\></det>" +
                    "<clid>17589602</clid>" +
                    "<fio>" +
                    confVars.LAST_NAME.toUpperCase() + " " +
                    confVars.FIRST_NAME.toUpperCase() + " " +
                    confVars.PATRONYMIC.toUpperCase() +
                    "</fio>" +
                    "<inn>2181618276</inn>" +
                    "<st>NEW</st>" +
                    "<pfin>y</pfin>" +
                    "<branch>DNH0</branch>" +
                    "<ldap>" + ldap.toUpperCase() + "</ldap>" +
                    "<url>"
                    + confVars.FRONT_JOURNAL_URL +
                    "</url>" +
                    "<url_pr>"
                    + confVars.FRONT_JOURNAL_URL +
                    "</url_pr>" +
                    "</modify>"
            ).
            when().
            post(confVars.MODIFY_RECORD_URL).
            then().
            body(hasXPath("//answer[@status='OK']"));
        sleep(5 * 1000);
    }

    public String getTotp(){
        return
                given()
                        .relaxedHTTPSValidation()
                        .header("Content-Type", "text/xml;charset=UTF-8").
                when()
                        .get(confVars.TOTP_URL)
                        .asString();
    }
}
