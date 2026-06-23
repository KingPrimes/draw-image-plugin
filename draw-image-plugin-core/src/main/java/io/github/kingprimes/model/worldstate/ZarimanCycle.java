package io.github.kingprimes.model.worldstate;

import io.github.kingprimes.model.enums.SyndicateEnum;
import io.github.kingprimes.utils.TimeUtils;
import lombok.Getter;

import java.time.Instant;

/**
 * 扎的曼
 *
 * <p>此循环计算方式来自 <a href="https://github.com/WFCD/warframe-worldstate-parser">warframe-worldstate-parser</a></p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Getter
public final class ZarimanCycle implements Cycle {

    private static final long CORPUS_TIME_MILLIS = 1655182800000L; // Corpus 起始时间（毫秒）
    private static final long FULL_CYCLE = 18000000; // 完整周期时长（毫秒）
    private static final long STATE_MAXIMUM = 9000000; // 每个阶段最大持续时间（毫秒）

    /**
     * 开始时间
     */
    private final Instant activation;
    /**
     * 结束时间
     */
    private final Instant expiry;
    /**
     * 当前派系是否为Corpus
     */
    private final boolean isCorpus;
    /**
     * 当前状态 Grineer/Corpus
     */
    private final String state;
    /**
     * 剩余时间
     */
    private final String timeLeft;
    /**
     * 是否已结束
     */
    private final boolean expired;

    /**
     * 构造 扎的曼 信息
     *
     * @param bountiesEndDate <br/><pro>
     *                        1. 获取{@link SyndicateMission#tag}中的{@link SyndicateEnum#ZarimanSyndicate} 数据<br/>
     *                        2. 获取{@link SyndicateMission#expiry} 结束时间类<br/>
     *                        3. 获取{@link DateField#getEpochSecond()} 毫秒时间戳 为构造参数<br/>
     *                        </pro>
     */
    public ZarimanCycle(Instant bountiesEndDate) {
        long now = System.currentTimeMillis();

        // 计算剩余时间
        long bountiesClone = bountiesEndDate.toEpochMilli() - 5000;
        long millisLeft = bountiesClone - now;

        // 计算当前处于哪个周期段
        long cycleTimeElapsed = (((bountiesClone - CORPUS_TIME_MILLIS) % FULL_CYCLE + FULL_CYCLE) % FULL_CYCLE);
        long cycleTimeLeft = FULL_CYCLE - cycleTimeElapsed;
        this.isCorpus = cycleTimeLeft > STATE_MAXIMUM;

        // 设置状态字符串
        this.state = isCorpus ? "Grineer" : "Corpus";

        // 时间格式化处理
        long minutesCoef = 1000 * 60;
        this.expiry = Instant.ofEpochMilli(Math.round((float) (now + millisLeft) / minutesCoef) * minutesCoef);
        this.activation = expiry.minusMillis((int) STATE_MAXIMUM);

        // 时间剩余字符串
        this.timeLeft = TimeUtils.timeDeltaToString(millisLeft);


        this.expired = expiry.isBefore(Instant.now());
    }

}
