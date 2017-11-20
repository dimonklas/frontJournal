import MethodUtils.DataBaseUtils;
import MethodUtils.Utils;
import database.MongoDB;
import io.qameta.allure.Description;
import listeners.AllureListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.FrontJournalPage;
import rest.RestUtils;
import setup.ConfVars;
import setup.SetupAndTeardown;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.page;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Listeners(AllureListener.class)
public class TestRunnerFJ extends SetupAndTeardown {
    FrontJournalPage frontJournal;
    ConfVars confVars = ConfVars.getInstance();
    Utils utils = new Utils();
    DataBaseUtils dataBaseUtils = new DataBaseUtils();
    RestUtils restUtils = new RestUtils();

    enum OperationState {NEW, INWORK}

    @Test(groups = "FrontJournal", priority = 1, description = "Поиск по основным параметрам. Поиск заявки по референсу")
    public void searchClaimsInFrontJournalByReference() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8));

        Long ref = utils.generateClaimRef();

        MongoDB.INSTANCE.addUnfinishedClaims(
            ref, "WAVE","IDENTPHYS", utils.getCurrentDate("yyyy-MM-dd HH:mm"), confVars.OPER_LDAP,
            confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.fillProdRefInput(ref.toString());

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("1");

        Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(ref.toString());

        assertThat(frontJournal.getClaimDate()).isEqualTo(claimData.get("date"));

        assertThat(frontJournal.getClaimName()).isEqualTo(claimData.get("name"));

        assertThat(frontJournal.getClaimFio().toUpperCase()).isEqualTo(claimData.get("fio"));

        assertThat(frontJournal.getClaimInn()).isEqualTo(claimData.get("inn"));

        assertThat(frontJournal.getClaimLdap()).isEqualTo(claimData.get("ldap"));

        assertThat(frontJournal.getClaimState()).isEqualTo(claimData.get("state"));

        assertThat(frontJournal.getClaimDetails()).isEqualTo(claimData.get("details"));

        MongoDB.INSTANCE.deleteClaimsOlderDate(confVars.OPER_LDAP, utils.getCurrentDate("yyyy-MM-dd"));

        frontJournal.fillProdRefInput("11111");

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("0");
    }

    @Test(groups = "FrontJournal", priority = 10, description = "Поиск по основным параметрам. Поиск заявки по деталям")
    public void searchClaimsInFrontJournalByDetails() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8));

        Long ref = utils.generateClaimRef();

        restUtils.addClaim(
            utils.getCurrentDate("yyyy-MM-dd HH:mm:ss.S"), ref.toString(), confVars.OPER_LDAP
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.fillDetailsInput("нотрдамиус");

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("1");

        Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(ref.toString());

        assertThat(frontJournal.getClaimDate()).isEqualTo(claimData.get("date"));

        assertThat(frontJournal.getClaimName()).isEqualTo(claimData.get("name"));

        assertThat(frontJournal.getClaimFio().toUpperCase()).isEqualTo(claimData.get("fio"));

        assertThat(frontJournal.getClaimInn()).isEqualTo(claimData.get("inn"));

        assertThat(frontJournal.getClaimLdap()).isEqualTo(claimData.get("ldap"));

        assertThat(frontJournal.getClaimState()).isEqualTo(claimData.get("state"));

        assertThat(frontJournal.getClaimDetails()).isEqualTo(claimData.get("details"));

        frontJournal.fillDetailsInput("нотрда");

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("0");

        frontJournal.fillDetailsInput("нотрдамиус уникальное");

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("1");

        assertThat(frontJournal.getClaimName()).isEqualTo(claimData.get("name"));
    }

    @Test(groups = "FrontJournal", priority = 20, description = "Поиск по основным параметрам. Поиск заявки по операции")
    public void searchClaimsInFrontJournalByOperation() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8));

        Long ref = utils.generateClaimRef();

        MongoDB.INSTANCE.addUnfinishedClaims(
            ref, "WAVE","UNIPACKSAS", utils.getCurrentDate("yyyy-MM-dd HH:mm"), confVars.OPER_LDAP,
            confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.fillOperationInput("универсальная");

        frontJournal.clickUnipacksasLabel();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("1");

        Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(ref.toString());

        assertThat(frontJournal.getClaimDate()).isEqualTo(claimData.get("date"));

        assertThat(frontJournal.getClaimName()).isEqualTo(claimData.get("name"));

        assertThat(frontJournal.getClaimFio().toUpperCase()).isEqualTo(claimData.get("fio"));

        assertThat(frontJournal.getClaimInn()).isEqualTo(claimData.get("inn"));

        assertThat(frontJournal.getClaimLdap()).isEqualTo(claimData.get("ldap"));

        assertThat(frontJournal.getClaimState()).isEqualTo(claimData.get("state"));

        assertThat(frontJournal.getClaimDetails()).isEqualTo(claimData.get("details"));

    }

    @Test(groups = "FrontJournal", priority = 30, description = "Поиск по основным параметрам. Поиск заявки по операции и состоянию")
    public void searchClaimsInFrontJournalByOperationAndState() throws Exception {

        MongoDB.INSTANCE.deleteClaimsOlderDate(confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8));

        Long ref = utils.generateClaimRef();

        MongoDB.INSTANCE.addUnfinishedClaims(
            ref, "WAVE","UNIPACKSAS", utils.getCurrentDate("yyyy-MM-dd HH:mm"), confVars.OPER_LDAP,
            confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.fillOperationInput("универсальная");

        frontJournal.clickUnipacksasLabel();

        frontJournal.selectOperationState(OperationState.NEW.name());

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("1");

        Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(ref.toString());

        assertThat(frontJournal.getClaimDate()).isEqualTo(claimData.get("date"));

        assertThat(frontJournal.getClaimName()).isEqualTo(claimData.get("name"));

        assertThat(frontJournal.getClaimFio().toUpperCase()).isEqualTo(claimData.get("fio"));

        assertThat(frontJournal.getClaimInn()).isEqualTo(claimData.get("inn"));

        assertThat(frontJournal.getClaimLdap()).isEqualTo(claimData.get("ldap"));

        assertThat(frontJournal.getClaimState()).isEqualTo(claimData.get("state"));

        assertThat(frontJournal.getClaimDetails()).isEqualTo(claimData.get("details"));

        frontJournal.selectOperationState(OperationState.INWORK.name());

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("0");
    }

    @Test(groups = "FrontJournal", priority = 40, description = "Поиск по основным параметрам. Поиск заявок за несколько дней")
    public void searchClaimsInSeveralDays() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());


        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(0), "WAVE","IDENTPHYS", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(1), "WAVE","UNIPACKSAS", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -24),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.setStartSearchDate(utils.changeCurrentDate("d-MM", 5, -1));

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for(int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }
    }

    @Test(groups = "FrontJournal", priority = 50, description = "Поиск по основным параметрам. Поиск всех заявок за один день")
    public void searchAllClaimsInOneDay() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());


        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(0), "WAVE","IDENTPHYS", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(1), "WAVE","UNIPACKSAS", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -2),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for(int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }
    }

    @Test(groups = "FrontJournalPhys", priority = 60, description = "Физлицо. Поиск заявок по номеру карты")
    public void searchClaimsInFrontJournalByCardNumberPhys() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());


        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(0), "WAVE","IDENTPHYS", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(1), "WAVE","UNIPACKSAS", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -2),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.clickOnlyMyClaimsRadiobutton();

        frontJournal.clickChooseClientRadiobutton();

        frontJournal.clickSearchByPhysCardNumberButton();

//        frontJournal.fillCardNumberInput(confVars.PHYS_CARD_NUMBER.substring(8));
//
//        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));
//
//        frontJournal.clickContinueButton();
//
//        frontJournal.clickSearchButton();
//
//        frontJournal.waitingForSearchResults("2");
//
//        for(int i = 0; i < refs.size(); i++) {
//            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());
//
//            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));
//
//            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));
//
//            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));
//
//            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));
//
//            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));
//
//            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));
//
//            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
//        }
//
//        frontJournal.clickEditSearchParamsButton();

        frontJournal.fillCardNumberInput("8888888888888888");

        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));

        frontJournal.clickContinueButton();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("0");

        frontJournal.clickEditSearchParamsButton();

        frontJournal.fillCardNumberInput(confVars.PHYS_CARD_NUMBER);

        frontJournal.clickContinueButton();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for(int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }
    }

    @Test(groups = "FrontJournal", priority = 70, description = "Физлицо. Поиск заявок по ID в ЕКБ")
    public void searchClaimsInFrontJournalByIdEkbPhys() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());


        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(0), "WAVE","IDENTPHYS", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(1), "WAVE","UNIPACKSAS", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -2),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.clickOnlyMyClaimsRadiobutton();

        frontJournal.clickChooseClientRadiobutton();

        frontJournal.clickSearchByPhysIdEkbButton();

        frontJournal.fillIdEkbInput(confVars.ID_EKB);

        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));

        frontJournal.clickContinueButton();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for(int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }
    }

    @Test(groups = "FrontJournal", priority = 80, description = "Физлицо. Поиск заявок по ИНН")
    public void searchClaimsInFrontJournalByInnPhys() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());


        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(0), "WAVE","IDENTPHYS", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(1), "WAVE","UNIPACKSAS", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -2),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.clickOnlyMyClaimsRadiobutton();

        frontJournal.clickChooseClientRadiobutton();

        frontJournal.clickSearchByPhysInnButton();

        frontJournal.fillInnInput(confVars.INN);

        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));

        frontJournal.clickContinueButton();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for(int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }
    }

    @Test(groups = "FrontJournal", priority = 90, description = "Физлицо. Поиск заявок по номеру телефона")
    public void searchClaimsInFrontJournalByPhonePhys() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());


        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(0), "WAVE","IDENTPHYS", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(1), "WAVE","UNIPACKSAS", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -2),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.clickOnlyMyClaimsRadiobutton();

        frontJournal.clickChooseClientRadiobutton();

        frontJournal.fillPhoneInput(confVars.PHONE_NUMBER);

        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));

        frontJournal.clickContinueButton();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for(int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }
    }

    @Test(groups = "FrontJournal", priority = 100, description = "Физлицо. Поиск заявок по серии и номеру документа")
    public void searchClaimsInFrontJournalByDocSeriesAndNumberPhys() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());


        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(0), "WAVE","IDENTPHYS", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(1), "WAVE","UNIPACKSAS", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -2),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.clickOnlyMyClaimsRadiobutton();

        frontJournal.clickChooseClientRadiobutton();

        frontJournal.clickSearchByPhysDocSeriesAndNumberButton();

        frontJournal.fillDocSeriesInput(confVars.DOC_SERIES);

        frontJournal.fillDocNumberInput(confVars.DOC_NUMBER);

        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));

        frontJournal.clickContinueButton();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for(int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }

        frontJournal.clickEditSearchParamsButton();

        frontJournal.fillDocSeriesInput("ьч");

        frontJournal.fillDocNumberInput("100000");

        frontJournal.clickContinueButton();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("0");

        frontJournal.clickEditSearchParamsButton();

        frontJournal.fillDocSeriesInput("");

        frontJournal.fillDocNumberInput(confVars.DOC_NUMBER);

        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));

        frontJournal.clickContinueButton();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for(int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }
    }

    @Test(groups = "FrontJournal", priority = 110, description = "Физлицо. Поиск заявок по ФИО и ДР")
    public void searchClaimsInFrontJournalByFioAndBirthdayPhys() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(0), "WAVE","IDENTPHYS", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(1), "WAVE","UNIPACKSAS", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -2),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.clickOnlyMyClaimsRadiobutton();

        frontJournal.clickChooseClientRadiobutton();

        frontJournal.clickSearchByPhysFioAndBirthdayButton();

        frontJournal.fillLastNameInput(confVars.LAST_NAME);

        frontJournal.fillFirstNameInput(confVars.FIRST_NAME);

        frontJournal.fillPatronymicInput(confVars.PATRONYMIC);

        frontJournal.setBirthdate(confVars.BIRTHDATE);

        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));

        frontJournal.clickContinueButton();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for (int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }
    }

    @Test(groups = "FrontJournal", priority = 120, description = "Юрлицо. Поиск заявок по полному номеру карты")
    public void searchJurFaceClaimsInFrontJournalByCardNumber() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addJurUnfinishedClaims(
            refs.get(0), "P48", "KOPILKA", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addJurUnfinishedClaims(
            refs.get(1), "DIINT", "CASHCHARGE", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -2),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.clickOnlyMyClaimsRadiobutton();

        frontJournal.clickChooseClientRadiobutton();

        frontJournal.clickSelectJurTabButton();

        frontJournal.clickSearchByJurCardNumberButton();

        frontJournal.fillCardNumberInput(confVars.PHYS_CARD_NUMBER);

        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));

        frontJournal.clickContinueButton();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for (int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }
    }

    @Test(groups = "FrontJournal", priority = 130, description = "Юрлицо. Поиск заявок ЧП по 8 последним цифрам номера карты")
    public void searchPrivateEnterpriseClaimsInFrontJournalByNotFullCardNumber() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addPrivateEnterpriseUnfinishedClaims(
            refs.get(0), "P48", "KOPILKA", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addPrivateEnterpriseUnfinishedClaims(
            refs.get(1), "DIINT", "CASHCHARGE", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -2),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.clickOnlyMyClaimsRadiobutton();

        frontJournal.clickChooseClientRadiobutton();

        frontJournal.clickSelectJurTabButton();

        frontJournal.clickSearchByJurCardNumberButton();

        frontJournal.fillCardNumberInput(confVars.PRIVATE_ENTERPRISE_CARD_NUMBER.substring(8));

        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));

        frontJournal.clickContinueButton();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for (int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }
    }

    @Test(groups = "FrontJournal", priority = 140, description = "Юрлицо. Поиск заявок по номеру телефона")
    public void searchJurFaceClaimsInFrontJournalByPhoneNumber() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addJurUnfinishedClaims(
            refs.get(0), "P48", "KOPILKA", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addJurUnfinishedClaims(
            refs.get(1), "DIINT", "CASHCHARGE", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -2),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.clickOnlyMyClaimsRadiobutton();

        frontJournal.clickChooseClientRadiobutton();

        frontJournal.clickSelectJurTabButton();

        frontJournal.fillPhoneInput(confVars.PHONE_NUMBER);

        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));

        frontJournal.clickContinueButton();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for (int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }
    }

    @Test(groups = "FrontJournal", priority = 150, description = "Юрлицо. Поиск заявок по ЕКБ ID")
    public void searchJurFaceClaimsInFrontJournalByIdEkb() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addJurUnfinishedClaims(
            refs.get(0), "P48", "KOPILKA", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addJurUnfinishedClaims(
            refs.get(1), "DIINT", "CASHCHARGE", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -2),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.clickOnlyMyClaimsRadiobutton();

        frontJournal.clickChooseClientRadiobutton();

        frontJournal.clickSelectJurTabButton();

        frontJournal.clickSearchByJurIdEkbButton();

        frontJournal.fillIdEkbInput(confVars.JUR_ID_EKB);

        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));

        frontJournal.clickContinueButton();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for (int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }
    }

    @Test(groups = "FrontJournal", priority = 160, description = "Юрлицо. Поиск заявок по ОКПО")
    public void searchJurFaceClaimsInFrontJournalByOkpo() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addJurUnfinishedClaims(
            refs.get(0), "P48", "KOPILKA", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addJurUnfinishedClaims(
            refs.get(1), "DIINT", "CASHCHARGE", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -2),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.clickOnlyMyClaimsRadiobutton();

        frontJournal.clickChooseClientRadiobutton();

        frontJournal.clickSelectJurTabButton();

        frontJournal.clickSearchByJurInnOkpoButton();

        frontJournal.fillInnInput(confVars.JUR_OKPO);

        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));

        frontJournal.clickContinueButton();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for (int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }
    }

    @Test(groups = "FrontJournal", priority = 170, description = "Юрлицо. Поиск заявок ЧП по ИНН")
    public void searchPrivateEnterpriseClaimsInFrontJournalByInn() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addPrivateEnterpriseUnfinishedClaims(
            refs.get(0), "P48", "KOPILKA", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addPrivateEnterpriseUnfinishedClaims(
            refs.get(1), "DIINT", "CASHCHARGE", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -2),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.clickOnlyMyClaimsRadiobutton();

        frontJournal.clickChooseClientRadiobutton();

        frontJournal.clickSelectJurTabButton();

        frontJournal.clickSearchByJurInnOkpoButton();

        frontJournal.fillInnInput(confVars.PRIVATE_ENTERPRISE_INN);

        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));

        frontJournal.clickContinueButton();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for (int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }
    }

    @Test(groups = "FrontJournal", priority = 180, description = "Юрлицо. Поиск заявок по расчетному счету")
    public void searchPrivateEnterpriseClaimsInFrontJournalByPaymentAccount() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addPrivateEnterpriseUnfinishedClaims(
            refs.get(0), "P48", "KOPILKA", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addPrivateEnterpriseUnfinishedClaims(
            refs.get(1), "DIINT", "CASHCHARGE", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -2),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.clickOnlyMyClaimsRadiobutton();

        frontJournal.clickChooseClientRadiobutton();

        frontJournal.clickSelectJurTabButton();

        frontJournal.clickSearchByJurPaymentAccountButton();

        frontJournal.fillPaymentAccountInput(confVars.PRIVATE_ENTERPRISE_PAYMENT_ACCOUNT);

        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));

        frontJournal.clickContinueButton();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for (int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }
    }

    @Test(groups = "FrontJournal", priority = 190, description = "Юрлицо. Поиск заявок по ФИО и ДР")
    public void searchJurFaceClaimsInFrontJournalByFioAndBirthday() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -8)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addJurUnfinishedClaims(
            refs.get(0), "P48", "KOPILKA", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addJurUnfinishedClaims(
            refs.get(1), "DIINT", "CASHCHARGE", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -2),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.clickOnlyMyClaimsRadiobutton();

        frontJournal.clickChooseClientRadiobutton();

        frontJournal.clickSelectJurTabButton();

        frontJournal.clickSearchByJurFioAndBirthdayButton();

        frontJournal.fillLastNameInput(confVars.LAST_NAME);

        frontJournal.fillFirstNameInput(confVars.FIRST_NAME);

        frontJournal.fillPatronymicInput(confVars.PATRONYMIC);

        frontJournal.setBirthdate(confVars.BIRTHDATE);

        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));

        frontJournal.clickContinueButton();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for (int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString());

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }
    }
}
