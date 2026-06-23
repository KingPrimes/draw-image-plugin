package io.github.kingprimes.model.worldstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 火星集市 瓦奇娅
 * <p>表示游戏中瓦奇娅火星集市的信息，包括商品清单和时间安排</p>
 * <p>该类继承 {@link BastWorldState} 基类</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PrimeVaultTrader extends BastWorldState {

    /**
     * 初始开始日期
     * <p>瓦奇娅火星集市的初始开始时间</p>
     */
    @JsonProperty("InitialStartDate")
    private DateField initialStartDate;

    /**
     * 节点
     * <p>瓦奇娅火星集市所在的星图节点，如"TradeHUB1"</p>
     */
    @JsonProperty("Node")
    private String node;

    /**
     * 商品清单
     * <p>{@link ManifestItem}</p>
     * <p>当前集市中可购买的商品列表</p>
     */
    @JsonProperty("Manifest")
    private List<ManifestItem> manifest;

    /**
     * 永久商品清单
     * <p>{@link ManifestItem}</p>
     * <p>在集市中始终可购买的商品列表</p>
     */
    @JsonProperty("EvergreenManifest")
    private List<ManifestItem> evergreenManifest;

    /**
     * 时间表信息
     * <p>{@link ScheduleInfo}</p>
     * <p>集市的时间安排信息列表</p>
     */
    @JsonProperty("ScheduleInfo")
    private List<ScheduleInfo> scheduleInfos;
}
