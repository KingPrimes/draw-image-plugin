package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 全局信息 基类
 * <p>这是所有世界状态信息类的基类，包含了通用的属性如ID、激活时间和过期时间</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
public class BastWorldState {

    /**
     * ID 唯一标识符
     */
    @JsonProperty("_id")
    private Id _id;

    /**
     * 开始时间
     */
    @JsonProperty("Activation")
    private DateField activation;
    /**
     * 结束时间
     */
    @JsonProperty("Expiry")
    private DateField expiry;

    /**
     * 剩余时间
     * <p>{@link DateField#getTimeLeft()}</p>
     *
     * @return 剩余时间
     */
    @JsonIgnore
    public String getTimeLeft() {
        return expiry.getTimeLeft();
    }

    /**
     * ID 唯一ID标识符
     * <p>用于唯一标识每个世界状态对象的内部类</p>
     */
    @Data
    public static class Id {
        /**
         * 唯一ID标识符
         * <p>从JSON数据中解析得到的OID字符串</p>
         */
        @JsonProperty("$oid")
        private String oid;
    }
}
