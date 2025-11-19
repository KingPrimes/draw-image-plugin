package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kingprimes.model.enums.FactionEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.awt.*;
import java.util.List;

/**
 * 任务信息模型类，用于封装任务的基础数据。
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class MissionInfo {
    /**
     * 任务种子
     */
    @JsonProperty("seed")
    private Integer seed;
    /**
     * 任务阵营
     */
    @JsonProperty("faction")
    private FactionEnum faction;
    /**
     * 任务奖励
     * <p>{@link Reward}</p>
     */
    @JsonProperty("missionReward")
    private List<Reward> missionReward;

    /**
     * 获取任务阵营名称
     *
     * @return {@link FactionEnum#name}
     */
    @JsonIgnore
    public String getFactionName() {
        return faction.getName();
    }

    /**
     * 获取任务阵营颜色
     *
     * @return {@link FactionEnum#color}
     */
    @JsonIgnore
    public Color getFactionColor() {
        return faction.getColor();
    }

    /**
     * 获取任务阵营图标
     *
     * @return {@link FactionEnum#icon}
     */
    @JsonIgnore
    public String getFactionIcon() {
        return faction.getIcon();
    }
}
