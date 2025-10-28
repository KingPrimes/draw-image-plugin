package io.github.kingprimes.model.worldstate;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 循环任务
 * <p>该类包含{@link EarthCycle}、{@link CetusCycle}、{@link CambionCycle}、{@link VallisCycle}循环任务的信息</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class AllCycle {
    /**
     * 地球循环
     *
     * @see EarthCycle
     */
    EarthCycle earthCycle;

    /**
     * 夜灵平原
     *
     * @see CetusCycle
     */
    CetusCycle cetusCycle;

    /**
     * 魔胎之境
     *
     * @see CambionCycle
     */
    CambionCycle cambionCycle;

    /**
     * 奥布山谷
     *
     * @see VallisCycle
     */
    VallisCycle vallisCycle;

    /**
     * 扎里曼
     *
     * @see ZarimanCycle
     */
    ZarimanCycle zarimanCycle;
}
