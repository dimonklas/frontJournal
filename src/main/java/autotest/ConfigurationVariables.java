package autotest;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConfigurationVariables {

    private static final ConfigurationVariables instance;

    private static Logger logger = Logger.getLogger(ConfigurationVariables.class);

    private static String configFilePath = "src/main/resources/testdata/config.properties";
    private static String testDataFilePath = "src/main/resources/testdata/testData.properties";
    private static Properties configurationData = new Properties();
    private static Properties testData = new Properties();

    /****************************************** Конфигурационные данные ***********************************************/
    public String USER_LOGIN = System.getProperty("userLogin");
    public String USER_PASSWORD = System.getProperty("userPassword");
    public String CURRENT_BROWSER = System.getProperty("currentBrowser");

    public String timeout = System.getProperty("selenide.timeout");

    public String BASE_URL = getProperty(configurationData, "baseUrl");
    public String FRONT_JOURNAL_URL = getProperty(configurationData, "frontJournalUrl");
    public String TOTP_URL = getProperty(testData, "totpUrl");

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

    public String locale = System.getProperty("locale");

    public String downloadsDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "downloads" + System.getProperty("file.separator");

    static {
        fillMyProperties(configurationData, configFilePath);
        fillMyProperties(testData, testDataFilePath);

        instance = new ConfigurationVariables();
    }

    private ConfigurationVariables() {

        if (USER_LOGIN == null || USER_LOGIN.equalsIgnoreCase(""))
            USER_LOGIN = getProperty(configurationData, "userLogin");

        if (USER_PASSWORD == null || USER_PASSWORD.equalsIgnoreCase(""))
            USER_PASSWORD = getProperty(configurationData, "userPassword");

        if (CURRENT_BROWSER == null || CURRENT_BROWSER.equalsIgnoreCase(""))
            CURRENT_BROWSER = getProperty(configurationData, "currentBrowser");

        if (timeout == null || timeout.trim().isEmpty())
            timeout = getProperty(configurationData, "timeout");

        if (locale == null || locale.equalsIgnoreCase(""))
            locale = getProperty(configurationData, "locale");

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
        return properties.getProperty(propertyKey);
    }

    public static ConfigurationVariables getInstance() {
        return instance;
    }
}