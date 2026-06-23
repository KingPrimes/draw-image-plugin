package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 闪购商品
 * <p>表示游戏中限时售卖的商品信息</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class FlashSale {
    /**
     * 买X件商品
     * <p>BOGO(买一送一)优惠中需要购买的商品数量</p>
     */
    @JsonProperty("BogoBuy")
    private Integer bogoBuy;

    /**
     * 送Y件商品
     * <p>BOGO(买一送一)优惠中赠送的商品数量</p>
     */
    @JsonProperty("BogoGet")
    private Integer bogoGet;

    /**
     * 是否为每日生成的销售
     * <p>标识该闪购是否为每日自动生成的销售项目</p>
     */
    @JsonProperty("DailySaleGenerated")
    private Boolean dailySaleGenerated;

    /**
     * 折扣
     * <p>商品折扣百分比，0表示无折扣</p>
     */
    @JsonProperty("Discount")
    private Integer discount;

    /**
     * 结束时间
     * <p>闪购活动的结束时间</p>
     */
    @JsonProperty("EndDate")
    private DateField endDate;

    /**
     * 是否在市场中隐藏
     * <p>true表示在市场中隐藏该商品</p>
     */
    @JsonProperty("HideFromMarket")
    private Boolean hideFromMarket;

    /**
     * 白金价格覆盖
     * <p>覆盖商品的白金价格，0表示使用默认价格</p>
     */
    @JsonProperty("PremiumOverride")
    private Integer premiumOverride;

    /**
     * 常规价格覆盖
     * <p>覆盖商品的常规价格，0表示使用默认价格</p>
     */
    @JsonProperty("RegularOverride")
    private Integer regularOverride;

    /**
     * 是否在市场中显示
     * <p>true表示在市场中显示该商品</p>
     */
    @JsonProperty("ShowInMarket")
    private Boolean showInMarket;

    /**
     * 开始时间
     * <p>闪购活动的开始时间</p>
     */
    @JsonProperty("StartDate")
    private DateField startDate;

    /**
     * 是否是支持者包
     * <p>true表示该商品为支持者专属包</p>
     */
    @JsonProperty("SupporterPack")
    private Boolean supporterPack;

    /**
     * 商品类型名称
     * <p>商品的完整类型路径，用于唯一标识商品</p>
     */
    @JsonProperty("TypeName")
    private String typeName;
}
