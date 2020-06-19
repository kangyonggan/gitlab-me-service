package com.kangyonggan.gitlab.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author kyg
 */
public final class DateUtil {

    private static final ZoneId ZONE_ID = ZoneId.systemDefault();

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_FORMAT);

    /**
     * 日期解析
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date) throws ParseException {
        return parseDate(date, DEFAULT_FORMAT);
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
        return Date.from(localDateTime.atZone(ZONE_ID).toInstant());
    }

    /**
     * 转为本地时间
     *
     * @param date
     * @return
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZONE_ID);
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
        return LocalDate.parse(date, DATE_FORMATTER).plusDays(daysToAdd).format(DATE_FORMATTER);
    }

    /**
     * 在当前日期上加天数
     *
     * @param daysToAdd
     * @return
     */
    public static String plusDays(long daysToAdd) {
        return LocalDate.now().plusDays(daysToAdd).format(DATE_FORMATTER);
    }

    /**
     * 在当前日期上加周数
     *
     * @param weeksToAdd
     * @return
     */
    public static String plusWeeks(long weeksToAdd) {
        return LocalDate.now().plusWeeks(weeksToAdd).format(DATE_FORMATTER);
    }

    /**
     * 在当前日期上加月数
     *
     * @param monthsToAdd
     * @return
     */
    public static String plusMonths(long monthsToAdd) {
        return LocalDate.now().plusMonths(monthsToAdd).format(DATE_FORMATTER);
    }

    /**
     * 在当前日期上加年数
     *
     * @param yearsToAdd
     * @return
     */
    public static String plusYears(long yearsToAdd) {
        return LocalDate.now().plusYears(yearsToAdd).format(DATE_FORMATTER);
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
