package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kingprimes.utils.TimeUtils;
import io.github.kingprimes.utils.TimeZoneUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

/**
 * 日期字段模型类
 * <p>用于解析和处理从API获取的时间数据</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class DateField {
    /**
     * 包含实际时间数据的内部类
     * <p>对应JSON中的$date对象</p>
     */
    @JsonProperty("$date")
    private D date;

    /**
     * 获取格式化后的时间字符串
     * <p>{@link TimeZoneUtil#formatTimestamp(Long)}</p>
     *
     * @return 格式化后的时间字符串，格式为 yyyy-MM-dd HH:mm:ss
     */
    @JsonIgnore
    public String getTime() {
        // 返回格式化之后的时间戳
        return TimeZoneUtil.formatTimestamp(date.getEpochSecond().toEpochMilli());
    }

    /**
     * 获取剩余时间的可读表示
     *
     * <p>{@link TimeUtils#timeDeltaToString(long)}</p>
     *
     * @return 剩余时间的字符串表示，如 "1d 2h 30m 0s"
     */
    @JsonIgnore
    public String getTimeLeft() {
        return TimeUtils.timeDeltaToString(date.getEpochSecond().toEpochMilli() - System.currentTimeMillis());
    }

    /**
     * 获取时间戳的Instant对象
     *
     * @return 时间戳对应的Instant对象
     */
    @JsonIgnore
    public Instant getEpochSecond() {
        return date.getEpochSecond();
    }

    /**
     * 内部日期数据类
     * <p>用于封装从JSON解析出来的时间数据</p>
     */
    @Data
    public static class D {
        /**
         * 时间戳数值（毫秒）
         * <p>对应JSON中的$numberLong字段</p>
         */
        @JsonProperty("$numberLong")
        private Instant numberLong;

        /**
         * 获取时间戳的Instant对象
         *
         * @return 时间戳对应的Instant对象
         */
        @JsonIgnore
        public Instant getEpochSecond() {
            return numberLong;
        }
    }
}
