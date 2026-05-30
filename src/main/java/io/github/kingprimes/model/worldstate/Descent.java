package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 深层下降
 *
 * @author KingPrimes
 * @version 1.0.8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class Descent extends BastWorldState {

    @JsonProperty("RandSeed")
    private Long randSeed;

    @JsonProperty("Challenges")
    private List<Challenge> challenges;

    @Data
    @Accessors(chain = true)
    public static class Challenge {
        @JsonProperty("Index")
        private Integer index;

        @JsonProperty("Type")
        private String type;

        @JsonProperty("Challenge")
        private String challenge;

        @JsonProperty("Level")
        private String level;

        @JsonProperty("Specs")
        private List<String> specs;

        @JsonProperty("Auras")
        private List<String> auras;
    }
}
