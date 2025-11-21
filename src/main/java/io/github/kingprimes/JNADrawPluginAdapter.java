package io.github.kingprimes;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import io.github.kingprimes.defaultdraw.DefaultDrawImagePlugin;
import io.github.kingprimes.model.*;
import io.github.kingprimes.model.market.MarketLichSister;
import io.github.kingprimes.model.market.MarketRiven;
import io.github.kingprimes.model.market.Orders;
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
        try {
            Pointer pointer = convertToPointer(allInfo);
            byte[] bytes = pointerToByteArray(library.nativeDrawAllInfoImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawAllInfoImage(allInfo);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawAllInfoImage(allInfo);
        }
    }

    /**
     * 绘制所有警报图像
     *
     * @param alerts 所有警报数据
     * @return 图像流
     */
    @Override
    public byte[] drawAlertsImage(List<Alert> alerts) {
        try {
            Pointer pointer = convertToPointer(alerts);
            byte[] bytes = pointerToByteArray(library.nativeDrawAlertsImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawAlertsImage(alerts);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawAlertsImage(alerts);
        }
    }

    /**
     * 绘制仲裁图像
     *
     * @param arbitration 仲裁数据
     * @return 图像流
     */
    @Override
    public byte[] drawArbitrationImage(Arbitration arbitration) {
        try {
            Pointer pointer = convertToPointer(arbitration);
            byte[] bytes = pointerToByteArray(library.nativeDrawArbitrationImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawArbitrationImage(arbitration);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawArbitrationImage(arbitration);
        }
    }

    /**
     * 绘制有价值的仲裁图像
     *
     * @param arbitrations 有价值的仲裁数据
     * @return 图像流
     */
    @Override
    public byte[] drawArbitrationsImage(List<Arbitration> arbitrations) {
        try {
            Pointer pointer = convertToPointer(arbitrations);
            byte[] bytes = pointerToByteArray(library.nativeDrawArbitrationsImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawArbitrationsImage(arbitrations);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawArbitrationsImage(arbitrations);
        }
    }

    /**
     * 绘制每日交易图像
     *
     * @param dailyDeal 每日交易数据
     * @return 图像流
     */
    @Override
    public byte[] drawDailyDealsImage(DailyDeals dailyDeal) {
        try {
            Pointer pointer = convertToPointer(dailyDeal);
            byte[] bytes = pointerToByteArray(library.nativeDrawDailyDealsImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawDailyDealsImage(dailyDeal);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawDailyDealsImage(dailyDeal);
        }
    }

    /**
     * 绘制双衍王境图像
     *
     * @param duvalierCycle 双衍王境循环数据
     * @return 图像流
     */
    @Override
    public byte[] drawDuviriCycleImage(DuvalierCycle duvalierCycle) {
        try {
            Pointer pointer = convertToPointer(duvalierCycle);
            byte[] bytes = pointerToByteArray(library.nativeDrawDuviriCycleImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawDuviriCycleImage(duvalierCycle);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawDuviriCycleImage(duvalierCycle);
        }
    }

    /**
     * 绘制裂隙图像
     *
     * @param activeMission 裂隙数据
     * @return 图像流
     */
    @Override
    public byte[] drawActiveMissionImage(List<ActiveMission> activeMission) {
        try {
            Pointer pointer = convertToPointer(activeMission);
            byte[] bytes = pointerToByteArray(library.nativeDrawActiveMissionImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawActiveMissionImage(activeMission);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawActiveMissionImage(activeMission);
        }
    }

    /**
     * 绘制入侵图像
     *
     * @param invasions 入侵数据
     * @return 图像流
     */
    @Override
    public byte[] drawInvasionImage(List<Invasion> invasions) {
        try {
            Pointer pointer = convertToPointer(invasions);
            byte[] bytes = pointerToByteArray(library.nativeDrawInvasionImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawInvasionImage(invasions);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawInvasionImage(invasions);
        }
    }


    /**
     * 绘制1999日历季节图像
     *
     * @param knownCalendarSeasons 1999日历季节数据
     * @return 图像流
     */
    @Override
    public byte[] drawKnownCalendarSeasonsImage(List<KnownCalendarSeasons> knownCalendarSeasons) {
        try {
            Pointer pointer = convertToPointer(knownCalendarSeasons);
            byte[] bytes = pointerToByteArray(library.nativeDrawKnownCalendarSeasonsImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawKnownCalendarSeasonsImage(knownCalendarSeasons);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawKnownCalendarSeasonsImage(knownCalendarSeasons);
        }
    }


    /**
     * 绘制执刑官猎杀图像
     *
     * @param liteSorite 执刑官猎杀数据
     * @return 图像流
     */
    @Override
    public byte[] drawLiteSoriteImage(LiteSorite liteSorite) {
        try {
            Pointer pointer = convertToPointer(liteSorite);
            byte[] bytes = pointerToByteArray(library.nativeDrawLiteSoriteImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawLiteSoriteImage(liteSorite);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawLiteSoriteImage(liteSorite);
        }
    }


    /**
     * 绘制 Market 市场 金垃圾 杜卡币 图像
     *
     * @param dump 金垃圾数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketGodDumpImage(Map<Ducats.DumpType, List<Ducats.Ducat>> dump) {
        try {
            Pointer pointer = convertToPointer(dump);
            byte[] bytes = pointerToByteArray(library.nativeDrawMarketGodDumpImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawMarketGodDumpImage(dump);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawMarketGodDumpImage(dump);
        }
    }


    /**
     * 绘制 Market 市场 银垃圾 杜卡币 图像
     *
     * @param dump 银垃圾数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketSilverDumpImage(Map<Ducats.DumpType, List<Ducats.Ducat>> dump) {
        try {
            Pointer pointer = convertToPointer(dump);
            byte[] bytes = pointerToByteArray(library.nativeDrawMarketSilverDumpImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawMarketSilverDumpImage(dump);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawMarketSilverDumpImage(dump);
        }
    }


    /**
     * 绘制 Market Liches 市场拍卖 图像
     *
     * @param marketLichs 市场拍卖数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketLichesImage(MarketLichSister marketLichs) {
        try {
            Pointer pointer = convertToPointer(marketLichs);
            byte[] bytes = pointerToByteArray(library.nativeDrawMarketLichesImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawMarketLichesImage(marketLichs);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawMarketLichesImage(marketLichs);
        }
    }


    /**
     * 绘制 Market Sister 市场拍卖 图像
     *
     * @param marketSister 市场拍卖数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketSisterImage(MarketLichSister marketSister) {
        try {
            Pointer pointer = convertToPointer(marketSister);
            byte[] bytes = pointerToByteArray(library.nativeDrawMarketSisterImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawMarketSisterImage(marketSister);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawMarketSisterImage(marketSister);
        }
    }


    /**
     * 绘制 Market Orders 订单 图像
     *
     * @param orders 订单数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketOrdersImage(Orders orders) {
        try {
            Pointer pointer = convertToPointer(orders);
            byte[] bytes = pointerToByteArray(library.nativeDrawMarketOrdersImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawMarketOrdersImage(orders);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawMarketOrdersImage(orders);
        }
    }


    /**
     * 绘制 可能要查询的 Orders 订单 图像
     *
     * @param possibleItems 可能要查询的物品列表
     * @return 图像流
     */
    @Override
    public byte[] drawMarketOrdersImage(List<String> possibleItems) {
        try {
            Pointer pointer = convertToPointer(possibleItems);
            byte[] bytes = pointerToByteArray(library.nativeDrawMarketOrdersImageList(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawMarketOrdersImage(possibleItems);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawMarketOrdersImage(possibleItems);
        }
    }


    /**
     * 绘制 Market Riven 紫卡 图像
     *
     * @param marketRiven 紫卡数据
     * @return 图像流
     */
    @Override
    public byte[] drawMarketRivenImage(MarketRiven marketRiven) {
        try {
            Pointer pointer = convertToPointer(marketRiven);
            byte[] bytes = pointerToByteArray(library.nativeDrawMarketRivenImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawMarketRivenImage(marketRiven);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawMarketRivenImage(marketRiven);
        }
    }


    /**
     * 绘制 电波 图像
     *
     * @param seasonInfo 电波数据
     * @return 图像流
     */
    @Override
    public byte[] drawSeasonInfoImage(SeasonInfo seasonInfo) {
        try {
            Pointer pointer = convertToPointer(seasonInfo);
            byte[] bytes = pointerToByteArray(library.nativeDrawSeasonInfoImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawSeasonInfoImage(seasonInfo);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawSeasonInfoImage(seasonInfo);
        }
    }


    /**
     * 绘制 遗物 图像
     *
     * @param relics 遗物数据
     * @return 图像流
     */
    @Override
    public byte[] drawRelicsImage(List<Relics> relics) {
        try {
            Pointer pointer = convertToPointer(relics);
            byte[] bytes = pointerToByteArray(library.nativeDrawRelicsImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawRelicsImage(relics);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawRelicsImage(relics);
        }
    }


    /**
     * 绘制 紫卡分析 图像
     *
     * @param rivenAnalyseTrendModel 紫卡分析数据
     * @return 图像流
     */
    @Override
    public byte[] drawRivenAnalyseTrendImage(List<RivenAnalyseTrendModel> rivenAnalyseTrendModel) {
        try {
            Pointer pointer = convertToPointer(rivenAnalyseTrendModel);
            byte[] bytes = pointerToByteArray(library.nativeDrawRivenAnalyseTrendImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawRivenAnalyseTrendImage(rivenAnalyseTrendModel);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawRivenAnalyseTrendImage(rivenAnalyseTrendModel);
        }
    }


    /**
     * 绘制 突击 图像
     *
     * @param sorties 突击数据
     * @return 图像流
     */
    @Override
    public byte[] drawSortiesImage(Sortie sorties) {
        try {
            Pointer pointer = convertToPointer(sorties);
            byte[] bytes = pointerToByteArray(library.nativeDrawSortiesImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawSortiesImage(sorties);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawSortiesImage(sorties);
        }
    }


    /**
     * 绘制 钢铁奖励 图像
     *
     * @param steelPath 钢铁奖励数据
     * @return 图像流
     */
    @Override
    public byte[] drawSteelPath(SteelPathOffering steelPath) {
        try {
            Pointer pointer = convertToPointer(steelPath);
            byte[] bytes = pointerToByteArray(library.nativeDrawSteelPath(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawSteelPath(steelPath);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawSteelPath(steelPath);
        }
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
        try {
            Pointer pointer = convertToPointer(sm);
            byte[] bytes = pointerToByteArray(library.nativeDrawSyndicateImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawSyndicateImage(sm);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawSyndicateImage(sm);
        }
    }


    /**
     * 绘制 虚空商人 图像
     *
     * @param vt 虚空商人数据
     * @return 图像流
     */
    @Override
    public byte[] drawVoidTraderImage(List<VoidTrader> vt) {
        try {
            Pointer pointer = convertToPointer(vt);
            byte[] bytes = pointerToByteArray(library.nativeDrawVoidTraderImage(pointer));
            if (bytes != null && bytes.length > 0) {
                return bytes;
            }
            return new DefaultDrawImagePlugin().drawVoidTraderImage(vt);
        } catch (Exception e) {
            return new DefaultDrawImagePlugin().drawVoidTraderImage(vt);
        }

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
