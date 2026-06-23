package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 每日特惠
 * <p>该类继承 {@link BastWorldState} 基类</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class DailyDeals extends BastWorldState {

    /**
     * 剩余数量
     */
    @JsonProperty("AmountSold")
    private Integer sold;

    /**
     * 总数
     */
    @JsonProperty("AmountTotal")
    private Integer total;

    /**
     * 折扣
     */
    @JsonProperty("Discount")
    private Double count;

    /**
     * 原价
     */
    @JsonProperty("OriginalPrice")
    private Integer originalPrice;

    /**
     * 售价
     */
    @JsonProperty("SalePrice")
    private Integer salePrice;
    /**
     * 商品
     */
    @JsonProperty("StoreItem")
    private String item;

}
