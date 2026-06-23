package io.github.kingprimes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kingprimes.model.enums.FactionEnum;
import io.github.kingprimes.utils.TimeUtils;
import io.github.kingprimes.utils.TimeZoneUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.awt.*;
import java.time.Instant;
import java.util.Arrays;

/**
 * 预测仲裁
 * <p>该类表示一个仲裁任务的信息，包括任务时间、地点、敌人类型等信息</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Arbitration {

    /**
     * 时间
     */
    @JsonProperty("activation")
    private Instant activation;
    /**
     * 结束时间
     */
    @JsonProperty("expiry")
    private Instant expiry;
    /**
     * 代号
     */
    @JsonProperty("id")
    private String id;
    /**
     * 节点
     */
    @JsonProperty("node")
    private String node;
    /**
     * 行星
     */
    @JsonProperty("planet")
    private String planet;
    /**
     * 敌人
     */
    @JsonProperty("enemy")
    private String enemy;
    /**
     * 任务类型
     */
    @JsonProperty("missionType")
    private String type;

    @JsonProperty("etc")
    private String etc;

    /**
     * 判断该仲裁任务是否值得参与
     * <p>根据任务类型、节点和敌人类型来判断任务的价值</p>
     *
     * @return 如果任务值得参与返回true，否则返回false
     */
    @JsonIgnore
    public Boolean isWorth() {
        boolean isType = type.contains("拦截") || type.contains("防御");
        boolean isNode = node.contains("谷神星") || node.contains("水星");
        boolean isEnemy = enemy.contains("Grin") || enemy.contains("Infest");
        return isType && isNode && isEnemy;
    }

    /**
     * 获取派系枚举
     *
     * @return 派系枚举
     */
    @JsonIgnore
    public FactionEnum getEnemy() {
        return Arrays.stream(FactionEnum.values()).filter(f -> f.name().contains(enemy.toUpperCase())).findFirst().orElse(FactionEnum.FC_NONE);
    }

    /**
     * 获取敌人名称
     *
     * @return 敌人名称
     */
    @JsonIgnore
    public String getEnemyName() {
        return getEnemy().getName();
    }

    /**
     * 获取敌人颜色
     *
     * @return 敌人颜色
     */
    @JsonIgnore
    public Color getEnemyColor() {
        return getEnemy().getColor();
    }

    /**
     * 获取敌人图标
     *
     * @return 敌人图标
     */
    @JsonIgnore
    public String getEnemyIcon() {
        return getEnemy().getIcon();
    }

    /**
     * 开始时间
     *
     * @return 开始时间
     */
    @JsonIgnore
    public String getActivationFormat() {
        return TimeZoneUtil.formatTimestamp(activation.toEpochMilli());
    }

    /**
     * 结束时间
     *
     * @return 结束时间
     */
    @JsonIgnore
    public String getExpiryFormat() {
        return TimeZoneUtil.formatTimestamp(expiry.toEpochMilli());
    }

    /**
     * 获取剩余时间的可读表示
     *
     * <p>{@link TimeUtils#timeDeltaToString(long)}</p>
     *
     * @return 剩余时间的字符串表示，如 "1d 2h 30m 0s"
     */
    @JsonIgnore
    public String getTimeLeft() {
        return TimeUtils.timeDeltaToString(expiry.toEpochMilli() - System.currentTimeMillis());
    }

    /**
     * 获取剩余时间
     *
     * @return 剩余时间
     */
    @JsonIgnore
    public String getEtc() {
        return TimeUtils.timeDeltaToString(expiry.toEpochMilli() - System.currentTimeMillis());
    }

}
