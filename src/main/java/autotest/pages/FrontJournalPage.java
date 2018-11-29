package autotest.pages;

import autotest.utils.Utils;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class FrontJournalPage {

    private Utils utils = new Utils();

    private By claimDateText = By.cssSelector(".search-result-create-date-time");
    private By claimNameText = By.cssSelector(".search-result-operation-name");
    private By claimFioText = By.cssSelector(".search-result-fio");
    private By claimInnText = By.cssSelector(".search-result-inn");
    private By claimLdapText = By.cssSelector(".search-result-ldap");
    private By claimStateText = By.cssSelector(".search-result-state-name");
    private By claimDetailsText = By.cssSelector(".search-result-details");


    @Step("Ввести '{text}' в поле референса заявки")
    public void fillProdRefInput(String text) {
        $(By.id("input_10")).setValue(text);
    }

    @Step("Ввести '{text}' в поле для поиска")
    public void fillDetailsInput(String text) {
        $(By.id("input_12")).setValue(text);
    }

    @Step("Ввести '{text}' в поле для выбора операции")
    public void fillOperationInput(String text) {
        $(By.id("input-7")).setValue(text);
    }

    @Step("Нажать на кнопку 'Найти'")
    public void clickSearchButton() {
        $(By.cssSelector(".search-params-find-button")).click();
    }

    @Step("Дождаться изменения количества отображаемых заявок")
    public void waitingForSearchResults(String text) {
        $(By.cssSelector(".search-params-finded-operations-title")).waitUntil(Condition.text(text), 15000);
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

    @Step("Выбрать операцию 'Выдать карту'")
    public void clickUnipacksasLabel() {
        $x("//span[contains(., 'Выдать карту')]").click();
    }

    @Step("Выбрать состояние '{state}' для операции")
    public void selectOperationState(String state) {
        $(By.id("select_8")).click();
        $(By.cssSelector("md-option[value='" + state.toUpperCase() + "']")).click();
    }

    /**
     * Select date from datepicker
     * @param date
     * 'date' should be in format d-MM
     */
    @Step("Указать дату начала поиска равной {date} с помощью js")
    public void setStartSearchDate(String date) throws Exception {
        $(By.id("input_4")).click();
        selectDateInDatepicker(date);
    }

    private void selectDateInDatepicker(String date) throws Exception {
        String day = date.split("-")[0];
        String monthName = utils.changeDateFormat(date, "d-MM", "MMM").substring(0, 3);

        Thread.sleep(1000);
        $(By.xpath("//td[contains(@class, 'md-calendar-selected-date')]/../..//td[@class='md-calendar-month-label']"))
            .shouldBe(visible).click();
        $(By.xpath("//td/span[text()='" + monthName + "']")).shouldBe(Condition.enabled).click();
        $(By.xpath(
            "//td[contains(@class, \"md-calendar-month-label\") and contains(., '" + monthName + "')]/../.." +
                "//td[contains(@class, 'md-calendar-date')]/span[text()='" + day + "']")
        ).shouldBe(Condition.enabled).click();

    }

    @Step("Нажать на радиобаттон 'Только мои заявки'")
    public void clickOnlyMyClaimsRadiobutton() {
        $(By.cssSelector("[ng-model='ctrl.onlyMyOperationsCheckboxEnable']")).click();
    }

    @Step("Нажать на радиобаттон 'Выбрать клиента'")
    public void clickChooseClientRadiobutton() {
        $(By.cssSelector("[ng-model='ctrl.searchByClientCheckboxEnable']")).click();
    }

    @Step("Нажать на кнопку 'Поиск по номеру карты'")
    public void clickSearchByPhysCardNumberButton() {
        $$x(".//button[contains(@ng-click,'PAN')]").filter(visible).first().click();
    }

    @Step("Нажать на кнопку 'Поиск по номеру карты' для юр.лица")
    public void clickSearchByJurCardNumberButton() {
        $$x(".//button[contains(@ng-click,'PAN')]").filter(visible).first().click();
    }

    @Step("Нажать на кнопку 'Поиск по ID в ЕКБ'")
    public void clickSearchByPhysIdEkbButton() {
        $$x(".//button[contains(@ng-click,'CLID')]").filter(visible).first().click();
    }

    @Step("Нажать на кнопку 'Поиск по ID в ЕКБ' для юр.лица")
    public void clickSearchByJurIdEkbButton() {
        $$x(".//button[contains(@ng-click,'CLID')]").filter(visible).first().click();
    }

    @Step("Нажать на кнопку 'Поиск по ИНН'")
    public void clickSearchByPhysInnButton() {
        $$x(".//button[contains(@ng-click,'INN')]").filter(visible).first().click();
    }

    @Step("Нажать на кнопку 'Поиск по ИНН / ОКПО'")
    public void clickSearchByJurInnOkpoButton() {
        $$x(".//button[contains(@ng-click,'INN')]").filter(visible).first().click();
    }

    @Step("Нажать на кнопку 'Поиск по номеру счета'")
    public void clickSearchByJurPaymentAccountButton() {
        $x(".//button[contains(@ng-click,'ACC_INFO')]").shouldBe(visible).click();
    }

    @Step("Нажать на кнопку 'Поиск по документу'")
    public void clickSearchByPhysDocSeriesAndNumberButton() {
        $x(".//button[contains(@ng-click,'DOC')]").shouldBe(visible).click();
    }

    @Step("Нажать на кнопку 'Поиск по ФИО и ДР'")
    public void clickSearchByPhysFioAndBirthdayButton() {
        $$x(".//button[contains(@ng-click,'FIO_BD')]").filter(visible).first().click();
    }

    @Step("Нажать на кнопку 'Поиск по ФИО и ДР' для юрлица")
    public void clickSearchByJurFioAndBirthdayButton() {
        $$x(".//button[contains(@ng-click,'FIO_BD')]").filter(visible).first().click();
    }

    @Step("Выбрать вкладку для поиска клиента 'Юр.лицо'")
    public void clickSelectJurTabButton() {
        $x("//md-tab-item/span[text()='Юр.лицо' or text()='Юр.особа']").click();
    }

    @Step("Ввести '{text}' в поле для ввода номера карты")
    public void fillCardNumberInput(String text) {
        $x(".//input[@name='pan']").shouldBe(visible, enabled).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода ID с ЕКБ")
    public void fillIdEkbInput(String text) {
        $x(".//input[@name='id']").shouldBe(visible, enabled).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода ИНН")
    public void fillInnInput(String text) {
        $x(".//input[@name='okpo']").shouldBe(visible, enabled).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода номера телефона")
    public void fillPhoneInput(String text) {
        $x(".//input[@name='allPhone']").shouldBe(visible, enabled).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода серии документа")
    public void fillDocSeriesInput(String text) {
        $x(".//input[@name='dser']").shouldBe(visible, enabled).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода номера документа")
    public void fillDocNumberInput(String text) {
        $x(".//input[@name='dnum']").shouldBe(visible, enabled).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода фамилии")
    public void fillLastNameInput(String text) {
        $x(".//input[@name='lname']").shouldBe(visible, enabled).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода имени")
    public void fillFirstNameInput(String text) {
        $x(".//input[@name='fname']").shouldBe(visible, enabled).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода отчества")
    public void fillPatronymicInput(String text) {
        $x(".//input[@name='mname']").shouldBe(visible, enabled).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода номера счета")
    public void fillPaymentAccountInput(String text) {
        $x(".//input[@name='accInfo']").shouldBe(visible, enabled).setValue(text);
    }

    @Step("Нажать на кнопку 'Продолжить'")
    public void clickContinueButton() {
        $(By.cssSelector("button.client-search-params-continue-btn")).click();
    }

    @Step("Нажать на кнопку редактирования параметров поиска по клиенту")
    public void clickEditSearchParamsButton() {
        $(By.cssSelector(".client-search-params-info-edit-icon")).click();
    }

    @Step("Выбрать дату рождения")
    public void setBirthdate(String birthdate) throws Exception {
        SelenideElement birthdateInput = $(By.cssSelector("[name='db'] input")).shouldBe(visible);
        String year = birthdate.substring(6);
        String month = utils.changeDateFormat(birthdate, "dd-MM-yyyy", "MMMM").substring(0, 3);
        String day = birthdate.substring(0, 2);
        executeJavaScript("arguments[0].removeAttribute('readonly','readonly');", birthdateInput);
        birthdateInput.setValue(birthdate);
        $(By.xpath("//td[text()='" + year + "']/../..//td/span[text()='" + month + "']")).click();
        $(By.xpath(
            "//td[contains(@class, \"md-calendar-month-label\") and contains(., '" + month + "')]/../.." +
                "//td[contains(@class, 'md-calendar-date')]/span[text()='" + day + "']")
        ).shouldBe(Condition.enabled).click();
    }
}



