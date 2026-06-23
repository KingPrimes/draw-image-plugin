package io.github.kingprimes.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * 时区工具类，提供时区相关的操作功能
 * 包括时区验证、时间格式化、时区获取等功能
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class TimeZoneUtil {

    private static volatile String cachedEffectiveTimeZone;

    /**
     * 获取系统默认时区
     *
     * @return 系统默认时区ID字符串
     */
    public static String getSystemTimeZone() {
        return ZoneId.systemDefault().toString();
    }

    /**
     * 获取所有可用的时区ID集合
     *
     * @return 包含所有可用时区ID的Set集合
     */
    public static Set<String> getAllTimeZones() {
        return ZoneId.getAvailableZoneIds();
    }

    /**
     * 格式化时间戳为指定时区的时间字符串
     *
     * @param timestampMillis 时间戳（毫秒）
     * @param timeZone        目标时区ID
     * @return 格式化后的时间字符串，格式为"yyyy-MM-dd HH:mm:ss"
     */
    public static String formatTimestamp(Long timestampMillis, String timeZone) {
        if (timestampMillis == null) {
            return null;
        }

        try {
            // 验证时区是否有效，无效则使用UTC作为默认时区
            if (!isValidTimeZone(timeZone)) {
                timeZone = "UTC"; // 默认使用UTC
            }

            // 创建指定时区的时间格式化器
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.of(timeZone));

            // 格式化时间戳并返回字符串
            return formatter.format(Instant.ofEpochMilli(timestampMillis));
        } catch (Exception e) {
            // 发生异常时返回错误提示
            return "Invalid timestamp";
        }
    }

    /**
     * 使用系统默认时区格式化时间戳
     *
     * @param timestampMillis 时间戳（毫秒）
     * @return 格式化后的时间字符串
     */
    public static String formatTimestamp(Long timestampMillis) {
        return formatTimestamp(timestampMillis, getEffectiveTimeZone());
    }

    /**
     * 验证时区ID是否有效
     *
     * @param timeZone 待验证的时区ID
     * @return 如果时区ID有效返回true，否则返回false
     */
    public static boolean isValidTimeZone(String timeZone) {
        return timeZone != null && getAllTimeZones().contains(timeZone);
    }

    /**
     * 获取最终使用的时区
     * 按优先级依次尝试：启动参数设置的时区 -> 系统默认时区 -> UTC
     *
     * @return 有效的时区ID字符串
     */
    public static String getEffectiveTimeZone() {
        String cached = cachedEffectiveTimeZone;
        if (cached != null) return cached;

        String tz = System.getProperty("user.timezone");
        if (!isValidTimeZone(tz)) {
            tz = getSystemTimeZone();
            if (!isValidTimeZone(tz)) {
                tz = "UTC";
            }
        }
        cachedEffectiveTimeZone = tz;
        return tz;
    }

    /**
     * 获取当前时间戳
     *
     * @return 当前时间的毫秒数
     */
    public static Long getTimeStampMillis() {
        return System.currentTimeMillis();
    }
}
