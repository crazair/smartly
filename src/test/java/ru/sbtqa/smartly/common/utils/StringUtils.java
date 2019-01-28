package ru.sbtqa.smartly.common.utils;

import org.apache.log4j.Logger;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Утилитный класс для работы со строками в атотестах */
public class StringUtils {

    public static final Logger LOG = Logger.getRootLogger();
    public final static String IPADDRESS_PATTERN = "([0-9]{1,3}[.]){3}[0-9]{1,3}";
    public final static String ZERO_IP = "0.0.0.0";

    private StringUtils() throws IllegalAccessException {
        throw new IllegalAccessException("StringUtils is utility class!");
    }

    /** Метод берёт строковое значение переводит в цифровое, добавляет дельту и возвращает в сроке :-) */
    public static String getStringWithDelta(String value, int delta) {
        return "" + (Integer.parseInt(value) + delta);
    }
    /** Метод удаляет пробелы из входной строки, пребразует в double, добавляет дельту и возвращает в сроке */
    public static String getStringWithDelta(String value, double delta) {
        double sumDouble = Double.valueOf(value.replaceAll("\\s",""));
        return Double.toString(sumDouble + delta);
    }

    /** Метод возращает сумму с разделителем тысячных разрядов */
    public static String getAmountWithDelimiter(String amount) {
            Double d = Double.parseDouble(amount.replace(',','.'));
            return String.format(Locale.FRENCH,"%,.2f",d).replace("\u00A0", " ");
    }

    /** Метод, который ищет подстроку, удовлетворяющую регулярному выражению */
    public static String findIpIntoUrl(String url) {
        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(url);
        return matcher.find() ? matcher.group() : ZERO_IP;
    }
}
