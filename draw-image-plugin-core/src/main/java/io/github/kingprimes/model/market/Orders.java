package io.github.kingprimes.model.market;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kingprimes.model.enums.MarketPlatformEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * <h1>用于生成图片的订单数据</h1>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class Orders {
    /**
     * 物品名称
     */
    @JsonProperty("name")
    String name;
    /**
     * 平台类型
     *
     * @see MarketPlatformEnum
     */
    @JsonProperty("form")
    MarketPlatformEnum form;
    /**
     * true: 购买 false: 出售
     */
    @JsonProperty("isBy")
    Boolean isBy;
    /**
     * true: 满级 false: 未满级
     */
    @JsonProperty("isMax")
    Boolean isMax;

    /**
     * 物品价值杜卡币
     */
    @JsonProperty("ducats")
    Integer ducats;

    /**
     * 遗物是否入库
     * true 入库
     * false 未入库
     */
    @JsonProperty("vaulted")
    Boolean vaulted;
    /**
     * 阿耶檀识 黄星星
     */
    @JsonProperty("maxAmberStars")
    Integer maxAmberStars;

    /**
     * 阿耶檀识 蓝星星
     */
    @JsonProperty("maxCyanStars")
    Integer maxCyanStars;

    /**
     * 阿耶檀识 基础 内融核心
     */
    @JsonProperty("baseEndo")
    Integer baseEndo;

    /**
     * 段位等级限制
     */
    @JsonProperty("reqMasteryRank")
    Integer reqMasteryRank;

    /**
     * 交易税
     */
    @JsonProperty("tradingTax")
    Integer tradingTax;

    /**
     * 物品图标 <br/>
     * 该数据可为空，当为空时不绘制图标
     */
    @JsonIgnore
    BufferedImage icon;

    /**
     * 订单列表
     *
     * @see OrderWithUser
     */
    @JsonProperty("orders")
    List<OrderWithUser> orders;
}
