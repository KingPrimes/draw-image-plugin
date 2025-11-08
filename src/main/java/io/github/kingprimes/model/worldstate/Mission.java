package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kingprimes.model.enums.MissionTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 任务详情
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class Mission {
    /**
     * 任务类型
     * <p>任务的类型，如MT_MOBILE_DEFENSE(移动防御)、MT_ARTIFACT(寻找神器)等</p>
     */
    @JsonProperty("missionType")
    private MissionTypeEnum missionType;
    /**
     * 节点
     * <p>任务所在的星图节点，如"SolNode125"</p>
     */
    @JsonProperty("node")
    private String node;

    /**
     * 获取任务类型
     *
     * @return 任务类型 {@link MissionTypeEnum#name} String字符串
     */
    @JsonIgnore
    public String getMissionTypeName() {
        return missionType.getName();
    }
}
