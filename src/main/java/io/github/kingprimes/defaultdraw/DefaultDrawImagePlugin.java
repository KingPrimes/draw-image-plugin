package io.github.kingprimes.defaultdraw;

import com.sun.jna.Pointer;
import io.github.kingprimes.DrawImagePlugin;
import io.github.kingprimes.model.*;
import io.github.kingprimes.model.market.MarketLichSister;
import io.github.kingprimes.model.market.MarketRiven;
import io.github.kingprimes.model.market.Orders;
import io.github.kingprimes.model.worldstate.*;

import java.util.List;
import java.util.Map;

/**
 * 绘图 默认实现类
 *
 * @author KingPrimes
 * @version 1.0.3
 */
public class DefaultDrawImagePlugin implements DrawImagePlugin {

    /**
     * 绘制帮助图像
     *
     * @param helpInfo 帮助信息
     * @return 图像流
     */
    @Override
    public byte[] drawHelpImage(List<String> helpInfo) {
        return DefaultDrawHelpImage.drawHelpImage(helpInfo);
    }

    /**
     * 绘制所有平原图像
     *
     * @param allCycle 所有平原数据
     * @return 图像流
     */
    @Override
    public byte[] drawAllCycleImage(AllCycle allCycle) {
        return DefaultDrawAllCycleImage.drawAllCycleImage(allCycle);
    }

    /**
     * 绘制所有系统信息图像
     *
     * @param allInfo 所有信息数据
     * @return 图像流
     */
    @Override
    public byte[] drawAllInfoImage(AllInfo allInfo) {
        return DefaultDrawAllInfoImage.drawAllInfoImage(allInfo);
    }

    /**
     * 绘制所有警报图像
     *
     * @param alerts 所有警报数据
     * @return 图像流
     */
    @Override
    public byte[] drawAlertsImage(List<Alert> alerts) {
        return DefaultDrawAlertsImage.drawAlertsImage(alerts);
    }

    /**
     * 绘制仲裁图像
     *
     * @param arbitration 仲裁数据
     * @return 图像流
     */
    @Override
    public byte[] drawArbitrationImage(Arbitration arbitration) {
        return DefaultDrawArbitrationImage.drawArbitrationImage(arbitration);
    }

    /**
     * 绘制有价值的仲裁图像
     *
     * @param arbitrations 有价值的仲裁数据
     * @return 图像流
     */
    @Override
    public byte[] drawArbitrationsImage(List<Arbitration> arbitrations) {
        return DefaultDrawArbitrationsImage.drawArbitrationsImage(arbitrations);
    }

    /**
     * 绘制每日交易图像
     *
     * @param dailyDeal 每日交易数据
     * @return 图像流
     */
    @Override
    public byte[] drawDailyDealsImage(DailyDeals dailyDeal) {
        return DefaultDrawDailyDealsImage.drawDailyDealsImage(dailyDeal);
    }

    /**
     * 绘制双衍王境图像
     *
     * @param duvalierCycle 双衍王境循环数据
     * @return 图像流
     */
    @Override
    public byte[] drawDuviriCycleImage(DuvalierCycle duvalierCycle) {
        return DefaultDrawDuviriCycleImage.drawDuviriCycleImage(duvalierCycle);
    }

    /**
     * 绘制裂隙图像
     *
     * @param activeMission 裂隙数据
     * @return 图像流
     */
    @Override
    public byte[] drawActiveMissionImage(List<ActiveMission> activeMission) {
        return DefaultDrawActiveMission.drawActiveMissionImage(activeMission);
    }

    /**
     * 绘制入侵图像
     *
     * @param invasions 入侵数据
     * @return 图像流
     */
    @Override
    public byte[] drawInvasionImage(List<Invasion> invasions) {
        return DefaultDrawInvasionImage.drawInvasionImage(invasions);
    }

    /**
     * 绘制1999日历季节图像
     *
     * @param knownCalendarSeasons 1999日历季节数据
     * @return 图像流
     */
    @Override
    public byte[] drawKnownCalendarSeasonsImage(List<KnownCalendarSeasons> knownCalendarSeasons) {
        return DefaultDrawKnownCalendarSeasonsImage.drawKnownCalendarSeasonsImage(knownCalendarSeasons);
    }

    /**
     * 绘制执刑官猎杀图像
     *
     * @param liteSorite 执刑官猎杀数据
     * @return 图像流
     */
    @Override
    public byte[] drawLiteSoriteImage(LiteSorite liteSorite) {
        return DefaultDrawLiteSoriteImage.drawLiteSoriteImage(liteSorite);
    }

    /**
     * 绘制 Market 市场 金垃圾 杜卡币 图像
     *
     * @param dump 金垃圾数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketGodDumpImage(Map<Ducats.DumpType, List<Ducats.Ducat>> dump) {
        return DefaultDrawMarketDucatsImage.drawMarketDucatsImage(dump, "Market 市场 金垃圾");
    }

    /**
     * 绘制 Market 市场 银垃圾 杜卡币 图像
     *
     * @param dump 银垃圾数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketSilverDumpImage(Map<Ducats.DumpType, List<Ducats.Ducat>> dump) {
        return DefaultDrawMarketDucatsImage.drawMarketDucatsImage(dump, "Market 市场 银垃圾");
    }

    /**
     * 绘制 Market Liches 市场拍卖 图像
     *
     * @param marketLichs 市场拍卖数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketLichesImage(MarketLichSister marketLichs) {
        return DefaultDrawMarketLichSisterImage.drawMarketLichesImage(marketLichs);
    }

    /**
     * 绘制 Market Sister 市场拍卖 图像
     *
     * @param marketSister 市场拍卖数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketSisterImage(MarketLichSister marketSister) {
        return DefaultDrawMarketLichSisterImage.drawMarketLichesImage(marketSister);
    }

    /**
     * 绘制 Market Orders 订单 图像
     *
     * @param orders 订单数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketOrdersImage(Orders orders) {
        return DefaultDrawMarketOrdersImage.drawMarketOrdersImage(orders);
    }

    /**
     * 绘制 可能要查询的 Orders 订单 图像
     *
     * @param possibleItems 可能要查询的物品列表
     * @return 图像流
     */
    @Override
    public byte[] drawMarketOrdersImage(List<String> possibleItems) {
        return DefaultDrawMarketOrdersImage.drawMarketOrdersImage(possibleItems);
    }

    /**
     * 绘制 Market Riven 紫卡 图像
     *
     * @param marketRiven 紫卡数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketRivenImage(MarketRiven marketRiven) {
        return DefaultDrawMarketRivenImage.drawMarketRivenImage(marketRiven);
    }

    /**
     * 绘制 电波 图像
     *
     * @param seasonInfo 电波数据
     * @return 图像流
     */
    @Override
    public byte[] drawSeasonInfoImage(SeasonInfo seasonInfo) {
        return DefaultDrawSeasonInfoImage.drawSeasonInfoImage(seasonInfo);
    }

    /**
     * 绘制 遗物 图像
     *
     * @param relics 遗物数据
     * @return 图像流
     */
    @Override
    public byte[] drawRelicsImage(List<Relics> relics) {
        return DefaultDrawRelicsImage.drawRelicsImage(relics);
    }

    /**
     * 绘制 紫卡分析 图像
     *
     * @param rivenAnalyseTrendModel 紫卡分析数据
     * @return 图像流
     */
    @Override
    public byte[] drawRivenAnalyseTrendImage(List<RivenAnalyseTrendModel> rivenAnalyseTrendModel) {
        return DefaultDrawRivenAnalyseTrendImage.drawRivenAnalyseTrendImage(rivenAnalyseTrendModel);
    }

    /**
     * 绘制 突击 图像
     *
     * @param sorties 突击数据
     * @return 图像流
     */
    @Override
    public byte[] drawSortiesImage(List<Sortie> sorties) {
        return DefaultDrawSortiesImage.drawSortiesImage(sorties);
    }

    /**
     * 绘制 钢铁奖励 图像
     *
     * @param steelPath 钢铁奖励数据
     * @return 图像流
     */
    @Override
    public byte[] drawSteelPath(SteelPathOffering steelPath) {
        return DefaultDrawSteelPathImage.drawSteelPathImage(steelPath);
    }

    /**
     * 根据枚举绘制对应的 赏金/集团 图像
     * <p>
     * 绘制 赏金/集团 图像
     *
     * @param sm 赏金/集团 数据
     * @return 图像流
     */
    @Override
    public byte[] drawSyndicateImage(SyndicateMission sm) {
        return DefaultDrawSyndicateImage.drawSyndicateImage(sm);
    }

    /**
     * 绘制 虚空商人 图像
     *
     * @param vt 虚空商人数据
     * @return 图像流
     */
    @Override
    public byte[] drawVoidTraderImage(List<VoidTrader> vt) {
        return DefaultDrawVoidTraderImage.drawVoidTraderImage(vt);
    }

    /**
     * 绘制 订阅 帮助 图像
     *
     * @param subscribe   订阅类型数据
     * @param missionType 订阅任务类型数据
     * @return 图像流
     */
    @Override
    public byte[] drawWarframeSubscribeImage(Map<Integer, String> subscribe, Map<Integer, String> missionType) {
        return DefaultDrawWarframeSubscribeImage.drawWarframeSubscribeImage(subscribe, missionType);
    }

    /**
     * 获取插件名称
     *
     * @return 插件名称
     */
    @Override
    public String getPluginName() {
        return "Default";
    }

    /**
     * 获取插件版本
     *
     * @return 插件版本
     */
    @Override
    public String getPluginVersion() {
        return "1.0.0";
    }

    /**
     * 释放插件内存
     */
    @Override
    public void releaseMemory() {

    }

    /**
     * 释放插件内存
     *
     * @param pointer 释放的对象
     */
    @Override
    public void releaseMemory(Pointer pointer) {

    }
}
