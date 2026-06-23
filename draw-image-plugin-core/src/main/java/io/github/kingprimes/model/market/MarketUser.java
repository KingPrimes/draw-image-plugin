package io.github.kingprimes.model.market;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.kingprimes.model.enums.MarketActivityTypeEnum;
import io.github.kingprimes.model.enums.MarketPlatformEnum;
import io.github.kingprimes.model.enums.MarketStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

/**
 * Market User
 *
 * @author KingPrimes
 * @version 1.0.3
 * @see OrderWithUser
 */
@Data
@Accessors(chain = true)
public class MarketUser {
    /**
     * 用户ID
     */
    @JsonProperty("id")
    String id;
    /**
     * 游戏昵称
     */
    @JsonProperty("ingameName")
    String ingameName;
    /**
     * 头像
     */
    @JsonProperty("avatar")
    String avatar;
    /**
     * 评分
     */
    @JsonProperty("reputation")
    Integer reputation;
    /**
     * 地区
     */
    @JsonProperty("locale")
    String locale;
    /**
     * 平台
     *
     * @see MarketPlatformEnum
     */
    @JsonProperty("platform")
    MarketPlatformEnum platform;
    /**
     * 状态
     *
     * @see MarketStatusEnum
     */
    @JsonProperty("status")
    MarketStatusEnum status;
    /**
     * 活动
     */
    @JsonProperty("activity")
    Activity activity;
    /**
     * 最后登录时间
     */
    @JsonProperty("lastSeen")
    Instant lastSeen;

    @Data
    @Accessors(chain = true)
    public static class Activity {
        /**
         * 活动类型
         *
         * @see MarketActivityTypeEnum
         */
        @JsonProperty("type")
        MarketActivityTypeEnum type;
        /**
         * 活动详情
         */
        @JsonProperty("details")
        String details;
        /**
         * 活动开始时间
         */
        @JsonProperty("startedAt")
        Instant startedAt;
    }
}
