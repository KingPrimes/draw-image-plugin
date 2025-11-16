package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kingprimes.model.enums.MissionTypeEnum;
import io.github.kingprimes.model.enums.ModifierTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 突击 任务模型<br/>
 * 该类使用在突击任务中的任务列表<br/>
 * {@link Sortie}
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class Variant {
    /**
     * 任务类型<br/>
     * {@link MissionTypeEnum}
     */
    @JsonProperty("missionType")
    private MissionTypeEnum missionType;
    /**
     * 状态类型<br/>
     * {@link ModifierTypeEnum}
     */
    @JsonProperty("modifierType")
    private ModifierTypeEnum modifierType;
    /**
     * 节点
     */
    @JsonProperty("node")
    private String node;
    /**
     * 地图板块
     */
    @JsonProperty("tileset")
    private String tileset;

    /**
     * 获取任务类型名称
     *
     * @return {@link MissionTypeEnum#name}
     */
    @JsonIgnore
    public String getMissionTypeName() {
        return missionType.getName();
    }

    /**
     * 获取状态类型名称
     *
     * @return {@link ModifierTypeEnum#str}
     */
    @JsonIgnore
    public String getModifierTypeStr() {
        return modifierType.getStr();
    }
}
