package io.github.kingprimes.model.worldstate;


import io.github.kingprimes.model.WorldState;
import io.github.kingprimes.utils.TimeUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 双衍王境
 * <p>此循环计算方式来自 <a href="https://github.com/WFCD/warframe-worldstate-parser">warframe-worldstate-parser</a></p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Getter
public final class DuvalierCycle {


    static final long CYCLE_TIME = 36000; // 总周期时长（秒）
    static final long STATE_TIME = 7200;  // 每个阶段持续时间（秒）
    static final List<String> STATES = List.of("悲伤", "恐惧", "喜悦", "愤怒", "嫉妒");


    /**
     * 当前状态 开始时间
     */
    Instant activation;
    /**
     * 当前状态 结束时间
     */
    Instant expiry;
    /**
     * 当前状态
     */
    String state;
    /**
     * 具体内容
     * -- SETTER --
     *  设置可选内容
     *
     */
    @Setter
    List<EndlessXpChoices> choices;
    /**
     * 剩余时间
     */
    String timeLeft;

    /**
     * 构造双衍王境循环
     *
     * @param choices 当前循环可选内容列表 从{@link WorldState#endlessXpChoices} 中获取
     */
    public DuvalierCycle(List<EndlessXpChoices> choices) {
        this.choices = choices;

        long nowSeconds = Instant.now().getEpochSecond();
        long cycleDelta = (nowSeconds - 52) % CYCLE_TIME;

        int stateInd = (int) (cycleDelta / STATE_TIME);
        long stateDelta = cycleDelta % STATE_TIME;
        long untilNext = STATE_TIME - stateDelta;

        this.state = STATES.get(stateInd);

        this.expiry = Instant.now().plus(untilNext, ChronoUnit.SECONDS).truncatedTo(ChronoUnit.SECONDS);
        this.activation = this.expiry.minus(STATE_TIME, ChronoUnit.SECONDS);
        this.timeLeft = TimeUtils.timeDeltaToString(this.expiry.toEpochMilli() - System.currentTimeMillis());
    }

}
