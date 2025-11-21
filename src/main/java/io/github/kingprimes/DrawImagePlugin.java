package io.github.kingprimes;

import com.sun.jna.Pointer;
import io.github.kingprimes.model.*;
import io.github.kingprimes.model.market.MarketLichSister;
import io.github.kingprimes.model.market.MarketRiven;
import io.github.kingprimes.model.market.Orders;
import io.github.kingprimes.model.worldstate.*;

import java.util.List;
import java.util.Map;

/**
 * 绘图接口，实现类必须实现除default标记的所有方法
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public interface DrawImagePlugin {

    /**
     * 绘制帮助图像
     *
     * @param helpInfo 帮助信息
     * @return 图像流
     */
    byte[] drawHelpImage(List<String> helpInfo);

    /**
     * 绘制所有平原图像
     *
     * @param allCycle 所有平原数据
     * @return 图像流
     */
    byte[] drawAllCycleImage(AllCycle allCycle);

    /**
     * 绘制所有系统信息图像
     *
     * @param allInfo 所有信息数据
     * @return 图像流
     */
    byte[] drawAllInfoImage(AllInfo allInfo);

    /**
     * 绘制所有警报图像
     *
     * @param alerts 所有警报数据
     * @return 图像流
     */
    byte[] drawAlertsImage(List<Alert> alerts);

    /**
     * 绘制仲裁图像
     *
     * @param arbitration 仲裁数据
     * @return 图像流
     */
    byte[] drawArbitrationImage(Arbitration arbitration);


    /**
     * 绘制有价值的仲裁图像
     *
     * @param arbitrations 有价值的仲裁数据
     * @return 图像流
     */
    byte[] drawArbitrationsImage(List<Arbitration> arbitrations);


    /**
     * 绘制每日交易图像
     *
     * @param dailyDeal 每日交易数据
     * @return 图像流
     */
    byte[] drawDailyDealsImage(DailyDeals dailyDeal);

    /**
     * 绘制双衍王境图像
     *
     * @param duvalierCycle 双衍王境循环数据
     * @return 图像流
     */
    byte[] drawDuviriCycleImage(DuvalierCycle duvalierCycle);

    /**
     * 绘制裂隙图像
     *
     * @param activeMission 裂隙数据
     * @return 图像流
     */
    byte[] drawActiveMissionImage(List<ActiveMission> activeMission);

    /**
     * 绘制入侵图像
     *
     * @param invasions 入侵数据
     * @return 图像流
     */
    byte[] drawInvasionImage(List<Invasion> invasions);

    /**
     * 绘制1999日历季节图像
     *
     * @param knownCalendarSeasons 1999日历季节数据
     * @return 图像流
     */
    byte[] drawKnownCalendarSeasonsImage(List<KnownCalendarSeasons> knownCalendarSeasons);

    /**
     * 绘制执刑官猎杀图像
     *
     * @param liteSorite 执刑官猎杀数据
     * @return 图像流
     */
    byte[] drawLiteSoriteImage(LiteSorite liteSorite);

    /**
     * 绘制 Market 市场 金垃圾 杜卡币 图像
     *
     * @param dump 金垃圾数据
     * @return 图像流
     */
    byte[] drawMarketGodDumpImage(Map<Ducats.DumpType, List<Ducats.Ducat>> dump);

    /**
     * 绘制 Market 市场 银垃圾 杜卡币 图像
     *
     * @param dump 银垃圾数据
     * @return 图像流
     */
    byte[] drawMarketSilverDumpImage(Map<Ducats.DumpType, List<Ducats.Ducat>> dump);

    /**
     * 绘制 Market Liches 市场拍卖 图像
     *
     * @param marketLichs 市场拍卖数据
     * @return 图像流
     */
    byte[] drawMarketLichesImage(MarketLichSister marketLichs);

    /**
     * 绘制 Market Sister 市场拍卖 图像
     *
     * @param marketSister 市场拍卖数据
     * @return 图像流
     */
    byte[] drawMarketSisterImage(MarketLichSister marketSister);

    /**
     * 绘制 Market Orders 订单 图像
     *
     * @param orders 订单数据
     * @return 图像流
     */
    byte[] drawMarketOrdersImage(Orders orders);

    /**
     * 绘制 可能要查询的 Orders 订单 图像
     *
     * @param possibleItems 可能要查询的物品列表
     * @return 图像流
     */
    byte[] drawMarketOrdersImage(List<String> possibleItems);

    /**
     * 绘制 Market Riven 紫卡 图像
     *
     * @param marketRiven 紫卡数据
     * @return 图像流
     */
    byte[] drawMarketRivenImage(MarketRiven marketRiven);

    /**
     * 绘制 电波 图像
     *
     * @param seasonInfo 电波数据
     * @return 图像流
     */
    byte[] drawSeasonInfoImage(SeasonInfo seasonInfo);

    /**
     * 绘制 遗物 图像
     *
     * @param relics 遗物数据
     * @return 图像流
     */
    byte[] drawRelicsImage(List<Relics> relics);

    /**
     * 绘制 紫卡分析 图像
     *
     * @param rivenAnalyseTrendModel 紫卡分析数据
     * @return 图像流
     */
    byte[] drawRivenAnalyseTrendImage(List<RivenAnalyseTrendModel> rivenAnalyseTrendModel);

    /**
     * 绘制 突击 图像
     *
     * @param sorties 突击数据
     * @return 图像流
     */
    byte[] drawSortiesImage(Sortie sorties);

    /**
     * 绘制 钢铁奖励 图像
     *
     * @param steelPath 钢铁奖励数据
     * @return 图像流
     */
    byte[] drawSteelPath(SteelPathOffering steelPath);

    /**
     * 根据枚举绘制对应的 赏金/集团 图像
     * <p>
     * 绘制 赏金/集团 图像
     *
     * @param sm 赏金/集团 数据
     * @return 图像流
     */
    byte[] drawSyndicateImage(SyndicateMission sm);

    /**
     * 绘制 虚空商人 图像
     *
     * @param vt 虚空商人数据
     * @return 图像流
     */
    byte[] drawVoidTraderImage(List<VoidTrader> vt);

    /**
     * 绘制 订阅 帮助 图像
     *
     * @param subscribe   订阅类型数据
     * @param missionType 订阅任务类型数据
     * @return 图像流
     */
    byte[] drawWarframeSubscribeImage(Map<Integer, String> subscribe, Map<Integer, String> missionType);

    /**
     * 获取插件名称
     *
     * @return 插件名称
     */
    String getPluginName();

    /**
     * 获取插件版本
     *
     * @return 插件版本
     */
    String getPluginVersion();

    /**
     * 释放插件内存
     */
    void releaseMemory();

    /**
     * 释放插件内存
     *
     * @param pointer 释放的对象
     */
    void releaseMemory(Pointer pointer);
}
