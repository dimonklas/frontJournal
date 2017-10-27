package pages;

import MethodUtils.Utils;
import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.text.ParseException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FrontJournalPage {
    Utils utils = new Utils();

    private By prodRefInput = By.id("input_10");
    private By detailsInput = By.id("input_12");
    private By operationInput = By.id("input-7");
    private By stateSelect = By.id("select_8");
    private By searchButton = By.cssSelector(".search-params-find-button");
    private By findedOperationsText = By.cssSelector(".search-params-finded-operations-title");
    private By claimDateText = By.cssSelector(".search-result-create-date-time");
    private By claimNameText = By.cssSelector(".search-result-operation-name");
    private By claimFioText = By.cssSelector(".search-result-fio");
    private By claimInnText = By.cssSelector(".search-result-inn");
    private By claimLdapText = By.cssSelector(".search-result-ldap");
    private By claimStateText = By.cssSelector(".search-result-state-name");
    private By claimDetailsText = By.cssSelector(".search-result-details");
    private By unipacksasLabel = By.xpath("//span[contains(., 'Оформить карту Универсальная')]");
    private By startSearchDate = By.id("input_4");


    @Step("Ввести '{text}' в поле референса заявки")
    public void fillProdRefInput(String text) {
        $(prodRefInput).setValue(text);
    }

    @Step("Ввести '{text}' в поле для поиска")
    public void fillDetailsInput(String text) {
        $(detailsInput).setValue(text);
    }

    @Step("Ввести '{text}' в поле для выбора операции")
    public void fillOperationInput(String text) {
        $(operationInput).setValue(text);
    }

    @Step("Нажать на кнопку 'Найти'")
    public void clickSearchButton() {
        $(searchButton).click();
    }

    @Step("Дождаться изменения количества отображаемых заявок")
    public void waitingForSearchResults(String text) {
        $(findedOperationsText).shouldHave(Condition.text(text));
    }

    @Step("Получить дату заявки")
    public String getClaimDate(int ... index) {
        String result = index.length == 0 ?
            $$(claimDateText).get(0).getText() : $$(claimDateText).get(index[0]).getText();
        return result;
    }

    @Step("Получить имя заявки")
    public String getClaimName(int ... index) {
        String result = index.length == 0 ?
            $$(claimNameText).get(0).getText() : $$(claimNameText).get(index[0]).getText();
        return result;
    }

    @Step("Получить ФИО заявки")
    public String getClaimFio(int ... index) {
        String result = index.length == 0 ?
            $$(claimFioText).get(0).getText() : $$(claimFioText).get(index[0]).getText();
        return result;
    }

    @Step("Получить ИНН заявки")
    public String getClaimInn(int ... index) {
        String result = index.length == 0 ?
            $$(claimInnText).get(0).getText() : $$(claimInnText).get(index[0]).getText();
        return result;
    }

    @Step("Получить LDAP заявки")
    public String getClaimLdap(int ... index) {
        String result = index.length == 0 ?
            $$(claimLdapText).get(0).getText() : $$(claimLdapText).get(index[0]).getText();
        return result;
    }

    @Step("Получить статус заявки")
    public String getClaimState(int ... index) {
        String result = index.length == 0 ?
            $$(claimStateText).get(0).getText() : $$(claimStateText).get(index[0]).getText();
        return result;
    }

    @Step("Получить детали заявки")
    public String getClaimDetails(int ... index) {
        String result = index.length == 0 ?
            $$(claimDetailsText).get(0).getText() : $$(claimDetailsText).get(index[0]).getText();
        return result;
    }

    @Step("Выбрать операцию 'Оформить карту Универсальная'")
    public void clickUnipacksasLabel() {
        $(unipacksasLabel).click();
    }

    @Step("Выбрать состояние '{state}' для операции")
    public void selectOperationState(String state) {
        $(stateSelect).click();
        $(By.cssSelector("md-option[value='" + state.toUpperCase() + "']")).click();
    }

    /**
     * Select date from datepicker
     * @param date
     * 'date' should be in format d-MM
     */
    @Step("Указать дату начала поиска равной {date} с помощью js")
    public void setStartSearchDate(String date) throws ParseException {
        $(startSearchDate).click();
        selectDateInDatepicker(date);
    }

    private void selectDateInDatepicker(String date) throws ParseException {
        String day = date.split("-")[0];
        String month = date.split("-")[1];
        String monthName = utils.changeDateFormat(date, "d-MM", "MMM").substring(0, 3);

        if(month.equals(utils.getCurrentDate("MM"))) {
            $(By.xpath(
                "//td[contains(@class, 'md-calendar-selected-date')]/../../tr/td/span[text()='" + day + "']")
            ).shouldBe(Condition.enabled).click();
        } else {
            $(By.xpath(
                "//td[contains(@class, 'md-calendar-selected-date')]/../..//td[@class='md-calendar-month-label']")
            ).shouldBe(Condition.enabled).click();
            $(By.xpath("//td/span[text()='" + monthName + "']")).shouldBe(Condition.enabled).click();
            $(By.xpath(
                "//td[contains(@class, \"md-calendar-month-label\") and contains(., '" + monthName + "')]/../.." +
                    "//td[contains(@class, 'md-calendar-date')]/span[text()='" + day + "']")
            ).shouldBe(Condition.enabled).click();
        }
    }

}



