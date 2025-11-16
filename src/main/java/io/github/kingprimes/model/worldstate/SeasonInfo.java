package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 电波
 * <p>表示游戏中的电波赛季信息，包括当前赛季、阶段和活跃挑战任务</p>
 * <p>该类继承 {@link BastWorldState} 基类</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SeasonInfo extends BastWorldState {

    /**
     * 集团标签
     * <p>关联的集团标识，如"RadioLegionIntermission13Syndicate"</p>
     */
    @JsonProperty("AffiliationTag")
    private String affiliationTag;

    /**
     * 赛季编号
     * <p>当前电波赛季的编号</p>
     */
    @JsonProperty("Season")
    private Integer season;

    /**
     * 阶段
     * <p>当前赛季的阶段编号</p>
     */
    @JsonProperty("Phase")
    private Integer phase;

    /**
     * 参数
     * <p>赛季相关的参数信息</p>
     */
    @JsonProperty("Params")
    private String params;

    /**
     * 活跃挑战
     * <p>当前激活的挑战任务列表</p>
     */
    @JsonProperty("ActiveChallenges")
    private List<ActiveChallenges> activeChallenges;

    /**
     * 活跃挑战类
     * <p>表示一个具体的活跃挑战任务</p>
     */
    @Data
    @Accessors(chain = true)
    public static class ActiveChallenges {
        /**
         * 挑战ID
         * <p>挑战任务的唯一标识符</p>
         */
        Id _id;

        /**
         * 挑战任务
         * <p>挑战任务的路径标识符</p>
         */
        String challenge;

        /**
         * 任务名称
         * <p>需要通过{@link #challenge} 标识符获取</p>
         */
        String name;

        /**
         * 任务描述
         * <p>需要通过{@link #challenge} 标识符获取</p>
         */
        String description;

        /**
         * 任务声望
         * <p>需要通过{@link #challenge} 标识符获取</p>
         */
        Integer standing;

        /**
         * 任务数量
         * <p>需要通过{@link #challenge} 标识符获取</p>
         */
        Integer required;

        /**
         * 是否为每日任务
         */
        Boolean daily;

        /**
         * 是否为每周任务
         * <p>需要通过{@link #challenge} 标识符获取</p>
         */
        Boolean weekly;

        /**
         * 是否为精英任务
         * <p>需要通过{@link #challenge} 标识符获取</p>
         */
        Boolean elite;

        public String getDescription() {
            return description.replace("|COUNT|", required.toString()).replaceAll("<.*?>","");
        }
    }

}
