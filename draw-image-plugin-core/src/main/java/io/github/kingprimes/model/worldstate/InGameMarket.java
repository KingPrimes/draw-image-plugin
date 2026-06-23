package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 游戏内商城
 * <p>表示游戏内商店的结构和商品分类信息</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class InGameMarket {

    /**
     * 商城首页信息
     * <p>包含商城首页的所有分类信息</p>
     */
    @JsonProperty("LandingPage")
    private LandingPage landingPage;

    /**
     * 商城分类枚举
     * <p>定义商城中的各种商品分类</p>
     */
    public enum CategoryName {
        /**
         * 新手玩家分类
         */
        NEW_PLAYER,

        /**
         * 新品分类
         */
        NEW,

        /**
         * 社区分类
         */
        COMMUNITY,

        /**
         * 传家宝分类
         */
        HEIRLOOM,

        /**
         * TennoGen创作者分类
         */
        TENNOGEN,

        /**
         * 特价分类
         */
        SALE,

        /**
         * 愿望清单分类
         */
        WISH_LIST,

        /**
         * 白金捆绑包分类
         */
        PREMIUM_BUNDLES,

        /**
         * 热门分类
         */
        POPULAR,

        /**
         * 季节性分类
         */
        SEASONAL,

        /**
         * 快速购买
         */
        QUICK_BUY

    }

    /**
     * 商城首页类
     * <p>表示商城首页的结构</p>
     */
    @Data
    @Accessors(chain = true)
    public static class LandingPage {
        /**
         * 分类列表
         * <p>商城首页展示的所有商品分类</p>
         */
        @JsonProperty("Categories")
        private List<Category> categories;
    }

    /**
     * 商品分类类
     * <p>表示商城中的一个商品分类</p>
     */
    @Data
    @Accessors(chain = true)
    public static class Category {
        /**
         * 分类名称
         * <p>分类的枚举标识，如NEW_PLAYER、POPULAR等</p>
         */
        @JsonProperty("CategoryName")
        private CategoryName categoryName;

        /**
         * 分类显示名称
         * <p>分类在界面上显示的本地化名称 如："/Lotus/Language/Store/NewPlayerCategoryTitle"</p>
         */
        @JsonProperty("Name")
        private String name;

        /**
         * 分类图标
         * <p>分类对应的图标标识，如："newplayer"、"popular"等</p>
         */
        @JsonProperty("Icon")
        private String icon;

        /**
         * 是否添加到菜单
         * <p>标识该分类是否应该显示在商城主菜单中</p>
         */
        @JsonProperty("AddToMenu")
        private Boolean addToMenu;

        /**
         * 商品列表
         * <p>该分类下包含的商品类型路径列表</p>
         */
        @JsonProperty("Items")
        private List<String> items;
    }
}
