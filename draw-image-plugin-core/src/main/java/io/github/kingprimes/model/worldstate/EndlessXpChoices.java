package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 双衍王境 奖励
 *
 * @author KingPrimes
 * @version 1.0.0
 */

@Data
@Accessors(chain = true)
public class EndlessXpChoices {

    /**
     * 奖励 类型枚举
     * <p>{@link Category}</p>
     */
    @JsonProperty("Category")
    private Category category;

    /**
     * 奖励 名称
     */
    @JsonProperty("Choices")
    private List<String> choices;

    public enum Category {
        /**
         * 普通
         */
        EXC_NORMAL,
        /**
         * 钢铁
         */
        EXC_HARD
    }
}
