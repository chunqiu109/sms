package com.danmi.sms.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Java 8 的时间工具类
 *
 * @author Rechel
 */
@Slf4j
public class DateUtils {
    /**
     * 默认使用系统当前时区
     */

    public static final String DATE_FORMAT_DS = "yyyyMMdd";
    public static final String MONTH_FORMAT = "yyyyMM";
    public static final String DATE_FORMAT_DS_LINE = "yyyy-MM-dd";
    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String DATE_FORMAT_SECOND = "yyyyMMddHHmmss";

    /**
     * 获取当前时间
     *
     * @param format format
     * @return String
     */
    public static String getCurrentTime(String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime now = LocalDateTime.now();
        return now.format(dateTimeFormatter);
    }

    public static String getCurrentTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DEFAULT);
        LocalDateTime now = LocalDateTime.now();
        return now.format(dateTimeFormatter);
    }

    /**
     * 获取时间的字符串
     *
     * @param format format
     * @param date
     * @return String
     */
    public static String getDateTimeString(Date date, String format) {
        if (StringUtils.isEmpty(format)) {
            format = DATE_FORMAT_DEFAULT;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 获取日期的字符串
     *
     * @param format format
     * @param date
     * @return String
     */
    public static String getDateString(Date date, String format) {
        if (StringUtils.isEmpty(format)) {
            format = DATE_FORMAT_DS_LINE;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * string转换date类型
     */
    public static Date getStringToDate(String date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date parse = null;
        try {
            parse = dateFormat.parse(date);
        } catch (Exception e) {
           log.error("getStringToDate error",e);
        }
        return parse;
    }


    /**
     * 获取昨日时间
     *
     * @param format format
     * @return String
     */
    public static String getYesterday(String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate nowDate = LocalDate.now();
        LocalDate yesterday = nowDate.minusDays(1);
        return yesterday.format(dateTimeFormatter);
    }

    /**
     * 获取一周前的日期
     *
     * @param format format
     * @return String
     */
    public static String getLastWeek(String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate nowDate = LocalDate.now();
        LocalDate lastWeek = nowDate.minusWeeks(1);
        return lastWeek.format(dateTimeFormatter);
    }

    /**
     * 获取一个月前的时间
     *
     * @param format format
     * @return String
     */
    public static String getLastMonth(String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate nowDate = LocalDate.now();
        LocalDate lastMonth = nowDate.minusMonths(1);
        return lastMonth.format(dateTimeFormatter);
    }

    /**
     * 获取一年前的时间
     *
     * @param format format
     * @return String
     */
    public static String getLastYear(String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate nowDate = LocalDate.now();
        LocalDate lastYear = nowDate.minusYears(1);
        return lastYear.format(dateTimeFormatter);
    }

    /**
     * 获取前多少天的日期
     *
     * @param format format
     * @param num    num
     * @return String
     */
    public static String getBeforeSomeDay(String format, int num) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate nowDate = LocalDate.now();
        LocalDate beforeDay = nowDate.minusDays(num);
        return beforeDay.format(dateTimeFormatter);
    }

    /**
     * 获取指定时间的前多少天
     *
     * @param format format
     * @param date   date
     * @param num    num
     * @return String
     */
    public static String getBeforeDayOfDate(String format, String date, int num) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        LocalDate beforeDay = localDate.minusDays(num);
        return beforeDay.format(dateTimeFormatter);
    }

    /**
     * LocalDateTime 转 Date
     *
     * @return String
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone( ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date 转 LocalDateTime
     *
     * @return String
     */
    public static LocalDateTime localDateTimeToDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * LocalDate 转 String
     *
     * @return String
     */
    public static String localDateToString(LocalDate localDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DS_LINE);
        return dateTimeFormatter.format(localDate);

    }

    public static String localDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DS_LINE);
        return dateTimeFormatter.format(localDateTime);

    }

    /**
     * String 转 LocalDate
     *
     * @return String
     */
    public static LocalDate stringToLocalDate(String localDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DS_LINE);
        return  LocalDate.parse(localDate);

    }

    public static LocalDateTime stringToLocalDateTime(String localDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DEFAULT);
        return  LocalDateTime.parse(localDate);

    }

    public static long stringToTimestamp(String localDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DEFAULT);
        LocalDateTime parse = LocalDateTime.parse(localDate);
        return parse.toInstant(ZoneOffset.of("+8")).toEpochMilli();

    }

    public static long localDateTimeToTimestamp(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 获取当天的开始时间  yyyy-MM-dd 00:00:00
     *
     * @param format format
     * @return String
     */
    public static String getDayStartTime(String format, String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        LocalDateTime toDayStart = LocalDateTime.of(localDate, LocalTime.MIN);
        return toDayStart.format(FORMATTER);
    }

    /**
     * 获取当天的结束时间 yyyy-MM-dd 23:59:59
     *
     * @param format format
     * @return String
     */
    public static String getDayEndTime(String format, String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        LocalDateTime toDayStart = LocalDateTime.of(localDate, LocalTime.MAX);
        return toDayStart.format(FORMATTER);
    }

    /**
     * 获取两个时间之间的间隔天数
     *
     * @param startDate yyyyMMdd
     * @param endDate   yyyyMMdd
     * @return long
     */
    public static long getRangeCountOfDate(String startDate, String endDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DS);
        LocalDate startLocalDate = LocalDate.parse(startDate, dateTimeFormatter);
        LocalDate endLocalDate = LocalDate.parse(endDate, dateTimeFormatter);
        return ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
    }

    /**
     * 后期两个时间之间的所有日期 【包含开始时间和结束时间】
     *
     * @param startDate yyyyMMdd
     * @param endDate   yyyyMMdd
     * @return List<String>
     */
    public static List<String> getRangeOfDate(String startDate, String endDate) {
        List<String> range = Lists.newArrayList();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DS);
        LocalDate startLocalDate = LocalDate.parse(startDate, dateTimeFormatter);
        LocalDate endLocalDate = LocalDate.parse(endDate, dateTimeFormatter);
        long count = ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
        if (count < 0) {
            return range;
        }
        range = Stream.iterate(startLocalDate, d -> d.plusDays(1)).limit(count + 1).map(
                s -> s.format(dateTimeFormatter)).collect(Collectors.toList());
        return range;
    }

    /**
     * 计算两个时间相差【*天*时*分*秒】
     * @param str1
     * @param str2
     * @return
     * @throws ParseException
     */
    public static long[] getDistanceTimes(String str1, String str2) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        long[] times = {day, hour, min, sec};
        return times;
    }

}