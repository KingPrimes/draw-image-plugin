package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 虚空商人
 * <p>表示游戏中的虚空商人(Baro'Ki Teel)信息，包括其位置和商品清单</p>
 * <p>该类继承 {@link BastWorldState} 基类</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class VoidTrader extends BastWorldState {
    /**
     * 角色名称
     * <p>虚空商人的角色名称，如"Baro'Ki Teel"</p>
     */
    @JsonProperty("Character")
    private String character;
    /**
     * 节点
     * <p>虚空商人出现的星图节点，如"SaturnHUB"</p>
     */
    @JsonProperty("Node")
    private String node;

    /**
     * 商品清单
     * <p>虚空商人出售的商品列表</p>
     */
    @JsonProperty("Manifest")
    private List<Manifest> manifest;

    /**
     * 商品清单类
     * <p>表示虚空商人出售的单个商品信息</p>
     */
    @Data
    @Accessors(chain = true)
    public static class Manifest {
        /**
         * 物品名称
         * <p>商品的物品类型路径标识符</p>
         */
        @JsonProperty("ItemType")
        private String item;

        /**
         * 杜卡币价格
         * <p>购买该商品需要支付的杜卡币数量</p>
         */
        @JsonProperty("PrimePrice")
        private Integer primePrice;
        /**
         * 星币价格
         * <p>购买该商品需要支付的星币数量</p>
         */
        @JsonProperty("RegularPrice")
        private Long regularPrice;

        /**
         * 限购数量
         * <p>每个玩家可以购买该商品的最大数量</p>
         */
        @JsonProperty("Limit")
        private Integer limit;
    }
}
