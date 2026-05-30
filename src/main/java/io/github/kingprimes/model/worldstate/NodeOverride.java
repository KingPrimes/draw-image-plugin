package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 节点覆盖
 * <p>表示对星图节点的覆盖配置，可以用于隐藏节点、修改关卡或添加特殊遭遇战</p>
 * <p>该类继承 {@link BastWorldState} 基类</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeOverride extends BastWorldState {
    /**
     * 节点
     * <p>需要覆盖的星图节点标识，如"EarthHUB"、"SolNode129"等</p>
     */
    @JsonProperty("Node")
    private String node;

    /**
     * 是否隐藏
     * <p>true表示隐藏该节点，false表示显示该节点</p>
     */
    @JsonProperty("Hide")
    private Boolean hide;

    /**
     * 关卡覆盖
     * <p>指定节点使用的特殊关卡配置路径</p>
     */
    @JsonProperty("LevelOverride")
    private String levelOverride;

    /**
     * 激活时间
     * <p>节点覆盖配置的激活时间</p>
     */
    @JsonProperty("Activation")
    private DateField activation;

    /**
     * 派系
     * <p>节点的派系归属，如"FC_CORPUS"</p>
     */
    @JsonProperty("Faction")
    private String faction;

    /**
     * 自定义NPC遭遇战
     * <p>在该节点可能出现的特殊NPC遭遇战列表</p>
     */
    @JsonProperty("CustomNpcEncounters")
    private List<String> customNpcEncounters;

    /**
     * 种子
     * <p>用于生成随机内容的种子值</p>
     */
    @JsonProperty("Seed")
    private Integer seed;
}
