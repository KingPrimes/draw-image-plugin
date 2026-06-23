package io.github.kingprimes.model.market;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <h1>Warframe.Market API V2版本  物品详细信息响应数据基类</h1></br>
 * <a href="#">https://api.warframe.market/v2/item/%s/set</a>
 *
 * @param <T> 响应数据类型
 */
@Data
@Accessors(chain = true)
public class BaseOrderObjet<T> {
    String apiVersion;
    T data;
    Object error;

    @lombok.Data
    @Accessors(chain = true)
    public static class Data<E> {
        String id;
        List<E> items;
    }
}
