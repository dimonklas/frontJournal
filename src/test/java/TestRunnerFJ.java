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

    @Description("Поиск заявки по референсу")
    @Test(groups = "FrontJournal")
    public void searchClaimsInFrontJournalByReference() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -2));

        Long ref = utils.generateClaimRef();

        MongoDB.INSTANCE.addUnfinishedClaims(
            ref,"IDENTPHYS", utils.getCurrentDate("yyyy-MM-dd HH:mm"), confVars.OPER_LDAP,
            confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.fillProdRefInput(ref.toString());

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("1");

        Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(ref.toString(), "wave");

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

    @Description("Поиск заявки по деталям")
    @Test(groups = "FrontJournal")
    public void searchClaimsInFrontJournalByDetails() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -2));

        Long ref = utils.generateClaimRef();

        restUtils.addClaim(
            utils.getCurrentDate("yyyy-MM-dd HH:mm:ss.S"), ref.toString(), confVars.OPER_LDAP
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.fillDetailsInput("нотрдамиус");

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("1");

        Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(ref.toString(), "wave");

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

    @Description("Поиск заявки по операции")
    @Test(groups = "FrontJournal")
    public void searchClaimsInFrontJournalByOperation() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -2));

        Long ref = utils.generateClaimRef();

        MongoDB.INSTANCE.addUnfinishedClaims(
            ref,"UNIPACKSAS", utils.getCurrentDate("yyyy-MM-dd HH:mm"), confVars.OPER_LDAP,
            confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.fillOperationInput("универсальная");

        frontJournal.clickUnipacksasLabel();

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("1");

        Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(ref.toString(), "wave");

        assertThat(frontJournal.getClaimDate()).isEqualTo(claimData.get("date"));

        assertThat(frontJournal.getClaimName()).isEqualTo(claimData.get("name"));

        assertThat(frontJournal.getClaimFio().toUpperCase()).isEqualTo(claimData.get("fio"));

        assertThat(frontJournal.getClaimInn()).isEqualTo(claimData.get("inn"));

        assertThat(frontJournal.getClaimLdap()).isEqualTo(claimData.get("ldap"));

        assertThat(frontJournal.getClaimState()).isEqualTo(claimData.get("state"));

        assertThat(frontJournal.getClaimDetails()).isEqualTo(claimData.get("details"));

    }

    @Description("Поиск заявки по операции и состоянию")
    @Test(groups = "FrontJournal")
    public void searchClaimsInFrontJournalByOperationAndState() throws Exception {

        MongoDB.INSTANCE.deleteClaimsOlderDate(confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -2));

        Long ref = utils.generateClaimRef();

        MongoDB.INSTANCE.addUnfinishedClaims(
            ref,"UNIPACKSAS", utils.getCurrentDate("yyyy-MM-dd HH:mm"), confVars.OPER_LDAP,
            confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.fillOperationInput("универсальная");

        frontJournal.clickUnipacksasLabel();

        frontJournal.selectOperationState(OperationState.NEW.name());

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("1");

        Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(ref.toString(), "wave");

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

    @Description("Поиск заявок за несколько дней")
    @Test(groups = "FrontJournal")
    public void searchClaimsInSeveralDays() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -2)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());


        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(0),"IDENTPHYS", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(1),"UNIPACKSAS", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -24),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.setStartSearchDate(utils.changeCurrentDate("d-MM", 5, -1));

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for(int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString(), "wave");

            assertThat(frontJournal.getClaimDate(i)).isEqualTo(claimData.get("date"));

            assertThat(frontJournal.getClaimName(i)).isEqualTo(claimData.get("name"));

            assertThat(frontJournal.getClaimFio(i).toUpperCase()).isEqualTo(claimData.get("fio"));

            assertThat(frontJournal.getClaimInn(i)).isEqualTo(claimData.get("inn"));

            assertThat(frontJournal.getClaimLdap(i)).isEqualTo(claimData.get("ldap"));

            assertThat(frontJournal.getClaimState(i)).isEqualTo(claimData.get("state"));

            assertThat(frontJournal.getClaimDetails(i)).isEqualTo(claimData.get("details"));
        }
    }

    @Description("Поиск всех заявок за один день")
    @Test(groups = "FrontJournal")
    public void searchAllClaimsInOneDay() throws Exception {
        MongoDB.INSTANCE.deleteClaimsOlderDate(
            confVars.OPER_LDAP, utils.changeCurrentDate("yyyy-MM-dd", 5, -2)
        );

        List<Long> refs = new LinkedList<>();
        refs.add(utils.generateClaimRef());


        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(0),"IDENTPHYS", utils.getCurrentDate("yyyy-MM-dd HH:mm"),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        refs.add(utils.generateClaimRef());

        MongoDB.INSTANCE.addUnfinishedClaims(
            refs.get(1),"UNIPACKSAS", utils.changeCurrentDate("yyyy-MM-dd HH:mm", 11, -2),
            confVars.OPER_LDAP, confVars.FRONT_JOURNAL_URL
        );

        frontJournal = page(FrontJournalPage.class);

        frontJournal.setStartSearchDate(utils.getCurrentDate("d-MM"));

        frontJournal.clickSearchButton();

        frontJournal.waitingForSearchResults("2");

        for(int i = 0; i < refs.size(); i++) {
            Map<String, String> claimData = dataBaseUtils.getClaimsDataFromDataBase(refs.get(i).toString(), "wave");

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
