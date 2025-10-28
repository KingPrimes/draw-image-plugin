package io.github.kingprimes;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.*;
import io.github.kingprimes.model.enums.SyndicateEnum;
import io.github.kingprimes.model.worldstate.*;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

/**
 * JNA 适配器
 *
 * @author KingPrimes
 * @version 1.0.2
 */
public final class JNADrawPluginAdapter implements DrawImagePlugin {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final NativeDrawLibrary library;


    public JNADrawPluginAdapter(String libraryName) {
        try {
            this.library = Native.load(libraryName, NativeDrawLibrary.class);
        } catch (Exception e) {
            throw new RuntimeException("无法加载本地库: %s".formatted(libraryName), e);
        }
    }

    /**
     * 绘制帮助图像
     *
     * @param helpInfo 帮助信息
     * @return 图像流
     */
    @Override
    public byte[] drawHelpImage(List<String> helpInfo) {
        try {
            Pointer pointer = convertToPointer(helpInfo);
            byte[] bytes = pointerToByteArray(library.nativeDrawHelpImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawHelpImage(helpInfo);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawHelpImage(helpInfo);
        }
    }

    /**
     * 绘制所有平原图像
     *
     * @param allCycle 所有平原数据
     * @return 图像流
     */
    @Override
    public byte[] drawAllCycleImage(AllCycle allCycle) {
        try {
            Pointer pointer = convertToPointer(allCycle);
            byte[] bytes = pointerToByteArray(library.nativeDrawAllCycleImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawAllCycleImage(allCycle);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawAllCycleImage(allCycle);
        }
    }

    /**
     * 绘制所有系统信息图像
     *
     * @param allInfo 所有信息数据
     * @return 图像流
     */
    @Override
    public byte[] drawAllInfoImage(AllInfo allInfo) {
        Pointer pointer = convertToPointer(allInfo);
        return pointerToByteArray(library.nativeDrawAllInfoImage(pointer));
    }

    /**
     * 绘制所有警报图像
     *
     * @param alerts 所有警报数据
     * @return 图像流
     */
    @Override
    public byte[] drawAlertsImage(List<Alert> alerts) {
        Pointer pointer = convertToPointer(alerts);
        return pointerToByteArray(library.nativeDrawAlertsImage(pointer));
    }

    /**
     * 绘制仲裁图像
     *
     * @param arbitration 仲裁数据
     * @return 图像流
     */
    @Override
    public byte[] drawArbitrationImage(Arbitration arbitration) {
        Pointer pointer = convertToPointer(arbitration);
        return pointerToByteArray(library.nativeDrawArbitrationImage(pointer));
    }

    /**
     * 绘制有价值的仲裁图像
     *
     * @param arbitrations 有价值的仲裁数据
     * @return 图像流
     */
    @Override
    public byte[] drawArbitrationsImage(List<Arbitration> arbitrations) {
        Pointer pointer = convertToPointer(arbitrations);
        return pointerToByteArray(library.nativeDrawArbitrationsImage(pointer));
    }

    /**
     * 绘制每日交易图像
     *
     * @param dailyDeal 每日交易数据
     * @return 图像流
     */
    @Override
    public byte[] drawDailyDealsImage(DailyDeals dailyDeal) {
        Pointer pointer = convertToPointer(dailyDeal);
        return pointerToByteArray(library.nativeDrawDailyDealsImage(pointer));
    }

    /**
     * 绘制双衍王境图像
     *
     * @param duvalierCycle 双衍王境循环数据
     * @return 图像流
     */
    @Override
    public byte[] drawDuviriCycleImage(DuvalierCycle duvalierCycle) {
        Pointer pointer = convertToPointer(duvalierCycle);
        return pointerToByteArray(library.nativeDrawDuviriCycleImage(pointer));
    }

    /**
     * 绘制裂隙图像
     *
     * @param activeMission 裂隙数据
     * @return 图像流
     */
    @Override
    public byte[] drawActiveMissionImage(List<ActiveMission> activeMission) {
        Pointer pointer = convertToPointer(activeMission);
        return pointerToByteArray(library.nativeDrawActiveMissionImage(pointer));
    }

    /**
     * 绘制入侵图像
     *
     * @param invasions 入侵数据
     * @return 图像流
     */
    @Override
    public byte[] drawInvasionImage(List<Invasion> invasions) {
        Pointer pointer = convertToPointer(invasions);
        return pointerToByteArray(library.nativeDrawInvasionImage(pointer));
    }


    /**
     * 绘制1999日历季节图像
     *
     * @param knownCalendarSeasons 1999日历季节数据
     * @return 图像流
     */
    @Override
    public byte[] drawKnownCalendarSeasonsImage(List<KnownCalendarSeasons> knownCalendarSeasons) {
        Pointer pointer = convertToPointer(knownCalendarSeasons);
        return pointerToByteArray(library.nativeDrawKnownCalendarSeasonsImage(pointer));
    }


    /**
     * 绘制执刑官猎杀图像
     *
     * @param liteSorite 执刑官猎杀数据
     * @return 图像流
     */
    @Override
    public byte[] drawLiteSoriteImage(LiteSorite liteSorite) {
        Pointer pointer = convertToPointer(liteSorite);
        return pointerToByteArray(library.nativeDrawLiteSoriteImage(pointer));
    }


    /**
     * 绘制 Market 市场 金垃圾 杜卡币 图像
     *
     * @param dump 金垃圾数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketGodDumpImage(Map<Ducats.DumpType, List<Ducats.Ducat>> dump) {
        Pointer pointer = convertToPointer(dump);
        return pointerToByteArray(library.nativeDrawMarketGodDumpImage(pointer));
    }


    /**
     * 绘制 Market 市场 银垃圾 杜卡币 图像
     *
     * @param dump 银垃圾数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketSilverDumpImage(Map<Ducats.DumpType, List<Ducats.Ducat>> dump) {
        Pointer pointer = convertToPointer(dump);
        return pointerToByteArray(library.nativeDrawMarketSilverDumpImage(pointer));
    }


    /**
     * 绘制 Market Liches 市场拍卖 图像
     *
     * @param marketLichs 市场拍卖数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketLichesImage(MarketLichSister marketLichs) {
        Pointer pointer = convertToPointer(marketLichs);
        return pointerToByteArray(library.nativeDrawMarketLichesImage(pointer));
    }


    /**
     * 绘制 Market Sister 市场拍卖 图像
     *
     * @param marketSister 市场拍卖数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketSisterImage(MarketLichSister marketSister) {
        Pointer pointer = convertToPointer(marketSister);
        return pointerToByteArray(library.nativeDrawMarketSisterImage(pointer));
    }


    /**
     * 绘制 Market Orders 订单 图像
     *
     * @param orders 订单数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketOrdersImage(Orders orders) {
        Pointer pointer = convertToPointer(orders);
        return pointerToByteArray(library.nativeDrawMarketOrdersImage(pointer));
    }


    /**
     * 绘制 可能要查询的 Orders 订单 图像
     *
     * @param possibleItems 可能要查询的物品列表
     * @return 图像流
     */
    @Override
    public byte[] drawMarketOrdersImage(List<String> possibleItems) {
        Pointer pointer = convertToPointer(possibleItems);
        return pointerToByteArray(library.nativeDrawMarketOrdersImageList(pointer));
    }


    /**
     * 绘制 Market Riven 紫卡 图像
     *
     * @param marketRiven 紫卡数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketRivenImage(MarketRiven marketRiven) {
        Pointer pointer = convertToPointer(marketRiven);
        return pointerToByteArray(library.nativeDrawMarketRivenImage(pointer));
    }


    /**
     * 绘制 电波 图像
     *
     * @param seasonInfo 电波数据
     * @return 图像流
     */
    @Override
    public byte[] drawSeasonInfoImage(SeasonInfo seasonInfo) {
        Pointer pointer = convertToPointer(seasonInfo);
        return pointerToByteArray(library.nativeDrawSeasonInfoImage(pointer));
    }


    /**
     * 绘制 遗物 图像
     *
     * @param relic 遗物数据
     * @return 图像流
     */
    @Override
    public byte[] drawRelicsImage(Relics relic) {
        Pointer pointer = convertToPointer(relic);
        return pointerToByteArray(library.nativeDrawRelicsImage(pointer));
    }


    /**
     * 绘制 紫卡分析 图像
     *
     * @param rivenAnalyseTrend 紫卡分析数据
     * @return 图像流
     */
    @Override
    public byte[] drawRivenAnalyseTrendImage(List<RivenAnalyseTrend> rivenAnalyseTrend) {
        Pointer pointer = convertToPointer(rivenAnalyseTrend);
        return pointerToByteArray(library.nativeDrawRivenAnalyseTrendImage(pointer));
    }


    /**
     * 绘制 突击 图像
     *
     * @param sorties 突击数据
     * @return 图像流
     */
    @Override
    public byte[] drawSortiesImage(List<Sortie> sorties) {
        Pointer pointer = convertToPointer(sorties);
        return pointerToByteArray(library.nativeDrawSortiesImage(pointer));
    }


    /**
     * 绘制 钢铁奖励 图像
     *
     * @param steelPath 钢铁奖励数据
     * @return 图像流
     */
    @Override
    public byte[] drawSteelPath(SteelPathOffering steelPath) {
        Pointer pointer = convertToPointer(steelPath);
        return pointerToByteArray(library.nativeDrawSteelPath(pointer));
    }


    /**
     * 根据枚举绘制对应的 赏金/集团 图像
     * <p>
     * 绘制 赏金/集团 图像
     *
     * @param sm 赏金/集团 数据
     * @param se 赏金/集团 枚举
     * @return 图像流
     */
    @Override
    public byte[] drawSyndicateImage(SyndicateMission sm, SyndicateEnum se) {
        Pointer smP = convertToPointer(sm);
        Pointer seP = convertToPointer(se);
        return pointerToByteArray(library.nativeDrawSyndicateImage(smP, seP));
    }


    /**
     * 绘制 虚空商人 图像
     *
     * @param vt 虚空商人数据
     * @return 图像流
     */
    @Override
    public byte[] drawVoidTraderImage(List<VoidTrader> vt) {
        Pointer pointer = convertToPointer(vt);
        return pointerToByteArray(library.nativeDrawVoidTraderImage(pointer));

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
        try {
            Pointer sp = convertToPointer(subscribe);
            Pointer mp = convertToPointer(missionType);
            byte[] bytes = pointerToByteArray(library.nativeDrawWarframeSubscribeImage(sp, mp));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            } else {
                return new DefaultDrawImagePlugin().drawWarframeSubscribeImage(subscribe, missionType);
            }
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawWarframeSubscribeImage(subscribe, missionType);
        }
    }

    /**
     * 获取插件名称
     *
     * @return 插件名称
     */
    @Override
    public String getPluginName() {
        return library.nativeGetPluginName();
    }

    /**
     * 获取插件版本
     *
     * @return 插件版本
     */
    @Override
    public String getPluginVersion() {
        return library.nativeGetPluginVersion();
    }

    /**
     * 释放插件内存
     */
    @Override
    public void releaseMemory() {
        library.nativeReleaseMemory();
    }

    /**
     * 释放插件内存
     *
     * @param pointer 释放的对象
     */
    @Override
    public void releaseMemory(Pointer pointer) {
        library.nativeReleaseMemory(pointer);
    }

    private Pointer convertToPointer(Object obj) {
        if (obj == null) {
            return Pointer.NULL;
        }
        byte[] jsonData = objectMapper.writeValueAsBytes(obj);
        try (Memory memory = new Memory(jsonData.length + 4)
        ) {

            memory.setInt(0, jsonData.length); // 写入长度
            memory.write(4, jsonData, 0, jsonData.length);
            return memory;
        } catch (Exception e) {
            throw new RuntimeException("JSON序列化对象失败", e);
        }
    }

    private byte[] pointerToByteArray(Pointer pointer) {
        try {
            if (pointer == null || Pointer.NULL.equals(pointer)) {
                return new byte[0];
            }

            // 从Pointer读取数据长度
            int dataLength = pointer.getInt(0);
            if (dataLength <= 0) {
                return new byte[0];
            }

            return pointer.getByteArray(4, dataLength);
        } catch (Exception e) {
            throw new RuntimeException("从Pointer读取数据失败", e);
        }
    }
}
