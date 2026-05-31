package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 新闻
 * <p>该类继承 {@link BastWorldState} 基类</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event extends BastWorldState {
    /**
     * 消息
     */
    @JsonProperty("Messages")
    private List<Message> messages;
    /**
     * 帖子链接
     */
    @JsonProperty("Prop")
    private String prop;
    /**
     * 图标
     */
    @JsonProperty("Icon")
    private String icon;
    /**
     * 优先级
     */
    @JsonProperty("Priority")
    private Boolean priority;
    /**
     * 手机端
     */
    @JsonProperty("MobileOnly")
    private Boolean mobileOnly;
    /**
     * 社区
     */
    @JsonProperty("Community")
    private Boolean community;
    /**
     * 时间
     */
    @JsonProperty("Date")
    private DateField date;
    /**
     * 图片
     */
    @JsonProperty("ImageUrl")
    private String imageUrl;
    /**
     * 活动开始时间
     */
    @JsonProperty("EventStartDate")
    private DateField eventStartDate;
    /**
     * 活动结束时间
     */
    @JsonProperty("EventEndDate")
    private DateField eventEndDate;
    /**
     * 是否隐藏活动
     * <p>false = 隐藏</p>
     * true = 显示
     */
    @JsonProperty("HideEndDateModifier")
    private Boolean hideEndDateModifier = false;

    @JsonProperty("EventLiveUrl")
    private String eventLiveUrl;

    @JsonProperty("Links")
    private List<EventLink> links;

    @Data
    @Accessors(chain = true)
    public static class EventLink {
        @JsonProperty("LanguageCode")
        private String languageCode;

        @JsonProperty("Link")
        private String link;
    }

}
