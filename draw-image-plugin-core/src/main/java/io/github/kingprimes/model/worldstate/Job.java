package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kingprimes.model.RewardPool;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 集团任务详情
 * <p>表示集团提供的具体任务信息，如赏金任务的详细信息</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class Job {
    /**
     * 任务类型
     * <p>任务的类型路径标识符</p>
     */
    @JsonProperty("jobType")
    private String type;

    /**
     * 任务描述
     * <p>任务的描述信息</p>
     */
    @JsonProperty("desc")
    private String desc;

    /**
     * 任务奖励
     * <p>完成任务后可获得的奖励路径</p>
     * 获取具体奖励数据请通过{@link #rewardPool}获取
     */
    @JsonProperty("rewards")
    private String rewards;

    /**
     * 奖励池 {@link RewardPool}
     * <p>任务奖励的详细信息</p>
     */
    @JsonProperty("rewardPool")
    private RewardPool rewardPool;

    /**
     * 段位限制
     * <p>执行该任务需要的最低段位要求</p>
     */
    @JsonProperty("masteryReq")
    private Integer masteryReq;

    /**
     * 敌人 最小等级
     * <p>任务中出现敌人的最低等级</p>
     */
    @JsonProperty("minEnemyLevel")
    private Integer minLevel;

    /**
     * 敌人 最大等级
     * <p>任务中出现敌人的最高等级</p>
     */
    @JsonProperty("maxEnemyLevel")
    private Integer maxLevel;

    /**
     * 任务奖励 XP
     * <p>完成任务各阶段可获得的经验值列表</p>
     */
    @JsonProperty("xpAmounts")
    private List<Integer> xpAmounts;

    /**
     * 是否为无尽任务
     * <p>true表示该任务为无尽模式任务</p>
     */
    @JsonProperty("endless")
    private Boolean endless;

    /**
     * 地点标签
     * <p>任务地点的标识标签</p>
     */
    @JsonProperty("locationTag")
    private String locationTag;

    /**
     * 是否为保险库任务
     * <p>true表示该任务为保险库相关任务</p>
     */
    @JsonProperty("isVault")
    private Boolean isVault;
}
