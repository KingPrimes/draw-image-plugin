package io.github.kingprimes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Market 市场 杜卡币数据
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class Ducats {

    @JsonProperty("payload")
    Previous payload;

    /**
     * 银垃圾列表
     */
    @JsonIgnore
    Map<DumpType, List<Ducat>> silverDump;
    /**
     * 金垃圾列表
     */
    @JsonIgnore
    Map<DumpType, List<Ducat>> goldDump;

    public enum DumpType {
        /**
         * 当天
         */
        DAY,
        /**
         * 每小时
         */
        HOUR
    }

    @Data
    @Accessors(chain = true)
    public static class Ducat {
        /**
         * 时间
         */
        @JsonProperty("datetime")
        Instant dateTime;

        /**
         * 杜卡币
         */
        @JsonProperty("ducats")
        Integer ducats;

        /**
         * 1白金=?杜卡币
         */
        @JsonProperty("ducats_per_platinum")
        Double ducatsPerPlatinum;

        /**
         * 1白金=?杜卡币 实时
         */
        @JsonProperty("ducats_per_platinum_wa")
        Double ducatsPerPlatinumWa;

        /**
         * 当前ID
         */
        @JsonProperty("id")
        String id;

        /**
         * 物品Id
         */
        @JsonProperty("item")
        String item;

        /**
         * 中位数
         */
        @JsonProperty("median")
        Integer median;

        /**
         * 平台价值
         */
        @JsonProperty("plat_worth")
        Float platWorth;

        /**
         * 日变动
         */
        @JsonProperty("position_change_day")
        Integer positionChangeDay;

        /**
         * 周变动
         */
        @JsonProperty("position_change_week")
        Integer positionChangeWeek;

        /**
         * 月变动
         */
        @JsonProperty("position_change_month")
        Integer positionChangeMonth;

        /**
         * 存货
         */
        @JsonProperty("volume")
        Integer volume;

        /**
         * 均价
         */
        @JsonProperty("wa_price")
        Double waPrice;
    }

    @Data
    @Accessors(chain = true)
    public static class Previous {
        @JsonProperty("previous_day")
        List<Ducat> previousDay;
        @JsonProperty("previous_hour")
        List<Ducat> previousHour;
    }
}
