package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 任务奖励模型类
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class Reward {
    /**
     * 现金奖励数量 <br/>
     * 例如：2000, 5000, 10000等
     */
    @JsonProperty("credits")
    private Integer credits;
    /**
     * 经验值奖励数量 <br/>
     * 完成任务后获得的角色或专精经验
     */
    @JsonProperty("xp")
    private Integer xp;
    /**
     * 物品奖励列表 <br/>
     * 包含奖励物品的名称列表
     */
    @JsonProperty("items")
    private List<String> items;
    /**
     * 有数量的物品奖励列表 <br/>
     * 包含特定数量的物品奖励详情 <br/>
     * 该奖励集合主要用于 {@link Invasion#getDefenderReward()} 与 {@link Invasion#getAttackerReward()} 奖励
     */
    @JsonProperty("countedItems")
    private List<Item> countedItems;

    /**
     * 奖励物品内部类 <br/>
     * 表示具有具体数量的单个物品奖励
     */
    @Data
    @Accessors(chain = true)
    public static class Item {
        /**
         * 物品名称
         * <p>如："/Lotus/Types/Recipes/Weapons/WeaponParts/DeraVandalBarrel"</p>
         */
        @JsonProperty("ItemType")
        private String name;

        /**
         * 物品数量
         * 该物品奖励的具体数量
         */
        @JsonProperty("ItemCount")
        private Integer count;
    }
}
