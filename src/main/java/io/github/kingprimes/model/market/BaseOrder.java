package io.github.kingprimes.model.market;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <h1>Warframe.Market API V2版本 响应数据基类</h1><br/>
 * <a href="#">https://api.warframe.market/v2/orders/item/%s</a>
 *
 * @param <T> {@link OrderWithUser}
 */
@Data
@Accessors(chain = true)
public class BaseOrder<T> {
    /**
     * API版本
     */
    String apiVersion;
    /**
     * 数据列表
     */
    List<T> data;
    /**
     * 错误信息
     */
    Object error;
}

