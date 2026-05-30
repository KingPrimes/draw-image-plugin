package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Prime 访问可用性
 *
 * @author KingPrimes
 * @version 1.0.7
 */
@Data
@Accessors(chain = true)
public class PrimeAccessAvailability {

    @JsonProperty("State")
    private String state;
}
