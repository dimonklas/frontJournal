package autotest.pages;

import autotest.utils.Utils;
import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

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
    private By onlyMyClaimsRadiobutton = By.cssSelector("[ng-model='ctrl.onlyMyOperationsCheckboxEnable']");
    private By chooseClientRadiobutton = By.cssSelector("[ng-model='ctrl.searchByClientCheckboxEnable']");
    private By searchByPhysCardNumberButton = By.cssSelector("#tab-content-30" +
        " [ng-click='ctrl.changeSearchBy(ctrl.searchByConsts.PAN)']");
    private By searchByPhysIdEkbButton = By.cssSelector("#tab-content-30" +
        " [ng-click='ctrl.changeSearchBy(ctrl.searchByConsts.CLID)']");
    private By searchByPhysInnButton = By.cssSelector("#tab-content-30" +
        " [ng-click='ctrl.changeSearchBy(ctrl.searchByConsts.INN)']");
    private By searchByPhysDocSeriesAndNumberButton = By.cssSelector("#tab-content-30" +
        " [ng-click='ctrl.changeSearchBy(ctrl.searchByConsts.DOC)']");
    private By searchByPhysFioAndBirthdayButton = By.cssSelector("#tab-content-30" +
        " [ng-click='ctrl.changeSearchBy(ctrl.searchByConsts.FIO_BD)']");
    private By searchByJurCardNumberButton = By.cssSelector("#tab-content-31" +
        " [ng-click='ctrl.changeSearchBy(ctrl.searchByConsts.PAN)']");
    private By searchByJurIdEkbButton = By.cssSelector("#tab-content-31" +
        " [ng-click='ctrl.changeSearchBy(ctrl.searchByConsts.CLID)']");
    private By searchByJurInnOkpoButton = By.cssSelector("#tab-content-31" +
        " [ng-click='ctrl.changeSearchBy(ctrl.searchByConsts.INN_OKPO)']");
    private By searchByJurPaymentAccountButton = By.cssSelector("#tab-content-31" +
        " [ng-click='ctrl.changeSearchBy(ctrl.searchByConsts.ACC_INFO)']");
    private By searchByJurFioAndBirthdayButton = By.cssSelector("#tab-content-31" +
        " [ng-click='ctrl.changeSearchBy(ctrl.searchByConsts.FIO_BD)']");
    private By cardNumberInput = By.cssSelector("input[name='pan']");
    private By idEkbInput = By.cssSelector("input[name='id']");
    private By innInput = By.cssSelector("input[name='okpo']");
    private By phoneInput = By.cssSelector("input[name='allPhone']");
    private By docSeriesInput = By.cssSelector("input[name='dser']");
    private By docNumberInput = By.cssSelector("input[name='dnum']");
    private By lastNameInput = By.cssSelector("input[name='lname']");
    private By firstNameInput = By.cssSelector("input[name='fname']");
    private By patronymicInput = By.cssSelector("input[name='mname']");
    private By birthdateInput = By.cssSelector("[name='db'] input");
    private By paymentAccountInput = By.cssSelector("input[name='accInfo']");
    private By continueButton = By.cssSelector("button.client-search-params-continue-btn");
    private By editSearchParamsButton = By.cssSelector(".client-search-params-info-edit-icon");
    private By selectJurTabButton = By.xpath("//md-tab-item/span[text()='Юр.лицо']");


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
        $(findedOperationsText).waitUntil(Condition.text(text), 15000);
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
    public void setStartSearchDate(String date) throws Exception {
        $(startSearchDate).click();
        selectDateInDatepicker(date);
    }

    private void selectDateInDatepicker(String date) throws Exception {
        String day = date.split("-")[0];
        String monthName = utils.changeDateFormat(date, "d-MM", "MMM").substring(0, 3);

        Thread.sleep(1000);
        $(By.xpath("//td[contains(@class, 'md-calendar-selected-date')]/../..//td[@class='md-calendar-month-label']"))
            .shouldBe(Condition.visible).click();
        $(By.xpath("//td/span[text()='" + monthName + "']")).shouldBe(Condition.enabled).click();
        $(By.xpath(
            "//td[contains(@class, \"md-calendar-month-label\") and contains(., '" + monthName + "')]/../.." +
                "//td[contains(@class, 'md-calendar-date')]/span[text()='" + day + "']")
        ).shouldBe(Condition.enabled).click();

    }

    @Step("Нажать на радиобаттон 'Только мои заявки'")
    public void clickOnlyMyClaimsRadiobutton() {
        $(onlyMyClaimsRadiobutton).click();
    }

    @Step("Нажать на радиобаттон 'Выбрать клиента'")
    public void clickChooseClientRadiobutton() {
        $(chooseClientRadiobutton).click();
    }

    @Step("Нажать на кнопку 'Поиск по номеру карты'")
    public void clickSearchByPhysCardNumberButton() {
        $(searchByPhysCardNumberButton).click();
    }

    @Step("Нажать на кнопку 'Поиск по номеру карты' для юр.лица")
    public void clickSearchByJurCardNumberButton() {
        $(searchByJurCardNumberButton).click();
    }

    @Step("Нажать на кнопку 'Поиск по ID в ЕКБ'")
    public void clickSearchByPhysIdEkbButton() {
        $(searchByPhysIdEkbButton).click();
    }

    @Step("Нажать на кнопку 'Поиск по ID в ЕКБ' для юр.лица")
    public void clickSearchByJurIdEkbButton() {
        $(searchByJurIdEkbButton).click();
    }

    @Step("Нажать на кнопку 'Поиск по ИНН'")
    public void clickSearchByPhysInnButton() {
        $(searchByPhysInnButton).click();
    }

    @Step("Нажать на кнопку 'Поиск по ИНН / ОКПО'")
    public void clickSearchByJurInnOkpoButton() {
        $(searchByJurInnOkpoButton).click();
    }

    @Step("Нажать на кнопку 'Поиск по номеру счета'")
    public void clickSearchByJurPaymentAccountButton() {
        $(searchByJurPaymentAccountButton).click();
    }

    @Step("Нажать на кнопку 'Поиск по документу'")
    public void clickSearchByPhysDocSeriesAndNumberButton() {
        $(searchByPhysDocSeriesAndNumberButton).click();
    }

    @Step("Нажать на кнопку 'Поиск по ФИО и ДР'")
    public void clickSearchByPhysFioAndBirthdayButton() {
        $(searchByPhysFioAndBirthdayButton).click();
    }

    @Step("Нажать на кнопку 'Поиск по ФИО и ДР' для юрлица")
    public void clickSearchByJurFioAndBirthdayButton() {
        $(searchByJurFioAndBirthdayButton).click();
    }

    @Step("Выбрать вкладку для поиска клиента 'Юр.лицо'")
    public void clickSelectJurTabButton() {
        $(selectJurTabButton).click();
    }

    @Step("Ввести '{text}' в поле для ввода номера карты")
    public void fillCardNumberInput(String text) {
        $(cardNumberInput).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода ID с ЕКБ")
    public void fillIdEkbInput(String text) {
        $(idEkbInput).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода ИНН")
    public void fillInnInput(String text) {
        $(innInput).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода номера телефона")
    public void fillPhoneInput(String text) {
        $(phoneInput).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода серии документа")
    public void fillDocSeriesInput(String text) {
        $(docSeriesInput).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода номера документа")
    public void fillDocNumberInput(String text) {
        $(docNumberInput).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода фамилии")
    public void fillLastNameInput(String text) {
        $(lastNameInput).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода имени")
    public void fillFirstNameInput(String text) {
        $(firstNameInput).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода отчества")
    public void fillPatronymicInput(String text) {
        $(patronymicInput).setValue(text);
    }

    @Step("Ввести '{text}' в поле для ввода номера счета")
    public void fillPaymentAccountInput(String text) {
        $(paymentAccountInput).setValue(text);
    }

    @Step("Нажать на кнопку 'Продолжить'")
    public void clickContinueButton() {
        $(continueButton).click();
    }

    @Step("Нажать на кнопку редактирования параметров поиска по клиенту")
    public void clickEditSearchParamsButton() {
        $(editSearchParamsButton).click();
    }

    @Step("Выбрать дату рождения")
    public void setBirthdate(String birthdate) throws Exception {
        String year = birthdate.substring(6);
        String month = utils.changeDateFormat(birthdate, "dd-MM-yyyy", "MMMM").substring(0, 3);
        String day = birthdate.substring(0, 2);
        executeJavaScript("arguments[0].removeAttribute('readonly','readonly');", $(birthdateInput));
        $(birthdateInput).setValue(birthdate);
        $(By.xpath("//td[text()='" + year + "']/../..//td/span[text()='" + month + "']")).click();
        $(By.xpath(
            "//td[contains(@class, \"md-calendar-month-label\") and contains(., '" + month + "')]/../.." +
                "//td[contains(@class, 'md-calendar-date')]/span[text()='" + day + "']")
        ).shouldBe(Condition.enabled).click();
    }
}



