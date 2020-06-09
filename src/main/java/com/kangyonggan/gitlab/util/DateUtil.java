package com.kangyonggan.gitlab.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * @author kyg
 */
public final class DateUtil {

    private static ZoneId zoneId = ZoneId.systemDefault();

    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static SimpleDateFormat chinaFormatter = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT+0800' (中国标准时间)", Locale.ENGLISH);

    /**
     * 中国标准时间
     *
     * @param date
     * @return
     */
    public static String toChina(Date date) {
        return chinaFormatter.format(date);
    }

    /**
     * 日期解析
     *
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date, String pattern) throws ParseException {
        return new SimpleDateFormat(pattern).parse(date);
    }

    /**
     * 转为date
     *
     * @param localDateTime
     * @return
     */
    private static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    /**
     * 转为本地时间
     *
     * @param date
     * @return
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), zoneId);
    }

    /**
     * 在知道的时间上加上分钟数
     *
     * @param minutes
     * @return
     */
    public static Date plusMinutes(Date date, long minutes) {
        return toDate(toLocalDateTime(date).plusMinutes(minutes));
    }

    /**
     * 在当前时间上加上分钟数
     *
     * @param minutes
     * @return
     */
    public static Date plusMinutes(long minutes) {
        return toDate(LocalDateTime.now().plusMinutes(minutes));
    }

    /**
     * 在指定日期上加天数
     *
     * @param date
     * @param daysToAdd
     * @return
     */
    public static String plusDays(String date, long daysToAdd) {
        return LocalDate.parse(date, dateFormatter).plusDays(daysToAdd).format(dateFormatter);
    }

    /**
     * 在当前日期上加天数
     *
     * @param daysToAdd
     * @return
     */
    public static String plusDays(long daysToAdd) {
        return LocalDate.now().plusDays(daysToAdd).format(dateFormatter);
    }

    /**
     * 在当前日期上加周数
     *
     * @param weeksToAdd
     * @return
     */
    public static String plusWeeks(long weeksToAdd) {
        return LocalDate.now().plusWeeks(weeksToAdd).format(dateFormatter);
    }

    /**
     * 在当前日期上加月数
     *
     * @param monthsToAdd
     * @return
     */
    public static String plusMonths(long monthsToAdd) {
        return LocalDate.now().plusMonths(monthsToAdd).format(dateFormatter);
    }

    /**
     * 在当前日期上加年数
     *
     * @param yearsToAdd
     * @return
     */
    public static String plusYears(long yearsToAdd) {
        return LocalDate.now().plusYears(yearsToAdd).format(dateFormatter);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

}
