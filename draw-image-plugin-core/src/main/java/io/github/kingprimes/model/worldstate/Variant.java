package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kingprimes.model.enums.MissionTypeEnum;
import io.github.kingprimes.model.enums.ModifierTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.awt.*;

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
     * 获取任务类型
     *
     * <p>{@link MissionTypeEnum#name}</p>
     *
     * @return 任务类型 String字符串
     */
    @JsonIgnore
    public String getMissionTypeName() {
        if (missionType == null) return MissionTypeEnum.MT_DEFAULT.getName();
        return missionType.getName();
    }

    /**
     * 获取任务类型颜色
     *
     * <p>{@link MissionTypeEnum#getColor(MissionTypeEnum)}</p>
     *
     * @return 任务类型颜色
     */
    @JsonIgnore
    public Color getMissionTypeColor() {
        if (missionType == null) return MissionTypeEnum.getColor(MissionTypeEnum.MT_DEFAULT);
        return MissionTypeEnum.getColor(missionType);
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
