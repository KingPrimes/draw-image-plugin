package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kingprimes.model.enums.FactionEnum;
import io.github.kingprimes.model.enums.MissionTypeEnum;
import io.github.kingprimes.model.enums.VoidEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.awt.*;

/**
 * 裂隙任务
 * <p>该类继承 {@link BastWorldState} 基类</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ActiveMission extends BastWorldState {
    /**
     * 任务类型
     */
    @JsonProperty("MissionType")
    private MissionTypeEnum missionType;
    /**
     * 遗物等级
     */
    @JsonProperty("Modifier")
    private VoidEnum modifier;
    /**
     * 节点
     */
    @JsonProperty("Node")
    private String node;

    /**
     * 派系
     */
    @JsonProperty("Faction")
    private FactionEnum faction;

    /**
     * 地区
     */
    @JsonProperty("Region")
    private Integer region;
    /**
     * 种子
     */
    @JsonProperty("Seed")
    private Integer seed;
    /**
     * 是否是钢铁模式
     */
    @JsonProperty("Hard")
    private Boolean hard = false;

    /**
     * 是否是虚空风暴模式
     */
    @JsonProperty("voidStorms")
    private Boolean voidStorms = false;

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
     * 获取遗物等级
     *
     * <p>{@link VoidEnum#name}</p>
     *
     * @return 遗物等级 String字符串
     */
    @JsonIgnore
    public String getModifierName() {
        if (modifier == null) return VoidEnum.VoidT1.getName();
        return modifier.getName();
    }

    /**
     * 获取遗物等级枚举
     *
     * <p>{@link VoidEnum}</p>
     *
     * @return 遗物等级枚举
     */
    @JsonIgnore
    public VoidEnum getModifier() {
        return modifier;
    }

    /**
     * 获取虚空遗物等级颜色
     * <p>{@link VoidEnum#color}</p>
     *
     * @return 虚空遗物等级的颜色值
     */
    @JsonIgnore
    public Color getModifierColor() {
        return modifier.getColor();
    }

    /**
     * 获取派系
     * <p>{@link FactionEnum}</p>
     *
     * @return 派系
     */
    @JsonIgnore
    public FactionEnum getFaction() {
        return faction;
    }

    /**
     * 获取派系名称
     * <p>{@link FactionEnum#name} </p>
     *
     * @return 派系名称
     */
    @JsonIgnore
    public String getFactionName() {
        return faction.getName();
    }

    /**
     * 获取派系颜色
     * <p>{@link FactionEnum#color} </p>
     *
     * @return 派系颜色
     */
    @JsonIgnore
    public Color getFactionColor() {
        return faction.getColor();
    }

    /**
     * 获取派系图标
     * <p>{@link FactionEnum#icon} </p>
     *
     * @return 派系图标
     */
    @JsonIgnore
    public String getFactionIcon() {
        return faction.getIcon();
    }
}
