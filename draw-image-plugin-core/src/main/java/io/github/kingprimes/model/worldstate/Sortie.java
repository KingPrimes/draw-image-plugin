package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kingprimes.model.enums.BossEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 突击
 * <p>表示游戏中的突击任务，包含特定的Boss和一系列变种任务</p>
 * <p>该类继承 {@link BastWorldState} 基类</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class Sortie extends BastWorldState {

    /**
     * Boss
     * <p>突击任务中的主要Boss</p>
     */
    @JsonProperty("Boss")
    private BossEnum boss;

    /**
     * 奖励
     * <p>完成突击任务后可获得的奖励路径</p>
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
     * 任务列表 {@link Variant}
     * <p>突击任务中的各个任务</p>
     */
    @JsonProperty("Variants")
    private List<Variant> variants;

    /**
     * 是否推特分享
     * <p>true表示该突击任务会在推特上分享</p>
     */
    @JsonProperty("Twitter")
    private Boolean twitter;

    /**
     * 额外掉落
     * <p>额外的掉落物品列表</p>
     */
    @JsonProperty("ExtraDrops")
    private List<String> extraDrops;

    /**
     * 获取Boss名称
     *
     * @return Boss的显示名称 {@link BossEnum#name}
     */
    @JsonIgnore
    public String getBoss() {
        return boss.getName();
    }
}
