package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kingprimes.model.enums.SyndicateEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 集团任务
 * <p>表示游戏中各个集团的任务信息，包括赏金任务和节点信息</p>
 * <p>该类继承 {@link BastWorldState} 基类</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SyndicateMission extends BastWorldState {
    /**
     * 集团标签
     * <p>标识该任务属于哪个集团，如ArbitersSyndicate、CetusSyndicate等</p>
     */
    @JsonProperty("Tag")
    private SyndicateEnum tag;

    /**
     * 种子
     * <p>用于生成任务的随机种子值</p>
     */
    @JsonProperty("Seed")
    private Integer seed;

    /**
     * 节点列表
     * <p>该集团任务涉及的星图节点列表</p>
     */
    @JsonProperty("Nodes")
    private List<String> nodes;

    /**
     * 任务列表
     * <p>该集团提供的具体任务列表，如赏金任务</p>
     */
    @JsonProperty("Jobs")
    private List<Job> jobs;

    /**
     * 获取集团名称
     * <p>返回该集团任务的名称</p>
     * {@link SyndicateEnum#name}
     *
     * @return 集团名称
     */
    @JsonIgnore
    public String getSyndicateName() {
        return tag.getName();
    }

    /**
     * 获取集团图标
     * <p>返回该集团任务的图标</p>
     * {@link SyndicateEnum#icon}
     *
     * @return 集团图标
     */
    @JsonIgnore
    public String getSyndicateIcon() {
        return tag.getIcon();
    }
}
