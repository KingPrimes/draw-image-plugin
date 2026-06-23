package io.github.kingprimes;

import io.github.kingprimes.model.*;
import io.github.kingprimes.model.market.MarketLichSister;
import io.github.kingprimes.model.market.MarketRiven;
import io.github.kingprimes.model.market.Orders;
import io.github.kingprimes.model.worldstate.*;

import java.util.List;
import java.util.Map;

/**
 * 可切换的 {@link DrawImagePlugin} 代理实现。
 * <p>
 * 所有 {@code drawXXX} 方法均委托给内部持有的 {@code delegate} 实例，
 * 宿主可通过 {@link #switchTo(DrawImagePlugin)} 在运行时原子切换实际插件，
 * 而业务 Bean 持有的此代理引用始终保持不变 —— 无需重新注入即可感知切换。
 * </p>
 *
 * <h3>线程安全</h3>
 * {@code delegate} 使用 {@code volatile} 保证切换的可见性，
 * 已进入方法调用的线程继续使用旧实例，新调用使用新实例，不会出现并发撕裂。
 *
 * <h3>使用示例</h3>
 * <pre>{@code
 * // 启动时：创建代理并注册到 Spring 容器
 * SwitchableDrawImagePlugin plugin = new SwitchableDrawImagePlugin(initialPlugin);
 *
 * // 运行时切换（由控制器调用）
 * plugin.switchTo(newPlugin);
 *
 * // 业务代码注入 DrawImagePlugin 即可，无需感知代理存在
 * }</pre>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class SwitchableDrawImagePlugin implements DrawImagePlugin {

    private volatile DrawImagePlugin delegate;

    /**
     * 创建可切换代理
     *
     * @param initialDelegate 初始插件实现
     */
    public SwitchableDrawImagePlugin(DrawImagePlugin initialDelegate) {
        if (initialDelegate == null) {
            throw new IllegalArgumentException("initialDelegate must not be null");
        }
        this.delegate = initialDelegate;
    }

    /**
     * 原子切换到新的插件实现
     *
     * @param newDelegate 新的插件实现
     */
    public void switchTo(DrawImagePlugin newDelegate) {
        if (newDelegate == null) {
            throw new IllegalArgumentException("newDelegate must not be null");
        }
        this.delegate = newDelegate;
    }

    /**
     * 获取当前代理的插件实例
     *
     * @return 当前插件实现
     */
    public DrawImagePlugin getCurrent() {
        return delegate;
    }

    // ==================== 委托方法 ====================

    @Override
    public byte[] drawHelpImage(List<String> helpInfo) {
        return delegate.drawHelpImage(helpInfo);
    }

    @Override
    public byte[] drawAllCycleImage(AllCycle allCycle) {
        return delegate.drawAllCycleImage(allCycle);
    }

    @Override
    public byte[] drawAllInfoImage(AllInfo allInfo) {
        return delegate.drawAllInfoImage(allInfo);
    }

    @Override
    public byte[] drawAlertsImage(List<Alert> alerts) {
        return delegate.drawAlertsImage(alerts);
    }

    @Override
    public byte[] drawArbitrationImage(Arbitration arbitration) {
        return delegate.drawArbitrationImage(arbitration);
    }

    @Override
    public byte[] drawArbitrationsImage(List<Arbitration> arbitrations) {
        return delegate.drawArbitrationsImage(arbitrations);
    }

    @Override
    public byte[] drawDailyDealsImage(DailyDeals dailyDeal) {
        return delegate.drawDailyDealsImage(dailyDeal);
    }

    @Override
    public byte[] drawDuviriCycleImage(DuvalierCycle duvalierCycle) {
        return delegate.drawDuviriCycleImage(duvalierCycle);
    }

    @Override
    public byte[] drawActiveMissionImage(List<ActiveMission> activeMission) {
        return delegate.drawActiveMissionImage(activeMission);
    }

    @Override
    public byte[] drawInvasionImage(List<Invasion> invasions) {
        return delegate.drawInvasionImage(invasions);
    }

    @Override
    public byte[] drawKnownCalendarSeasonsImage(List<KnownCalendarSeasons> knownCalendarSeasons) {
        return delegate.drawKnownCalendarSeasonsImage(knownCalendarSeasons);
    }

    @Override
    public byte[] drawLiteSoriteImage(LiteSorite liteSorite) {
        return delegate.drawLiteSoriteImage(liteSorite);
    }

    @Override
    public byte[] drawMarketGodDumpImage(Map<Ducats.DumpType, List<Ducats.Ducat>> dump) {
        return delegate.drawMarketGodDumpImage(dump);
    }

    @Override
    public byte[] drawMarketSilverDumpImage(Map<Ducats.DumpType, List<Ducats.Ducat>> dump) {
        return delegate.drawMarketSilverDumpImage(dump);
    }

    @Override
    public byte[] drawMarketLichesImage(MarketLichSister marketLichs) {
        return delegate.drawMarketLichesImage(marketLichs);
    }

    @Override
    public byte[] drawMarketSisterImage(MarketLichSister marketSister) {
        return delegate.drawMarketSisterImage(marketSister);
    }

    @Override
    public byte[] drawMarketOrdersImage(Orders orders) {
        return delegate.drawMarketOrdersImage(orders);
    }

    @Override
    public byte[] drawMarketOrdersImage(List<String> possibleItems) {
        return delegate.drawMarketOrdersImage(possibleItems);
    }

    @Override
    public byte[] drawMarketRivenImage(MarketRiven marketRiven) {
        return delegate.drawMarketRivenImage(marketRiven);
    }

    @Override
    public byte[] drawSeasonInfoImage(SeasonInfo seasonInfo) {
        return delegate.drawSeasonInfoImage(seasonInfo);
    }

    @Override
    public byte[] drawRelicsImage(List<Relics> relics) {
        return delegate.drawRelicsImage(relics);
    }

    @Override
    public byte[] drawRivenAnalyseTrendImage(List<RivenAnalyseTrendModel> rivenAnalyseTrendModel) {
        return delegate.drawRivenAnalyseTrendImage(rivenAnalyseTrendModel);
    }

    @Override
    public byte[] drawSortiesImage(Sortie sorties) {
        return delegate.drawSortiesImage(sorties);
    }

    @Override
    public byte[] drawSteelPath(SteelPathOffering steelPath) {
        return delegate.drawSteelPath(steelPath);
    }

    @Override
    public byte[] drawSyndicateImage(SyndicateMission sm) {
        return delegate.drawSyndicateImage(sm);
    }

    @Override
    public byte[] drawVoidTraderImage(List<VoidTrader> vt) {
        return delegate.drawVoidTraderImage(vt);
    }

    @Override
    public byte[] drawWarframeSubscribeImage(Map<Integer, String> subscribe,
                                              Map<Integer, String> missionType,
                                              Map<Integer, String> invasionReward) {
        return delegate.drawWarframeSubscribeImage(subscribe, missionType, invasionReward);
    }

    @Override
    public byte[] drawConquestImage(List<Conquest> conquests) {
        return delegate.drawConquestImage(conquests);
    }

    @Override
    public byte[] drawDescentImage(List<Descent> descents) {
        return delegate.drawDescentImage(descents);
    }

    @Override
    public String getPluginName() {
        return delegate.getPluginName();
    }

    @Override
    public String getPluginVersion() {
        return delegate.getPluginVersion();
    }

    @Override
    public String getPluginDescription() {
        return delegate.getPluginDescription();
    }

    @Override
    public void warmup() {
        delegate.warmup();
    }

    @Override
    public void releaseMemory() {
        delegate.releaseMemory();
    }
}
