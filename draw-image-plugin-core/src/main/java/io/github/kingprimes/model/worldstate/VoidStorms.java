package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kingprimes.model.enums.VoidEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.awt.*;

/**
 * 虚空风暴
 * <p>表示九重天中的虚空风暴任务节点信息</p>
 * <p>该类继承 {@link BastWorldState} 基类</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class VoidStorms extends BastWorldState {
    /**
     * 节点
     * <p>虚空风暴所在的九重天节点，如"CrewBattleNode509"</p>
     */
    @JsonProperty("Node")
    private String node;

    /**
     * 激活任务等级
     * <p>当前激活的虚空任务等级，如VoidT1、VoidT2等</p>
     */
    @JsonProperty("ActiveMissionTier")
    private VoidEnum Tier;

    /**
     * 获取遗物等级
     *
     * <p>{@link VoidEnum#name}</p>
     *
     * @return 遗物等级 String字符串
     */
    @JsonIgnore
    public String getTierName() {
        if (Tier == null) return VoidEnum.VoidT1.getName();
        return Tier.getName();
    }


    /**
     * 获取虚空遗物等级颜色
     * <p>{@link VoidEnum#color}</p>
     *
     * @return 虚空遗物等级的颜色值
     */
    @JsonIgnore
    public Color getTierColor() {
        return Tier.getColor();
    }
}
