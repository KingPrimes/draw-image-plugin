package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 1999 日历季节
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class KnownCalendarSeasons extends BastWorldState {

    @JsonProperty("Days")
    private List<Days> days;

    @JsonProperty("Season")
    private SeasonEnum season;

    @JsonProperty("YearIteration")
    private Integer yearIteration;

    @JsonProperty("Version")
    private Integer version;

    @JsonProperty("UpgradeAvaliabilityRequirements")
    private List<String> upgradeAvaliabilityRequirements;

    @JsonProperty("MonthDays")
    private Map<Integer, List<Days>> monthDays;

    /**
     * 将一年中的第几天转换为自然月日，并按月份日期升序排序
     */
    @JsonIgnore
    public void processDays() {
        if (days == null || days.isEmpty()) return;
        int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        for (Days day : days) {
            int remaining = day.getDay();
            int m = 0;
            while (m < monthDays.length && remaining > monthDays[m]) {
                remaining -= monthDays[m];
                m++;
            }
            day.setMonth(m + 1);
            day.setDay(remaining);
        }
        days.sort(Comparator.comparingInt(Days::getMonth).thenComparingInt(Days::getDay));
    }

    @Getter
    public enum SeasonEnum {
        CST_FALL("秋季"),
        CST_SUMMER("夏季"),
        CST_SPRING("春季"),
        CST_WINTER("冬季");

        private final String name;

        SeasonEnum(String name) {
            this.name = name;
        }
    }

    @Getter
    public enum DaysTypeEnum {
        CET_CHALLENGE("任务"),
        CET_REWARD("奖励"),
        CET_UPGRADE("加成");

        private final String displayName;

        DaysTypeEnum(String displayName) {
            this.displayName = displayName;
        }
    }

    @Data
    @Accessors(chain = true)
    public static class Days {
        @JsonProperty("day")
        private Integer day;

        @JsonProperty("events")
        private List<Events> events;

        @JsonProperty("month")
        private Integer month;
    }

    @Data
    @Accessors(chain = true)
    public static class Events {
        @JsonProperty("type")
        private DaysTypeEnum type;

        @JsonProperty("challenge")
        private String challenge;

        @JsonProperty("reward")
        private String reward;

        @JsonProperty("upgrade")
        private String upgrade;
    }
}
