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

    public String POSTGRE_SERVER = getProperty(configurationData, "postgreServer");
    public String POSTGRE_USERNAME = getProperty(configurationData, "postgreUsername");
    public String POSTGRE_PASSWORD = getProperty(configurationData, "postgrePassword");
    public String ORACLE_BASE_URL = getProperty(configurationData, "oracleTestDataBaseURL");
    public String ORACLE_BASE_USER_LOGIN = getProperty(configurationData, "oracleTestDataBaseUserLogin");
    public String ORACLE_BASE_USER_PASSWORD = getProperty(configurationData, "oracleTestDataBaseUserPassword");
    public String MONGO_IP = getProperty(configurationData, "mongoIP");
    public String MONGO_PORT = getProperty(configurationData, "mongoPort");
    public String MONGO_NAME = getProperty(configurationData, "mongoName");
    public String MONGO_USERNAME = getProperty(configurationData, "mongoUsername");
    public String MONGO_PASSWORD = getProperty(configurationData, "mongoPassword");

    /*********************************************** Тестовые данные **************************************************/
    public String OPER_LDAP = getProperty(testData, "operLdap");
    public String MODIFY_RECORD_URL = getProperty(testData, "modifyRecordUrl");

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