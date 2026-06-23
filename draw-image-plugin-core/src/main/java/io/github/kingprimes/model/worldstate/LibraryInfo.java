package io.github.kingprimes.model.worldstate;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 图书馆信息
 * <p>表示游戏中图书馆系统的相关信息</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class LibraryInfo {

    /**
     * 最后完成的目标类型
     * <p>表示玩家最近完成的图书馆研究目标类型路径</p>
     */
    @JsonProperty("LastCompletedTargetType")
    String lastCompletedTargetType;
}
