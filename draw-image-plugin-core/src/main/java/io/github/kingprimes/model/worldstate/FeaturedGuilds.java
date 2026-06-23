package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 精选氏族
 * <p>该类继承 {@link BastWorldState} 基类</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class FeaturedGuilds extends BastWorldState {
    /**
     * 氏族联盟ID
     * <p>关联的联盟唯一标识符</p>
     */
    @JsonProperty("AllianceId")
    private Id id;

    /**
     * 是否有族徽
     * <p>表示该氏族是否设置了族徽</p>
     */
    @JsonProperty("Emblem")
    private Boolean emblem;

    /**
     * 隐藏平台信息
     * <p>表示该氏族在特定平台上的可见性设置</p>
     */
    @JsonProperty("HiddenPlatforms")
    private HiddenPlatforms hiddenPlatforms;

    /**
     * 图标覆盖
     * <p>图标覆盖设置，0表示默认图标，1表示自定义图标</p>
     */
    @JsonProperty("IconOverride")
    private Integer iconOverride;

    /**
     * 氏族名称
     * <p>氏族的完整名称，格式为"名称#编号"</p>
     */
    @JsonProperty("Name")
    private String name;

    /**
     * 氏族等级
     * <p>氏族的等级，范围从1到5</p>
     */
    @JsonProperty("Tier")
    private Integer tier;

    @Data
    public static class HiddenPlatforms {
        /**
         * iOS平台隐藏状态
         * <p>true表示在iOS平台上隐藏该氏族</p>
         */
        @JsonProperty("PLATFORM_IOS")
        private Boolean ios = false;

        /**
         * Switch平台隐藏状态
         * <p>true表示在Switch平台上隐藏该氏族</p>
         */
        @JsonProperty("PLATFORM_SWITCH")
        private Boolean switchPlatform = false;

        /**
         * 跨平台隐藏状态
         * <p>true表示在所有跨平台环境中隐藏该氏族</p>
         */
        @JsonProperty("PLATFORM_CROSS_PLATFORM")
        private Boolean crossPlatform = false;
    }
}
