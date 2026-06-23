package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kingprimes.model.enums.FactionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.awt.*;
import java.util.List;

/**
 * 入侵任务
 * <p>表示游戏中的入侵任务，玩家可以通过完成任务支持进攻方或防守方</p>
 * <p>该类继承 {@link BastWorldState} 基类</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class Invasion extends BastWorldState {
    /**
     * 进攻方阵营
     * <p>入侵任务的进攻方阵营，如FC_CORPUS、FC_GRINEER等</p>
     */
    @JsonProperty("Faction")
    private FactionEnum faction;

    /**
     * 防守方阵营
     * <p>入侵任务的防守方阵营，如FC_CORPUS、FC_GRINEER等</p>
     */
    @JsonProperty("DefenderFaction")
    private FactionEnum defenderFaction;

    /**
     * 星图节点
     * <p>入侵任务所在的星图节点，如"SolNode50"</p>
     */
    @JsonProperty("Node")
    private String node;

    /**
     * 当前进度计数
     * <p>入侵任务的当前进度值，负数表示防守方领先，正数表示进攻方领先</p>
     */
    @JsonProperty("Count")
    private Double count;

    /**
     * 目标值
     * <p>完成入侵任务需要达到的目标值</p>
     */
    @JsonProperty("Goal")
    private Double goal;

    /**
     * 本地化标签
     * <p>用于显示入侵任务描述的本地化字符串键</p>
     */
    @JsonProperty("LocTag")
    private String locTag;

    /**
     * 是否完成
     * <p>标识该入侵任务是否已经完成</p>
     */
    @JsonProperty("Completed")
    private Boolean completed;

    /**
     * 链接ID
     * <p>入侵任务链的唯一标识符</p>
     */
    @JsonProperty("ChainID")
    private Id chainID;

    /**
     * 进攻方奖励
     * <p>支持进攻方胜利后可获得的奖励列表</p>
     * <p>{@link Reward}</p>
     */
    @JsonProperty("AttackerReward")
    private List<Reward> attackerReward;

    /**
     * 进攻方任务信息
     * <p>进攻方任务的详细信息</p>
     */
    @JsonProperty("AttackerMissionInfo")
    private MissionInfo attackerMissionInfo;

    /**
     * 防守方奖励
     * <p>支持防守方胜利后可获得的奖励</p>
     * <p>{@link Reward}</p>
     */
    @JsonProperty("DefenderReward")
    private Reward defenderReward;

    /**
     * 防守方任务信息
     * <p>防守方任务的详细信息</p>
     */
    @JsonProperty("DefenderMissionInfo")
    private MissionInfo defenderMissionInfo;

    /**
     * 获取进攻方阵营
     * <p>{@link FactionEnum}</p>
     *
     * @return 进攻方阵营
     */
    @JsonIgnore
    public FactionEnum getFaction() {
        return faction;
    }

    /**
     * 获取进攻方阵营名称
     * <p>{@link FactionEnum#name} </p>
     *
     * @return 进攻方阵营
     */
    @JsonIgnore
    public String getFactionName() {
        return faction.getName();
    }

    /**
     * 获取进攻方阵营颜色
     * <p>{@link FactionEnum#color} </p>
     *
     * @return 进攻方阵营颜色
     */
    @JsonIgnore
    public Color getFactionColor() {
        return faction.getColor();
    }

    /**
     * 获取进攻方阵营图标
     * <p>{@link FactionEnum#icon} </p>
     *
     * @return 进攻方阵营图标
     */
    @JsonIgnore
    public String getFactionIcon() {
        return faction.getIcon();
    }

    /**
     * 获取防守方阵营
     * <p>{@link FactionEnum}</p>
     *
     * @return 防守方阵营
     */
    @JsonIgnore
    public FactionEnum getDefenderFaction() {
        return defenderFaction;
    }

    /**
     * 获取防守方阵营枚举名称
     * <p>{@link FactionEnum#name} </p>
     *
     * @return 进攻方阵营的显示名称 例如："Corpus"
     */
    @JsonIgnore
    public String getDefenderFactionName() {
        return defenderFaction.getName();
    }

    /**
     * 获取防守方阵营颜色
     * <p>{@link FactionEnum#color} </p>
     *
     * @return 防守方阵营颜色
     */
    @JsonIgnore
    public Color getDefenderFactionColor() {
        return defenderFaction.getColor();
    }

    /**
     * 获取防守方阵营图标
     * <p>{@link FactionEnum#icon} </p>
     *
     * @return 防守方阵营图标
     */
    @JsonIgnore
    public String getDefenderFactionIcon() {
        return defenderFaction.getIcon();
    }

    /**
     * 获取任务完成度百分比
     *
     * @return 格式化为两位小数的完成度百分比字符串
     */
    @JsonIgnore
    public String getCompletion() {
        return String.format("%.2f", count / goal);
    }
}
