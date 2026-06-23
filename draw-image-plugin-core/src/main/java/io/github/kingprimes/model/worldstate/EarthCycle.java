package io.github.kingprimes.model.worldstate;


import io.github.kingprimes.utils.TimeUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * 地球昼夜循环
 * <p>此循环计算方式来自 <a href="https://github.com/WFCD/warframe-worldstate-parser">warframe-worldstate-parser</a></p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Setter(AccessLevel.NONE)
@Getter
public final class EarthCycle implements Cycle {

    // 常量定义
    private static final long CYCLE_SECONDS = 28800; // 地球周期总时长（秒）
    private static final long DAYTIME_LIMIT = 14400; // 白天最大持续时间（秒）

    // 成员变量
    /**
     * 当前状态 开始时间
     */
    private Instant activation;
    /**
     * 当前状态 结束时间
     */
    private Instant expiry;
    /**
     * 当前状态 是否为白昼
     */
    private boolean isDay;
    /**
     * 当前状态 白昼/夜晚
     */
    private String state;
    /**
     * 剩余时间
     */
    private String timeLeft;
    /**
     * 圆整时间
     */
    private Instant rounded;
    /**
     * 循环开始时间
     */
    private Instant start;
    /**
     * 循环是否已结束
     */
    private boolean expired;


    public EarthCycle() {
        long now = System.currentTimeMillis();
        long nowSeconds = now / 1000;

        long cycleSeconds = nowSeconds % CYCLE_SECONDS;
        this.isDay = cycleSeconds < DAYTIME_LIMIT;

        long secondsLeft = DAYTIME_LIMIT - (cycleSeconds % DAYTIME_LIMIT);
        long millisLeft = secondsLeft * 1000;

        this.expiry = Instant.ofEpochMilli(now + millisLeft);

        long minutesCoef = 1000 * 60;

        this.rounded = Instant.ofEpochMilli(Math.round((float) (now + millisLeft) / minutesCoef) * minutesCoef);

        // 格式化剩余时间
        this.timeLeft = TimeUtils.timeDeltaToString(millisLeft);

        this.state = isDay ? "白昼" : "夜晚";

        // 开始时间为结束时间前 4 小时
        this.start = this.expiry.minus(4, ChronoUnit.HOURS);

        this.activation = this.start;

        this.expired = expiry.isBefore(Instant.now());
    }


}
