package ru.sbtqa.smartly.common.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

/**
 * Утилитный класс для работы с датами в автотестах
 */
public final class DateUtils {

    /** Список празднечных дней в 2018 - 2019 году */
    private static final ArrayList<LocalDate> WEEKEND_DAYS_2018 = new ArrayList<LocalDate>() {{
        add(LocalDate.of(2018, 4, 30));
        add(LocalDate.of(2018, 5, 1));
        add(LocalDate.of(2018, 5, 2));
        add(LocalDate.of(2018, 5, 9));
        add(LocalDate.of(2018, 6, 11));
        add(LocalDate.of(2018, 6, 12));
        add(LocalDate.of(2018, 11,5));
        add(LocalDate.of(2018, 12,31));

        add(LocalDate.of(2019, 1, 1));
        add(LocalDate.of(2019, 1, 2));
        add(LocalDate.of(2019, 1, 3));
        add(LocalDate.of(2019, 1, 4));
        add(LocalDate.of(2019, 1, 7));
        add(LocalDate.of(2019, 1, 8));
        add(LocalDate.of(2019, 3, 8));
        add(LocalDate.of(2018, 5, 1));
        add(LocalDate.of(2019, 5, 2));
        add(LocalDate.of(2019, 5, 3));
        add(LocalDate.of(2019, 5, 9));
        add(LocalDate.of(2019, 5, 10));
        add(LocalDate.of(2019, 6, 12));
        add(LocalDate.of(2019, 11, 4));
    }};

    /** Список "рабочих-выходных" дней в 2018 году */
    private static final ArrayList<LocalDate> NO_WEEKEND_DAYS_2018 = new ArrayList<LocalDate>() {{
        add(LocalDate.of(2018, 4, 28));
        add(LocalDate.of(2018, 6, 9));
        add(LocalDate.of(2018, 12,29));
    }};

    /** Формат даты по-умолчанию */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private DateUtils() {
        throw new IllegalAccessError("DateUtils is utility class!");
    }

    /** Метод возвращаюший текущую дату в формате по-умолчанию */
    public static String getCurrentDate() {
        return LocalDate.now().format(DATE_TIME_FORMATTER);
    }

    /** Метод возвращаюший дату в указанном формате
     * @param date - дата в формате dd.MM.yyyy
     * @param format - новый формат даты
     */
    public static String getDateWithFormat(String date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(date, DATE_TIME_FORMATTER).format(formatter);
    }

    /**
     * Метод возвращаюший дельту от текущей даты + кол-во дней в формате по-умолчанию
     *
     * @param deltaDays - кол-во дней
     */
    public static String getDateWithDeltaDays(int deltaDays) {
        return LocalDate.now().plusDays(deltaDays).format(DATE_TIME_FORMATTER);
    }

    /**
     * Метод возвращаюший дельту от даты + кол-во дней в формате по-умолчанию
     *
     * @param date - дата, от которрой берем дельту
     * @param deltaDays - кол-во дней
     */
    public static String getDateWithDeltaDays(String date, int deltaDays) {
        return LocalDate.parse(date, DATE_TIME_FORMATTER).plusDays(deltaDays).format(DATE_TIME_FORMATTER);
    }

    /**
     * Метод возвращаюший дельту от текущей даты + кол-во месяцев в формате по-умолчанию
     *
     * @param deltaMonths - кол-во месяцев
     */
    public static String getDateWithDeltaMonths(int deltaMonths) {
        return LocalDate.now().plusMonths(deltaMonths).format(DATE_TIME_FORMATTER);
    }

    /**
     * Метод возвращаюший дельту от даты + кол-во месяцев в формате по-умолчанию
     *
     * @param date - дата, от которрой берем дельту
     * @param deltaMonths - кол-во месяцев
     */
    public static String getDateWithDeltaMonths(String date, int deltaMonths) {
        return LocalDate.parse(date, DATE_TIME_FORMATTER).plusMonths(deltaMonths).format(DATE_TIME_FORMATTER);
    }

    /**
     * Метод возвращаюший дельту от даты + кол-во лет в формате по-умолчанию
     *
     * @param date       - дата, от которой берем дельту
     * @param deltaYears - кол-во лет
     */
    public static String getDateWithDeltaYears(String date, int deltaYears) {
        return LocalDate.parse(date, DATE_TIME_FORMATTER).plusYears(deltaYears).format(DATE_TIME_FORMATTER);
    }

    /**
     * Метод возвращаюший дельту от текущей даты + кол-во лет в формате по-умолчанию
     *
     * @param deltaYears - кол-во лет
     */
    public static String getDateWithDeltaYears(int deltaYears) {
        return LocalDate.now().plusYears(deltaYears).format(DATE_TIME_FORMATTER);
    }

    /**
     * Метод возвращаюший дельту от текущей даты + кол-во дней, месяцев, лет в формате по-умолчанию
     *
     * @param deltaDays - кол-во дней
     * @param deltaMonths - кол-во месяцев
     * @param deltaYears - кол-во лет
     */
    public static String getDateWithDelta(int deltaDays, int deltaMonths, int deltaYears) {
        return LocalDate.now().plusDays(deltaDays).plusMonths(deltaMonths).plusYears(deltaYears).
                format(DATE_TIME_FORMATTER);
    }

    /**
     * Метод возвращаюший период(кол-во дней) от двух дат
     *
     * @param startDate дата начала
     * @param endDate   дата окончания
     * @return период между датой начала и датой окончания
     */
    public static String getPeriodDates(String startDate, String endDate) {
        return String.valueOf(ChronoUnit.DAYS.between(LocalDate.parse(startDate, DATE_TIME_FORMATTER),
                LocalDate.parse(endDate, DATE_TIME_FORMATTER)));
    }

    /**
     * Метод проверяет является ли дата в строке выходным и, если является,
     * то возвращает ближайший будущий рабочий день
     */
    public static String ifWeekendDayToNextWorkingDay(String date) {
        return ifWeekendDayToNextWorkingDay(LocalDate.parse(date, DATE_TIME_FORMATTER))
                .format(DATE_TIME_FORMATTER);
    }

    /**
     * Метод проверяет является ли дата выходным и, если является,
     * то возвращает ближайший будущий рабочий день
     */
    public static LocalDate ifWeekendDayToNextWorkingDay(LocalDate localDate) {
        boolean isWeekendDay = isWeekendDate(localDate);

        if(!isWeekendDay) return localDate;

        return ifWeekendDayToNextWorkingDay(localDate.plusDays(1));
    }

    /**
     * Метод проверяет является ли дата в строке рабочим днём и, если является,
     * то возвращает ближайший будущий выходной день
     */
    public static String ifWorkingDayToNextWeekendDay(String date) {
        return ifWorkingDayToNextWeekendDay(LocalDate.parse(date, DATE_TIME_FORMATTER))
                .format(DATE_TIME_FORMATTER);
    }

    /**
     * Метод проверяет является ли дата рабочим днём и, если является,
     * то возвращает ближайший будущий выходной день
     */
    public static LocalDate ifWorkingDayToNextWeekendDay(LocalDate localDate) {
        boolean isWeekendDay = isWeekendDate(localDate);

        if(isWeekendDay) return localDate;

        return ifWorkingDayToNextWeekendDay(localDate.plusDays(1));
    }

    /**
     * Метод проверяет является ли дата выходным и, если является,
     * то возвращает ближайший предыдущий рабочий день
     */
    public static LocalDate ifWeekendDateToPreviousWorkingDate(LocalDate localDate){
        boolean isWeekendDay = isWeekendDate(localDate);
        if(!isWeekendDay) return localDate;

        return ifWeekendDateToPreviousWorkingDate(localDate.minusDays(1));
    }

    /**
     * Метод проверяет является ли дата выходным и, если является,
     * то возвращает ближайший предыдущий рабочий день
     */
    public static String ifWeekendDateToPreviousWorkingDate(String date){
        return ifWeekendDateToPreviousWorkingDate(LocalDate.parse(date, DATE_TIME_FORMATTER))
                .format(DATE_TIME_FORMATTER);
    }

    /**
     * Метод проверяет попадает ли дата на выходной день
     */
    private static boolean isWeekendDate(LocalDate localDate) {
        if(WEEKEND_DAYS_2018.contains(localDate)) return true;
        if(NO_WEEKEND_DAYS_2018.contains(localDate)) return false;

        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        return dayOfWeek == SATURDAY || dayOfWeek == SUNDAY;
    }
}