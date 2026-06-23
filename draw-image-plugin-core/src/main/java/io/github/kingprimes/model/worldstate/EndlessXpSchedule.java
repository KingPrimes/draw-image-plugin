package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 双衍王境奖励时间表
 *
 * @author KingPrimes
 * @version 1.0.8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class EndlessXpSchedule extends BastWorldState {

    @JsonProperty("CategoryChoices")
    private List<EndlessXpChoices> categoryChoices;
}
