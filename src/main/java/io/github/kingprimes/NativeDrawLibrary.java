package io.github.kingprimes;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

/**
 * JNA 接口，用于调用C++库
 *
 * @author KingPrimes
 * @version 1.0.2
 */
public interface NativeDrawLibrary extends Library {

    // 插件信息函数

    /**
     * 获取插件名称
     *
     * @return 插件名称
     */
    String nativeGetPluginName();

    /**
     * 获取插件版本
     *
     * @return 插件版本
     */
    String nativeGetPluginVersion();

    /**
     * 绘制帮助图像
     *
     * @param helpInfo 帮助信息
     * @return 图像流
     */
    Pointer nativeDrawHelpImage(Pointer helpInfo);


    /**
     * 绘制所有平原图像
     *
     * @param allCycle 所有平原数据
     * @return 图像流
     */
    Pointer nativeDrawAllCycleImage(Pointer allCycle);

    /**
     * 绘制所有系统信息图像
     *
     * @param allInfo 所有信息数据
     * @return 图像流
     */
    Pointer nativeDrawAllInfoImage(Pointer allInfo);

    /**
     * 绘制所有警报图像
     *
     * @param alerts 所有警报数据
     * @return 图像流
     */
    Pointer nativeDrawAlertsImage(Pointer alerts);


    /**
     * 绘制仲裁图像
     *
     * @param arbitration 仲裁数据
     * @return 图像流
     */
    Pointer nativeDrawArbitrationImage(Pointer arbitration);

    /**
     * 绘制有价值的仲裁图像
     *
     * @param arbitrations 有价值的仲裁数据
     * @return 图像流
     */
    Pointer nativeDrawArbitrationsImage(Pointer arbitrations);

    /**
     * 绘制每日交易图像
     *
     * @param dailyDeal 每日交易数据
     * @return 图像流
     */
    Pointer nativeDrawDailyDealsImage(Pointer dailyDeal);

    /**
     * 绘制双衍王境图像
     *
     * @param duvalierCycle 双衍王境循环数据
     * @return 图像流
     */
    Pointer nativeDrawDuviriCycleImage(Pointer duvalierCycle);

    /**
     * 绘制裂隙图像
     *
     * @param activeMission 裂隙数据
     * @return 图像流
     */
    Pointer nativeDrawActiveMissionImage(Pointer activeMission);

    /**
     * 绘制入侵图像
     *
     * @param invasions 入侵数据
     * @return 图像流
     */
    Pointer nativeDrawInvasionImage(Pointer invasions);

    /**
     * 绘制1999日历季节图像
     *
     * @param knownCalendarSeasons 1999日历季节数据
     * @return 图像流
     */
    Pointer nativeDrawKnownCalendarSeasonsImage(Pointer knownCalendarSeasons);

    /**
     * 绘制 Market 市场 金垃圾 杜卡币 图像
     *
     * @param dump 金垃圾数据
     * @return 图像流
     */
    Pointer nativeDrawMarketGodDumpImage(Pointer dump);

    /**
     * 绘制 Market 市场 银垃圾 杜卡币 图像
     *
     * @param dump 银垃圾数据
     * @return 图像流
     */
    Pointer nativeDrawMarketSilverDumpImage(Pointer dump);

    /**
     * 绘制 Market Liches 市场拍卖 图像
     *
     * @param marketLichs 市场拍卖数据
     * @return 图像流
     */
    Pointer nativeDrawMarketLichesImage(Pointer marketLichs);

    /**
     * 绘制执刑官猎杀图像
     *
     * @param liteSorite 执刑官猎杀数据
     * @return 图像流
     */
    Pointer nativeDrawLiteSoriteImage(Pointer liteSorite);

    /**
     * 绘制 Market Sister 市场拍卖 图像
     *
     * @param marketSister 市场拍卖数据
     * @return 图像流
     */
    Pointer nativeDrawMarketSisterImage(Pointer marketSister);

    /**
     * 绘制 Market Orders 订单 图像
     *
     * @param orders 订单数据
     * @return 图像流
     */
    Pointer nativeDrawMarketOrdersImage(Pointer orders);

    /**
     * 绘制 可能要查询的 Orders 订单 图像
     *
     * @param possibleItems 可能要查询的物品列表
     * @return 图像流
     */
    Pointer nativeDrawMarketOrdersImageList(Pointer possibleItems);

    /**
     * 绘制 Market Riven 紫卡 图像
     *
     * @param marketRiven 紫卡数据
     * @return 图像流
     */
    Pointer nativeDrawMarketRivenImage(Pointer marketRiven);

    /**
     * 绘制 电波 图像
     *
     * @param seasonInfo 电波数据
     * @return 图像流
     */
    Pointer nativeDrawSeasonInfoImage(Pointer seasonInfo);

    /**
     * 绘制 遗物 图像
     *
     * @param relic 遗物数据
     * @return 图像流
     */
    Pointer nativeDrawRelicsImage(Pointer relic);

    /**
     * 绘制 紫卡分析 图像
     *
     * @param rivenAnalyseTrend 紫卡分析数据
     * @return 图像流
     */
    Pointer nativeDrawRivenAnalyseTrendImage(Pointer rivenAnalyseTrend);

    /**
     * 绘制 突击 图像
     *
     * @param sorties 突击数据
     * @return 图像流
     */
    Pointer nativeDrawSortiesImage(Pointer sorties);

    /**
     * 绘制 钢铁奖励 图像
     *
     * @param steelPath 钢铁奖励数据
     * @return 图像流
     */
    Pointer nativeDrawSteelPath(Pointer steelPath);

    /**
     * 根据枚举绘制对应的 赏金/集团 图像
     * <p>
     * 绘制 赏金/集团 图像
     *
     * @param sm 赏金/集团 数据
     * @param se 赏金/集团 枚举
     * @return 图像流
     */
    Pointer nativeDrawSyndicateImage(Pointer sm, Pointer se);

    /**
     * 绘制 虚空商人 图像
     *
     * @param vt 虚空商人数据
     * @return 图像流
     */
    Pointer nativeDrawVoidTraderImage(Pointer vt);

    /**
     * 绘制 订阅 帮助 图像
     *
     * @param subscribe   订阅类型数据
     * @param missionType 订阅任务类型数据
     * @return 图像流
     */
    Pointer nativeDrawWarframeSubscribeImage(Pointer subscribe, Pointer missionType);

    /**
     * 释放插件内存
     */
    void nativeReleaseMemory();

    /**
     * 释放插件内存
     *
     * @param pointer 释放的对象
     */
    void nativeReleaseMemory(Pointer pointer);
}
