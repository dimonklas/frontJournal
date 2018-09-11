package MethodUtils;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public String getCurrentDate(String format) {
        Locale locale = new Locale("ru");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatDate = new SimpleDateFormat(format, locale);
        return formatDate.format(cal.getTime());
    }

    public void deleteCookie(String cookieName) {
        WebDriverRunner.getWebDriver().manage().deleteCookieNamed(cookieName);
    }

    /**
     * Метод для изменения значения текущей даты
     * @param format - формат даты, например "yyyy-MM-dd"
     * @param timeUnit - параметр для изменения, примеры значений:
     *       14 - Calendar.MILLISECOND
     *       13 - Calendar.SECOND
     *       12 - Calendar.MINUTE
     *       10 - Calendar.HOUR
     *        5 - Calendar.DAY_OF_MONTH
     *        2 - Calendar.MONTH
     *        1 - Calendar.YEAR
     * @param count - количество timeUnit's для изменения
     * @return измененное значение текущей даты в заданом формате
     */
    public static String changeCurrentDate(String format, int timeUnit, int count) {
        Locale locale = new Locale("ru");
        Calendar cal = Calendar.getInstance();
        cal.add(timeUnit, count);
        SimpleDateFormat formatDate = new SimpleDateFormat(format, locale);
        return formatDate.format(cal.getTime());
    }

    /**
     * Метод для изменения переданой даты
     * @param date - дата для изменения
     * @param dateFormat - формат даты
     * @param param - параметр для изменения, примеры значений:
     *       14 - Calendar.MILLISECOND
     *       13 - Calendar.SECOND
     *       12 - Calendar.MINUTE
     *       10 - Calendar.HOUR
     *        5 - Calendar.DAY_OF_MONTH
     *        2 - Calendar.MONTH
     *        1 - Calendar.YEAR
     * @param count - количество timeUnit's для изменения
     * @return измененное значение переданой даты
     * @throws Exception
     */
    public static String changeDate(String date, String dateFormat, int param, int count) throws Exception {
        Locale locale = new Locale("ru");
        SimpleDateFormat formatDate = new SimpleDateFormat(dateFormat, locale);
        Date verifiableDate = formatDate.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(verifiableDate);
        cal.add(param, count);
        return formatDate.format(cal.getTime());
    }

    @Step("Сгенерировать референс заявки")
    public Long generateClaimRef() {
        return Long.parseLong(changeCurrentDate("dMMyyyyHHssS", 5, 0));
    }

    public String getRegex(String pattern, String text) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        m.find();
        return m.group(1);
    }

    public String changeDateFormat(String date, String startFormat, String endFormat) throws ParseException {
        Locale locale = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat(startFormat);
        Date verifiableDate = format.parse(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat(endFormat, locale);
        return dateFormat.format(verifiableDate);
    }

    /**
     * Преобразование Timestamp в дату нужного формата
     * @param date - значение Timestamp
     * @param endFormat - формат в который нужно преобразовать дату
     * @return дата в заданном формате
     * @throws ParseException
     */
    public String changeDateFormatFromTimestamp(String date, String endFormat) throws ParseException {
        Locale locale = new Locale("ru");
        Date verifiableDate = new Date(Long.parseLong(date));
        SimpleDateFormat dateFormat = new SimpleDateFormat(endFormat, locale);
        return dateFormat.format(verifiableDate);
    }
}
