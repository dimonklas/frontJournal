package setup;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConfVars {
    private static final ConfVars instance;

    public static Logger logger = Logger.getLogger(ConfVars.class);
    private static String configFilePath = "src/test/resources/testdata/config.properties";
    private static String testDataFilePath = "src/test/resources/testdata/testData.properties";
    private static Properties configurationData = new Properties();
    private static Properties testData = new Properties();

    static {
        fillMyProperties(configurationData, configFilePath);
        fillMyProperties(testData, testDataFilePath);

        instance = getInstance();
    }

    private static class ConfVarsHolder {
        private final static ConfVars instance = new ConfVars();
    }

    /****************************************** Конфигурационные данные ***********************************************/
    public String USER_LOGIN = System.getProperty("userLogin") != null ?
        System.getProperty("userLogin") : getProperty(configurationData, "userLogin");
    public String USER_PASSWORD = System.getProperty("userPassword") != null ?
        System.getProperty("userPassword") : getProperty(configurationData, "userPassword");
    public String CURRENT_BROWSER = System.getProperty("currentBrowser") != null ?
        System.getProperty("currentBrowser") : getProperty(configurationData, "currentBrowser");

    public String BASE_URL = getProperty(configurationData, "baseUrl");
    public String FRONT_JOURNAL_URL = getProperty(configurationData, "frontJournalUrl");

    public String MONGO_IP = getProperty(configurationData, "mongoIP");
    public String MONGO_PORT = getProperty(configurationData, "mongoPort");
    public String MONGO_NAME = getProperty(configurationData, "mongoName");
    public String MONGO_USERNAME = getProperty(configurationData, "mongoUsername");
    public String MONGO_PASSWORD = getProperty(configurationData, "mongoPassword");

    /*********************************************** Тестовые данные **************************************************/
    public String OPER_LDAP = getProperty(testData, "operLdap");
    public String MODIFY_RECORD_URL = getProperty(testData, "modifyRecordUrl");
    public String PHYS_CARD_NUMBER = getProperty(testData, "physCardNumber");
    public String ID_EKB = getProperty(testData, "idEkb");
    public String INN = getProperty(testData, "inn");
    public String PHONE_NUMBER = getProperty(testData, "phoneNumber");
    public String DOC_SERIES = getProperty(testData, "docSeries");
    public String DOC_NUMBER = getProperty(testData, "docNumber");

    public String LAST_NAME = getProperty(testData, "lastName");
    public String FIRST_NAME = getProperty(testData, "firstName");
    public String PATRONYMIC = getProperty(testData, "patronymic");
    public String BIRTHDATE = getProperty(testData, "birthdate");
    public String JUR_ID_EKB = getProperty(testData, "jurIdEkb");
    public String JUR_NAME = getProperty(testData, "jurName");
    public String JUR_INN = getProperty(testData, "jurInn");
    public String JUR_OKPO = getProperty(testData, "jurOkpo");
    public String PRIVATE_ENTERPRISE_CARD_NUMBER = getProperty(testData, "privateEnterpriseCardNumber");
    public String PRIVATE_ENTERPRISE_EKB_ID = getProperty(testData, "privateEnterpriseEkbId");
    public String PRIVATE_ENTERPRISE_NAME = getProperty(testData, "privateEnterpriseName");
    public String PRIVATE_ENTERPRISE_INN = getProperty(testData, "privateEnterpriseInn");
    public String PRIVATE_ENTERPRISE_PAYMENT_ACCOUNT = getProperty(testData, "privateEnterprisePaymentAccount");

    public static ConfVars getInstance() {
        return ConfVarsHolder.instance;
    }

    private static void fillMyProperties(Properties properties, String filePath) {
        InputStreamReader input;
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            input = new InputStreamReader(fileInputStream, "UTF8");

            // считываем свойства
            properties.load(input);
        } catch (java.io.FileNotFoundException e) {
            logger.fatal("Ошибка. Файл config.properties не был найден. " + e);
        } catch (java.io.IOException e) {
            logger.fatal("IO ошибка в пользовательском методе." + e);
        }
    }

    private static String getProperty(Properties properties, String propertyKey) {
        return properties.getProperty(propertyKey).toString();
    }
}