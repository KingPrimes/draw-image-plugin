package io.github.kingprimes.utils;

import java.time.*;
import java.time.temporal.TemporalAdjusters;

/**
 * 时间工具类，提供时间计算和格式化功能
 * 包含时间差计算、时区处理和日期获取等功能
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class TimeUtils {
    /**
     * 将毫秒时间差转换为可读的字符串格式
     * 格式为: Xd Xh Xm Xs (分别表示天、小时、分钟、秒)
     *
     * @param millis 时间差(毫秒)
     * @return 格式化后的时间差字符串
     */
    public static String timeDeltaToString(long millis) {
        Duration duration = Duration.ofMillis(Math.abs(millis));

        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        sb.append(seconds).append("s");

        return sb.toString().trim();
    }

    /**
     * 计算两个时间戳之间的分钟差（指定时区）
     *
     * @param startMillis 起始时间戳（毫秒）
     * @param endMillis   结束时间戳（毫秒）
     * @param timezone    时区ID
     * @return 两个时间戳之间的分钟差（绝对值）
     */
    public static long timeDeltaToMinutes(long startMillis, long endMillis, String timezone) {
        // 计算两个时区时间的分钟差（考虑时区偏移和夏令时）
        Duration duration = calculateTimeDelta(startMillis, endMillis, timezone);
        return Math.abs(duration.toMinutes());
    }

    /**
     * 计算两个时间戳之间的分钟差（使用系统默认时区）
     *
     * @param startMillis 起始时间戳（毫秒）
     * @param endMillis   结束时间戳（毫秒）
     * @return 两个时间戳之间的分钟差（绝对值）
     */
    public static long timeDeltaToMinutes(long startMillis, long endMillis) {
        return timeDeltaToMinutes(startMillis, endMillis, TimeZoneUtil.getEffectiveTimeZone());
    }

    /**
     * 计算指定时间戳与当前时间的分钟差
     *
     * @param startMillis 起始时间戳（毫秒）
     * @return 指定时间戳与当前时间的分钟差（绝对值）
     */
    public static long timeDeltaToMinutes(long startMillis) {
        return timeDeltaToMinutes(startMillis, System.currentTimeMillis());
    }

    /**
     * 计算两个时间戳之间的差异，并根据时区返回格式化的字符串结果
     *
     * @param startMillis 起始时间戳（毫秒）
     * @param endMillis   结束时间戳（毫秒）
     * @param timezone    时区ID
     * @return 格式化的时间差字符串
     */
    public static String timeDeltaToString(long startMillis, long endMillis, String timezone) {
        // 计算时区时间差（毫秒）并格式化
        Duration duration = calculateTimeDelta(startMillis, endMillis, timezone);
        long deltaMillis = Math.abs(duration.toMillis());

        // 使用现有的方法格式化时间差
        return timeDeltaToString(deltaMillis);
    }

    /**
     * 根据指定时区计算两个时间戳之间的时间差
     *
     * @param startMillis 起始时间戳（毫秒）
     * @param endMillis   结束时间戳（毫秒）
     * @param timezone    时区ID
     * @return 两个时间戳之间的时间差Duration对象
     */
    private static Duration calculateTimeDelta(long startMillis, long endMillis, String timezone) {
        // 验证时区是否有效，无效则使用系统默认时区
        if (!TimeZoneUtil.isValidTimeZone(timezone)) {
            timezone = TimeZoneUtil.getEffectiveTimeZone();
        }

        // 使用时区参数计算时间差（核心修改）
        ZoneId zoneId = ZoneId.of(timezone);
        ZonedDateTime startZoned = ZonedDateTime.ofInstant(Instant.ofEpochMilli(startMillis), zoneId);
        ZonedDateTime endZoned = ZonedDateTime.ofInstant(Instant.ofEpochMilli(endMillis), zoneId);

        // 计算时区时间差（毫秒）并格式化
        return Duration.between(startZoned, endZoned);
    }

    /**
     * 计算两个时间戳之间的差异，使用系统默认时区
     *
     * @param startMillis 起始时间戳（毫秒）
     * @param endMillis   结束时间戳（毫秒）
     * @return 格式化的时间差字符串
     */
    public static String timeDeltaToString(long startMillis, long endMillis) {
        return timeDeltaToString(startMillis, endMillis, TimeZoneUtil.getEffectiveTimeZone());
    }

    /**
     * 计算指定时间戳与当前时间的差异
     *
     * @param millis   时间戳（毫秒）
     * @param timezone 时区ID
     * @return 格式化的时间差字符串
     */
    public static String timeDeltaToNow(long millis, String timezone) {
        return timeDeltaToString(millis, System.currentTimeMillis(), timezone);
    }

    /**
     * 计算指定时间戳与当前时间的差异，使用系统默认时区
     *
     * @param millis 时间戳（毫秒）
     * @return 格式化的时间差字符串
     */
    public static String timeDeltaToNow(long millis) {
        return timeDeltaToNow(millis, TimeZoneUtil.getEffectiveTimeZone());
    }

    /**
     * 获取一周的第一天（周一）
     *
     * @return 本周第一天的LocalDateTime对象（UTC时区）
     */
    public static LocalDateTime getFirstDayOfWeek() {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        LocalDate firstDay = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return firstDay.atStartOfDay().atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    /**
     * 获取一周的最后一天（周日）
     *
     * @return 本周最后一天的LocalDateTime对象（UTC时区，时间为23:59:59）
     */
    public static LocalDateTime getLastDayOfWeek() {
        LocalDateTime firstDay = getFirstDayOfWeek();
        return firstDay.plusDays(6).withHour(23).withMinute(59).withSecond(59).withNano(0);
    }

    /**
     * 获取当天的开始时间
     *
     * @return 当天开始时间的LocalDateTime对象（UTC时区）
     */
    public static LocalDateTime getStartOfDay() {
        return LocalDate.now(ZoneOffset.UTC).atStartOfDay().atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    /**
     * 获取当天的结束时间
     *
     * @return 当天结束时间的LocalDateTime对象（UTC时区，时间为23:59:59）
     */
    public static LocalDateTime getEndOfDay() {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        return today.atTime(23, 59, 59).atOffset(ZoneOffset.UTC).toLocalDateTime();
    }
}
