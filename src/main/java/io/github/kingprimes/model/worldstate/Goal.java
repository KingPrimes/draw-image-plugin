package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kingprimes.model.enums.FactionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 活动目标
 * <p>表示游戏中的活动目标或事件信息</p>
 * <p>该类继承 {@link BastWorldState} 基类</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class Goal extends BastWorldState {
    /**
     * 节点
     * <p>活动相关的星图节点，如"SolNode129"</p>
     */
    @JsonProperty("Node")
    private String node;

    /**
     * 分数变量
     * <p>用于跟踪活动进度的变量名，如"FissuresClosed"</p>
     */
    @JsonProperty("ScoreVar")
    private String scoreVar;

    /**
     * 分数标签
     * <p>本地化字符串键，用于显示分数描述，如"/Lotus/Language/G1Quests/HeatFissuresEventScore"</p>
     */
    @JsonProperty("ScoreLocTag")
    private String scoreLocTag = "";

    /**
     * 节点分数
     * <p>当前活动的进度分数</p>
     */
    @JsonProperty("Count")
    private Integer count;

    /**
     * 进度百分比
     * <p>活动完成进度的百分比</p>
     */
    @JsonProperty("HealthPct")
    private Double healthPct;

    /**
     * 地区
     * <p>活动涉及的地区列表</p>
     */
    @JsonProperty("Regions")
    private List<Integer> regions;

    /**
     * 描述
     * <p>活动描述的本地化字符串键</p>
     */
    @JsonProperty("Desc")
    private String desc;

    /**
     * 提示
     * <p>活动提示信息的本地化字符串键</p>
     */
    @JsonProperty("ToolTip")
    private String toolTip;

    /**
     * 宽限期
     */
    @JsonProperty("GracePeriod")
    private DateField gracePeriod;

    /**
     * 物品类型
     */
    @JsonProperty("ItemType")
    private String itemType;

    /**
     * 可选任务
     * <p>标识该目标在任务中是否为可选目标</p>
     */
    @JsonProperty("OptionalInMission")
    private Boolean optionalInMission;

    /**
     * 标签
     * <p>活动标签，用于标识活动类型，如"HeatFissure"、"DeimosHub"</p>
     */
    @JsonProperty("Tag")
    private String tag;

    /**
     * 升级ID列表
     * <p>与活动相关的升级项ID列表</p>
     */
    @JsonProperty("UpgradeIds")
    private List<Id> upgradeIds;

    /**
     * 个人任务
     * <p>标识该目标是否为个人任务目标</p>
     */
    @JsonProperty("Personal")
    private Boolean personal;

    /**
     * 社区任务
     * <p>标识该目标是否为社区共同目标</p>
     */
    @JsonProperty("Community")
    private Boolean community;

    /**
     * 目标值
     * <p>完成活动需要达到的目标值</p>
     */
    @JsonProperty("Goal")
    private Integer goal;

    /**
     * 奖励
     * <p>完成活动后可获得的奖励</p>
     */
    @JsonProperty("Reward")
    private Reward reward;

    /**
     * 中期目标
     * <p>活动过程中的阶段性目标值列表</p>
     */
    @JsonProperty("InterimGoals")
    private List<Integer> interimGoals;

    /**
     * 中期奖励
     * <p>达成阶段性目标后可获得的奖励列表</p>
     * <p>{@link Reward}</p>
     */
    @JsonProperty("InterimRewards")
    private List<Reward> interimRewards;

    /**
     * 额外奖励
     * <p>完成活动后可获得的额外奖励</p>
     * <p>{@link Reward}</p>
     */
    @JsonProperty("BonusReward")
    private Reward bonusReward;

    /**
     * 氏族目标
     * <p>氏族共同需要达成的目标值列表</p>
     */
    @JsonProperty("ClanGoal")
    private List<Integer> clanGoal;

    /**
     * 派系
     * <p>活动相关的派系</p>
     */
    @JsonProperty("Faction")
    private FactionEnum faction;

    /**
     * 图标
     * <p>活动图标的路径</p>
     */
    @JsonProperty("Icon")
    private String icon;

    /**
     * 任务键名称
     * <p>任务名称的本地化字符串键</p>
     */
    @JsonProperty("MissionKeyName")
    private String missionKeyName = "";

    /**
     * 成功状态
     * <p>活动是否成功的状态标识</p>
     */
    @JsonProperty("Success")
    private Integer success;
}
