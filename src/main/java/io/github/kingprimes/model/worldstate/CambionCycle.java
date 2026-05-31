package io.github.kingprimes.model.worldstate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * 魔胎之境
 * <p>该类表示魔胎之境的循环周期信息，包括当前活动状态、剩余时间等</p>
 *
 * <p>此循环计算方式来自 <a href="https://github.com/WFCD/warframe-worldstate-parser">warframe-worldstate-parser</a></p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Setter(AccessLevel.NONE)
@Getter
public final class CambionCycle implements Cycle {
    /**
     * 活动 FASS/VOME
     * <p>表示当前魔胎之境的活动状态，FASS或VOME</p>
     */
    String active;
    /**
     * 剩余时间
     * <p>表示当前活动状态的剩余时间</p>
     */
    String timeLeft;
    /**
     * 结束时间
     * <p>表示当前活动状态的结束时间</p>
     */
    Instant expiry;
    /**
     * 开始时间
     * <p>表示当前活动状态的开始时间</p>
     */
    Instant activation;

    /**
     * 构造函数
     * <p>根据夜灵平原的循环周期初始化魔胎之境的循环周期</p>
     *
     * @param cycle 平原循环 {@link CetusCycle}
     */
    public CambionCycle(CetusCycle cycle) {
        this.activation = cycle.getActivation();
        this.expiry = cycle.getExpiry();
        this.active = cycle.getIsDay() ? "FASS" : "VOME";
        this.timeLeft = cycle.getTimeLeft();
    }
}
