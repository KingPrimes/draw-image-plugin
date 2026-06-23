package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 深层征服
 *
 * @author KingPrimes
 * @version 1.0.8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class Conquest extends BastWorldState {

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Missions")
    private List<Mission> missions;

    @JsonProperty("Variables")
    private List<String> variables;

    @JsonProperty("RandomSeed")
    private Integer randomSeed;

    @Data
    @Accessors(chain = true)
    public static class Mission {
        @JsonProperty("faction")
        private String faction;

        @JsonProperty("missionType")
        private String missionType;

        @JsonProperty("difficulties")
        private List<Difficulty> difficulties;
    }

    @Data
    @Accessors(chain = true)
    public static class Difficulty {
        @JsonProperty("type")
        private String type;

        @JsonProperty("deviation")
        private String deviation;

        @JsonProperty("risks")
        private List<String> risks;
    }
}
