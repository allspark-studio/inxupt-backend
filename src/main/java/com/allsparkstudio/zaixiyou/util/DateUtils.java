package com.allsparkstudio.zaixiyou.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转换相关工具类
 *
 * @author wzr
 * @date 2023/7/19
 */
public class DateUtils {
    /**
     * 根据时间转换为时间戳
     *
     * @param date
     * @param timestampType 转换类型 0毫秒 1秒
     * @return
     */
    public long getTimeStamp(Date date, int timestampType) {
        long times = date.getTime();
        if (timestampType == 1) {
            times = times / 1000L;
        }
        return times;
    }

    /**
     * 时间戳转时间
     *
     * @param timestamp
     * @param timestampType 时间戳格式 0毫秒 1秒
     * @return
     */
    public Date getDateTime(long timestamp, int timestampType) {
        if (timestampType == 1) {
            //如果时间戳格式是秒，需要江时间戳变为毫秒
            timestamp = timestamp * 1000L;
        }
        Date dateTime = new Date(timestamp);
        return dateTime;

    }

    /**
     * 格式化传入的时间，将时间转化为指定格式字符串
     *
     * @param date
     * @param format 时间格式，如：yyyy-MM-dd HH:mm:ss SSS 或 yyyy年MM月dd日 HH:mm:ss
     * @return
     */
    public String getDateTimeString(Date date, String format) {
        if (format == null || format.length() <= 0) {
            return null;
        }
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String timeString = sdf.format(date);
        return timeString;
    }

    /**
     * 格式化传入的时间戳，将时间戳转化为指定格式字符串
     *
     * @param timestamp
     * @param format        时间格式，如：yyyy-MM-dd HH:mm:ss SSS 或 yyyy年MM月dd日 HH:mm:ss     *
     * @param timestampType 时间戳格式 0毫秒 1秒
     * @return
     */
    public String getTimeStampString(long timestamp, String format, int timestampType) {
        if (format == null || format.length() <= 0) {
            return null;
        }
        if (timestampType == 1) {
            //如果时间戳格式是秒，需要江时间戳变为毫秒
            timestamp = timestamp * 1000L;
        }
        Date dateTime = new Date(timestamp);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String timeString = sdf.format(dateTime);
        return timeString;
    }

    /**
     * 将时间换换为文字说明
     *
     * @param publishTime
     * @return
     */
    public static String formatPublishTime(Date publishTime) {
        Date now = new Date();
        long minutes = (now.getTime() - publishTime.getTime()) / (1000 * 60);

        if (minutes < 3) {
            return "刚刚";
        } else if (minutes < 60) {
            return minutes + "分钟前";
        } else if (isSameDay(publishTime, now)) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            return "今天 " + formatter.format(publishTime);
        } else if (isYesterday(publishTime, now)) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            return "昨天 " + formatter.format(publishTime);
        } else if (isWithinOneYear(publishTime, now)) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM.dd");
            return formatter.format(publishTime);
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
            return formatter.format(publishTime);
        }
    }

    private static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(date1).equals(formatter.format(date2));
    }

    private static boolean isYesterday(Date date1, Date date2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String date1Str = formatter.format(date1);
        String date2Str = formatter.format(date2);
        return (Long.parseLong(date1Str) + 1) == Long.parseLong(date2Str);
    }

    private static boolean isWithinOneYear(Date date1, Date date2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String year1 = formatter.format(date1);
        String year2 = formatter.format(date2);
        return year1.equals(year2);
    }
}
