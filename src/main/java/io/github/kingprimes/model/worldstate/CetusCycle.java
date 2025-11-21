package io.github.kingprimes.model.worldstate;

import io.github.kingprimes.model.enums.SyndicateEnum;
import io.github.kingprimes.utils.TimeUtils;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;


@Getter
public final class CetusCycle {
    /**
     * 白昼持续时间（秒）
     */
    private static final long NIGHT_TIME = 3000;

    /**
     * 白昼/夜晚 最大持续时间（毫秒）
     */
    private static final Map<String, Long> MAXIMUMS = new HashMap<>();

    static {
        /*
          白天最大持续时间（毫秒）
         */
        MAXIMUMS.put("白昼", 6000000L);
        /*
          夜晚最大持续时间（毫秒）
         */
        MAXIMUMS.put("夜晚", 3000000L);
    }

    /**
     * 当前时间是否是白昼
     */
    Boolean isDay;
    /**
     * 当前状态结束时间
     */
    Instant expiry;
    /**
     * 当前状态开始时间
     */
    Instant activation;
    /**
     * 白昼/夜晚
     */
    String state;
    /**
     * day/night
     */
    String cycle;

    /**
     * 剩余时间
     */
    String timeLeft;

    /**
     * 获取当前CetusCycle
     *
     * @param bountiesEndDate <br/>
     *                        1. 获取{@link SyndicateMission#tag}中的{@link SyndicateEnum#CetusSyndicate} 数据<br/>
     *                        2. 获取{@link SyndicateMission#expiry}结束时间类<br/>
     *                        3. 获取{@link DateField#getEpochSecond()} 毫秒时间戳 为构造参数
     *
     */
    public CetusCycle(Instant bountiesEndDate) {
        Instant now = Instant.now();
        Instant bountiesClone = bountiesEndDate.truncatedTo(ChronoUnit.SECONDS);

        long millisLeft = Duration.between(now, bountiesClone).toMillis();
        long secondsToNightEnd = Math.round((double) millisLeft / 1000);
        boolean dayTime = secondsToNightEnd > NIGHT_TIME;

        long secondsRemainingInCycle = dayTime ? secondsToNightEnd - NIGHT_TIME : secondsToNightEnd;
        millisLeft = secondsRemainingInCycle * 1000;
        long minutesCof = 1000 * 60;
        Instant expiry = now.plusMillis(Math.round((double) millisLeft / minutesCof) * minutesCof);
        String state = dayTime ? "白昼" : "夜晚";
        String cycle = dayTime ? "day" : "night";

        this.activation = expiry.minusMillis(MAXIMUMS.get(state));
        this.expiry = expiry;
        this.isDay = dayTime;
        this.state = state;
        this.cycle = cycle;
        this.timeLeft = TimeUtils.timeDeltaToString(millisLeft);
    }

}
