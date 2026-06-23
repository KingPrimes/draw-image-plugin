package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品清单
 * <p>{@link PrimeVaultTrader}</p>
 * <p>表示游戏中瓦奇娅火星集市的商品清单</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */

@Data
@Accessors(chain = true)
public class ManifestItem {
    /**
     * 商品类型 例如："/Lotus/Types/StoreItems/Packages/MegaPrimeVault/MPVMagPrimeSinglePack"
     */
    @JsonProperty("ItemType")
    private String itemType;
    /**
     * 所需 御品阿耶精华 数量
     */
    @JsonProperty("PrimePrice")
    private Integer primePrice;
    /**
     * 所需 阿耶精华 数量
     */
    @JsonProperty("RegularPrice")
    private Integer regularPrice;
}
