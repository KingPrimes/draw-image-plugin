package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * PVP挑战实例
 * <p>表示游戏中的PVP挑战任务实例，包括每日和每周挑战</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class PVPChallengeInstance {
    /**
     * 挑战类型引用ID
     * <p>指向挑战类型的路径标识符</p>
     */
    @JsonProperty("challengeTypeRefID")
    private String challengeTypeRefID;

    /**
     * 开始日期
     * <p>挑战任务的开始时间</p>
     */
    @JsonProperty("startDate")
    private DateField startDate;

    /**
     * 结束日期
     * <p>挑战任务的结束时间</p>
     */
    @JsonProperty("endDate")
    private DateField endDate;

    /**
     * 唯一标识符
     */
    @JsonProperty("_id")
    private BastWorldState.Id _id;

    /**
     * 参数列表
     * <p>挑战任务的参数配置列表</p>
     */
    @JsonProperty("params")
    private List<Param> params;

    /**
     * 是否为生成的挑战
     * <p>true表示该挑战是自动生成的</p>
     */
    @JsonProperty("isGenerated")
    private Boolean isGenerated;

    /**
     * PVP模式
     * <p>挑战适用的PVP模式，如PVPMODE_ALL、PVPMODE_CAPTURETHEFLAG等</p>
     */
    @JsonProperty("PVPMode")
    private String pvpMode;

    /**
     * 子挑战列表
     * <p>包含的子挑战ID列表</p>
     */
    @JsonProperty("subChallenges")
    private List<BastWorldState.Id> subChallenges;

    /**
     * 挑战分类
     * <p>挑战的分类，如PVPChallengeTypeCategory_WEEKLY、PVPChallengeTypeCategory_DAILY等</p>
     */
    @JsonProperty("Category")
    private String category;

    /**
     * 参数类
     * <p>表示挑战任务的参数</p>
     */
    @Data
    @Accessors(chain = true)
    public static class Param {
        /**
         * 参数名称
         * <p>参数的名称，如"ScriptParamValue"</p>
         */
        @JsonProperty("n")
        private String name;

        /**
         * 参数值
         * <p>参数的数值</p>
         */
        @JsonProperty("v")
        private Integer value;
    }
}
