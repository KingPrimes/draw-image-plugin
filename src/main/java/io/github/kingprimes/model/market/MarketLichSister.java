package io.github.kingprimes.model.market;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kingprimes.model.enums.ElementEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

/**
 * <h1>查询赤毒/信条武器拍卖市场数据 API V1 版本</h1>
 * <p>用于封装从市场API获取的赤毒武器和信条武器拍卖信息</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class MarketLichSister {

    /**
     * 响应数据载荷
     * <p>包含拍卖行数据的主要内容</p>
     */
    @JsonProperty("payload")
    private Payload payload;

    /**
     * 响应载荷类
     * <p>封装拍卖行数据的载荷部分</p>
     */
    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    public static class Payload {
        /**
         * 拍卖列表
         * <p>当前查询条件下的所有拍卖项目列表</p>
         */
        @JsonProperty("auctions")
        private List<Auctions> auctions;

        /**
         * 拍卖物品名称
         * <p>当前查询条件下的所有拍卖物品名称</p>
         */
        @JsonProperty("item_name")
        private String itemName;

    }

    /**
     * 拍卖项目类
     * <p>表示单个拍卖项目的详细信息</p>
     */
    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    public static class Auctions {
        /**
         * 买断价格
         * <p>玩家可以立即购买此拍卖品的价格</p>
         */
        @JsonProperty("buyout_price")
        private Integer buyoutPrice;

        /**
         * 备注信息
         * <p>卖家添加的拍卖备注信息</p>
         */
        @JsonProperty("note")
        private String note;

        /**
         * 是否可见
         * <p>标识该拍卖是否对其他玩家可见</p>
         */
        @JsonProperty("visible")
        private Boolean visible;

        /**
         * 物品信息
         * <p>拍卖物品的详细信息</p>
         */
        @JsonProperty("item")
        private Item item;

        /**
         * 起拍价
         * <p>拍卖的起始竞拍价格</p>
         */
        @JsonProperty("starting_price")
        private Integer startingPrice;

        /**
         * 声望要求
         * <p>参与竞拍所需的最低声望值</p>
         */
        @JsonProperty("minimal_reputation")
        private Integer minimalReputation;

        /**
         * 卖家信息
         * <p>拍卖发起者的详细信息</p>
         */
        @JsonProperty("owner")
        private Owner owner;

        /**
         * 平台
         * <p>拍卖所在的平台，如PC、PlayStation、Xbox等</p>
         */
        @JsonProperty("platform")
        private String platform;

        /**
         * 是否关闭
         * <p>标识该拍卖是否已关闭</p>
         */
        @JsonProperty("closed")
        private Boolean closed;

        /**
         * 最高出价
         * <p>当前最高的竞拍出价</p>
         */
        @JsonProperty("top_bid")
        private Integer topBid;

        /**
         * 赢家信息
         * <p>拍卖获胜者的信息，拍卖进行中时为null</p>
         */
        @JsonProperty("winner")
        private Object winner;

        /**
         * 标记信息
         * <p>拍卖品被标记的相关信息</p>
         */
        @JsonProperty("is_marked_for")
        private Object isMarkedFor;

        /**
         * 标记操作时间
         * <p>拍卖品被标记的时间</p>
         */
        @JsonProperty("marked_operation_at")
        private Object markedOperationAt;

        /**
         * 创建时间
         * <p>拍卖创建的时间</p>
         */
        @JsonProperty("created")
        private Instant created;

        /**
         * 更新时间
         * <p>拍卖信息最后更新的时间</p>
         */
        @JsonProperty("updated")
        private Instant updated;

        /**
         * 原始备注
         * <p>未经处理的原始备注信息</p>
         */
        @JsonProperty("note_raw")
        private String noteRaw;

        /**
         * 是否直接出售
         * <p>标识该拍卖是否为直接出售（无竞拍过程）</p>
         */
        @JsonProperty("is_direct_sell")
        private Boolean isDirectSell;

        /**
         * 拍卖ID
         * <p>拍卖项目的唯一标识符</p>
         */
        @JsonProperty("id")
        private String id;

        /**
         * 是否为私人拍卖
         * <p>标识该拍卖是否为私人拍卖（仅对特定玩家可见）</p>
         */
        @JsonProperty("private")
        private Boolean privateX;


    }

    /**
     * 拍卖物品类
     * <p>表示拍卖物品的详细信息</p>
     */
    @NoArgsConstructor
    @Data
    @Accessors(chain = true)
    public static class Item {
        /**
         * 物品类型
         * <p>拍卖物品的类型标识</p>
         */
        @JsonProperty("type")
        private String type;

        /**
         * 加成数值
         * <p>武器的伤害加成数值</p>
         */
        @JsonProperty("damage")
        private Integer damage;

        /**
         * 武器URL名称
         * <p>武器在URL中使用的名称标识</p>
         */
        @JsonProperty("weapon_url_name")
        private String weaponUrlName;

        /**
         * 是否携带幻纹
         * <p>标识该武器是否携带幻纹</p>
         */
        @JsonProperty("having_ephemera")
        private Boolean havingEphemera;

        /**
         * 元素属性
         * <p>武器的元素属性类型，如火焰、冰冻等</p>
         */
        @JsonProperty("element")
        private ElementEnum element;

    }


}
