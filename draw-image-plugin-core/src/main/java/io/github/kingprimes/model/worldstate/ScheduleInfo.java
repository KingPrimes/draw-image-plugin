package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 火星集市 瓦奇娅 的时间信息表 {@link PrimeVaultTrader}
 * <p>该类继承 {@link BastWorldState} 基类</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ScheduleInfo extends BastWorldState {

    /**
     * 物品名称<br/>
     * 例如："/Lotus/Types/StoreItems/Packages/MegaPrimeVault/MPVEquinoxWukongPrimeDualPack"
     */
    @JsonProperty("FeaturedItem")
    String item;

}
