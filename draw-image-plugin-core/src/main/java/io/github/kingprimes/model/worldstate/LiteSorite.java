package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kingprimes.model.enums.BossEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 执刑官猎杀
 * <p>表示游戏中的执刑官猎杀任务，包含特定的Boss和一系列任务</p>
 * <p>该类继承 {@link BastWorldState} 基类</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class LiteSorite extends BastWorldState {
    /**
     * 奖励
     * <p>完成执刑官猎杀后可获得的奖励路径</p>
     */
    @JsonProperty("Reward")
    private String reward;

    /**
     * 种子
     * <p>用于生成任务的随机种子值</p>
     */
    @JsonProperty("Seed")
    private Integer seed;

    /**
     * Boss
     * <p>执刑官猎杀中的主要Boss</p>
     */
    @JsonProperty("Boss")
    private BossEnum boss;

    /**
     * 任务列表
     * {@link Mission}
     * <p>需要完成的任务列表，包含任务类型和节点信息</p>
     */
    @JsonProperty("Missions")
    private List<Mission> missions;

    /**
     * 获取Boss名称
     *
     * @return Boss的显示名称
     */
    @JsonIgnore
    public String getBoss() {
        return boss.getName();
    }
}
