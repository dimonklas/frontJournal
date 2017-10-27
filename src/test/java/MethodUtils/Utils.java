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
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatDate = new SimpleDateFormat(format);
        return formatDate.format(cal.getTime());
    }

    public void deleteCookie(String cookieName) {
        WebDriverRunner.getWebDriver().manage().deleteCookieNamed(cookieName);
    }

    public static String changeCurrentDate(String format, int timeUnit, int count) {
        Calendar cal = Calendar.getInstance();
        cal.add(timeUnit, count);
        SimpleDateFormat formatDate = new SimpleDateFormat(format);
        return formatDate.format(cal.getTime());
    }

    public static String changeDate(String date, String dateFormat, int param, int count) throws Exception {
        SimpleDateFormat formatDate = new SimpleDateFormat(dateFormat);
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
        SimpleDateFormat format = new SimpleDateFormat(startFormat);
        Date verifiableDate = format.parse(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat(endFormat);
        return dateFormat.format(verifiableDate);
    }

    public String changeDateFormatFromTimestamp(String date, String endFormat) throws ParseException {
        Date verifiableDate = new Date(Long.parseLong(date));
        SimpleDateFormat dateFormat = new SimpleDateFormat(endFormat);
        return dateFormat.format(verifiableDate);
    }
}
