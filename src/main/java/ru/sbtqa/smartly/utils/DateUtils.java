package ru.sbtqa.smartly.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.DayOfWeek.*;
import static java.time.temporal.TemporalAdjusters.next;

public final class DateUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private DateUtils() throws IllegalAccessException {
        throw new IllegalAccessException("AllureUtils is utility class!");
    }

    public static String getCurrentDate(){
        return LocalDate.now().format(DATE_TIME_FORMATTER);
    }

    public static String getDateWithDeltaDeys(int deltaDays){
        return LocalDate.now().plusDays(deltaDays).format(DATE_TIME_FORMATTER);
    }

    public static String toNextWorkingDay(String date){
        LocalDate localDate = LocalDate.parse(date, DATE_TIME_FORMATTER);
        return isWeekendDate(localDate)
                ? localDate.with(next(MONDAY)).format(DATE_TIME_FORMATTER)
                : localDate.format(DATE_TIME_FORMATTER);
    }

    public static boolean isWeekendDate(LocalDate localDate) {
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        return dayOfWeek == SATURDAY  || dayOfWeek == SUNDAY;
    }
}
