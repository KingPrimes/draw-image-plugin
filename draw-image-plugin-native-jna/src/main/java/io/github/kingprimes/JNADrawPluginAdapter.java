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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * JNA 适配器
 * <p>
 * 将 Java 对象序列化为 JSON 后传入 native 动态库，
 * native 端解析 JSON 执行绘图，返回序列化后的图像字节流。
 * </p>
 *
 * <h3>内存管理</h3>
 * <ul>
 *   <li><b>输入</b>：Java → native 的 JSON 数据由 {@link Memory} 分配，native 调用完成后立即释放</li>
 *   <li><b>输出</b>：native → Java 的图像数据由 native 端分配，Java 端读取后调用 {@link NativeDrawLibrary#nativeFree(Pointer)} 释放</li>
 * </ul>
 *
 * <h3>Virtual Thread 兼容</h3>
 * 所有 JNA 原生调用会 pin 住 VT 载体线程。
 * 本类提供静态平台线程池 {@link #PLATFORM_EXECUTOR}，
 * 调用方可使用 {@link #supplyOnPlatformThread(Callable)} 将原生调用委托到平台线程执行。
 *
 * @author KingPrimes
 * @version 1.0.3
 */
public final class JNADrawPluginAdapter implements DrawImagePlugin {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int JNA_POOL_SIZE = Math.max(1,
            Integer.getInteger("draw.plugin.jna.pool.size", 1));
    private static final ExecutorService PLATFORM_EXECUTOR =
            Executors.newFixedThreadPool(JNA_POOL_SIZE, r -> {
                Thread t = new Thread(r, "jna-platform-worker");
                t.setDaemon(true);
                return t;
            });

    private final NativeDrawLibrary library;
    private final DefaultDrawImagePlugin fallback = new DefaultDrawImagePlugin();

    public JNADrawPluginAdapter(String libraryName) {
        try {
            this.library = Native.load(libraryName, NativeDrawLibrary.class);
        } catch (Exception e) {
            throw new RuntimeException("无法加载本地库: %s".formatted(libraryName), e);
        }
    }

    public JNADrawPluginAdapter(String libraryName, String absolutePath) {
        try {
            this.library = Native.load(absolutePath, NativeDrawLibrary.class);
        } catch (Exception e) {
            throw new RuntimeException("无法加载本地库: %s @ %s".formatted(libraryName, absolutePath), e);
        }
    }

    /**
     * 将原生调用委托到平台线程执行，避免 Virtual Thread pin 住 carrier。
     * 调用方可自行包装 JNA 调用：
     * <pre>{@code
     *   adapter.supplyOnPlatformThread(() -> adapter.drawHelpImage(data));
     * }</pre>
     */
    public static <T> T supplyOnPlatformThread(Callable<T> task) {
        try {
            return PLATFORM_EXECUTOR.submit(task).get();
        } catch (Exception e) {
            throw new RuntimeException("平台线程执行失败", e);
        }
    }

    // ==================== drawXXX 单参数方法 ====================

    @Override
    public byte[] drawHelpImage(List<String> helpInfo) {
        Pointer input = serializeToPointer(helpInfo);
        try { return readAndFree(library.nativeDrawHelpImage(input)); }
        catch (Exception e) { return fallback.drawHelpImage(helpInfo); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawAllCycleImage(AllCycle allCycle) {
        Pointer input = serializeToPointer(allCycle);
        try { return readAndFree(library.nativeDrawAllCycleImage(input)); }
        catch (Exception e) { return fallback.drawAllCycleImage(allCycle); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawAllInfoImage(AllInfo allInfo) {
        Pointer input = serializeToPointer(allInfo);
        try { return readAndFree(library.nativeDrawAllInfoImage(input)); }
        catch (Exception e) { return fallback.drawAllInfoImage(allInfo); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawAlertsImage(List<Alert> alerts) {
        Pointer input = serializeToPointer(alerts);
        try { return readAndFree(library.nativeDrawAlertsImage(input)); }
        catch (Exception e) { return fallback.drawAlertsImage(alerts); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawArbitrationImage(Arbitration arbitration) {
        Pointer input = serializeToPointer(arbitration);
        try { return readAndFree(library.nativeDrawArbitrationImage(input)); }
        catch (Exception e) { return fallback.drawArbitrationImage(arbitration); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawArbitrationsImage(List<Arbitration> arbitrations) {
        Pointer input = serializeToPointer(arbitrations);
        try { return readAndFree(library.nativeDrawArbitrationsImage(input)); }
        catch (Exception e) { return fallback.drawArbitrationsImage(arbitrations); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawDailyDealsImage(DailyDeals dailyDeal) {
        Pointer input = serializeToPointer(dailyDeal);
        try { return readAndFree(library.nativeDrawDailyDealsImage(input)); }
        catch (Exception e) { return fallback.drawDailyDealsImage(dailyDeal); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawDuviriCycleImage(DuvalierCycle duvalierCycle) {
        Pointer input = serializeToPointer(duvalierCycle);
        try { return readAndFree(library.nativeDrawDuviriCycleImage(input)); }
        catch (Exception e) { return fallback.drawDuviriCycleImage(duvalierCycle); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawActiveMissionImage(List<ActiveMission> activeMission) {
        Pointer input = serializeToPointer(activeMission);
        try { return readAndFree(library.nativeDrawActiveMissionImage(input)); }
        catch (Exception e) { return fallback.drawActiveMissionImage(activeMission); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawInvasionImage(List<Invasion> invasions) {
        Pointer input = serializeToPointer(invasions);
        try { return readAndFree(library.nativeDrawInvasionImage(input)); }
        catch (Exception e) { return fallback.drawInvasionImage(invasions); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawKnownCalendarSeasonsImage(List<KnownCalendarSeasons> knownCalendarSeasons) {
        Pointer input = serializeToPointer(knownCalendarSeasons);
        try { return readAndFree(library.nativeDrawKnownCalendarSeasonsImage(input)); }
        catch (Exception e) { return fallback.drawKnownCalendarSeasonsImage(knownCalendarSeasons); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawLiteSoriteImage(LiteSorite liteSorite) {
        Pointer input = serializeToPointer(liteSorite);
        try { return readAndFree(library.nativeDrawLiteSoriteImage(input)); }
        catch (Exception e) { return fallback.drawLiteSoriteImage(liteSorite); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawMarketGodDumpImage(Map<Ducats.DumpType, List<Ducats.Ducat>> dump) {
        Pointer input = serializeToPointer(dump);
        try { return readAndFree(library.nativeDrawMarketGodDumpImage(input)); }
        catch (Exception e) { return fallback.drawMarketGodDumpImage(dump); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawMarketSilverDumpImage(Map<Ducats.DumpType, List<Ducats.Ducat>> dump) {
        Pointer input = serializeToPointer(dump);
        try { return readAndFree(library.nativeDrawMarketSilverDumpImage(input)); }
        catch (Exception e) { return fallback.drawMarketSilverDumpImage(dump); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawMarketLichesImage(MarketLichSister marketLichs) {
        Pointer input = serializeToPointer(marketLichs);
        try { return readAndFree(library.nativeDrawMarketLichesImage(input)); }
        catch (Exception e) { return fallback.drawMarketLichesImage(marketLichs); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawMarketSisterImage(MarketLichSister marketSister) {
        Pointer input = serializeToPointer(marketSister);
        try { return readAndFree(library.nativeDrawMarketSisterImage(input)); }
        catch (Exception e) { return fallback.drawMarketSisterImage(marketSister); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawMarketOrdersImage(Orders orders) {
        Pointer input = serializeToPointer(orders);
        try { return readAndFree(library.nativeDrawMarketOrdersImage(input)); }
        catch (Exception e) { return fallback.drawMarketOrdersImage(orders); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawMarketOrdersImage(List<String> possibleItems) {
        Pointer input = serializeToPointer(possibleItems);
        try { return readAndFree(library.nativeDrawMarketOrdersImageList(input)); }
        catch (Exception e) { return fallback.drawMarketOrdersImage(possibleItems); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawMarketRivenImage(MarketRiven marketRiven) {
        Pointer input = serializeToPointer(marketRiven);
        try { return readAndFree(library.nativeDrawMarketRivenImage(input)); }
        catch (Exception e) { return fallback.drawMarketRivenImage(marketRiven); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawSeasonInfoImage(SeasonInfo seasonInfo) {
        Pointer input = serializeToPointer(seasonInfo);
        try { return readAndFree(library.nativeDrawSeasonInfoImage(input)); }
        catch (Exception e) { return fallback.drawSeasonInfoImage(seasonInfo); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawRelicsImage(List<Relics> relics) {
        Pointer input = serializeToPointer(relics);
        try { return readAndFree(library.nativeDrawRelicsImage(input)); }
        catch (Exception e) { return fallback.drawRelicsImage(relics); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawRivenAnalyseTrendImage(List<RivenAnalyseTrendModel> rivenAnalyseTrendModel) {
        Pointer input = serializeToPointer(rivenAnalyseTrendModel);
        try { return readAndFree(library.nativeDrawRivenAnalyseTrendImage(input)); }
        catch (Exception e) { return fallback.drawRivenAnalyseTrendImage(rivenAnalyseTrendModel); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawSortiesImage(Sortie sorties) {
        Pointer input = serializeToPointer(sorties);
        try { return readAndFree(library.nativeDrawSortiesImage(input)); }
        catch (Exception e) { return fallback.drawSortiesImage(sorties); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawSteelPath(SteelPathOffering steelPath) {
        Pointer input = serializeToPointer(steelPath);
        try { return readAndFree(library.nativeDrawSteelPath(input)); }
        catch (Exception e) { return fallback.drawSteelPath(steelPath); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawSyndicateImage(SyndicateMission sm) {
        Pointer input = serializeToPointer(sm);
        try { return readAndFree(library.nativeDrawSyndicateImage(input)); }
        catch (Exception e) { return fallback.drawSyndicateImage(sm); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawVoidTraderImage(List<VoidTrader> vt) {
        Pointer input = serializeToPointer(vt);
        try { return readAndFree(library.nativeDrawVoidTraderImage(input)); }
        catch (Exception e) { return fallback.drawVoidTraderImage(vt); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawConquestImage(List<Conquest> conquests) {
        Pointer input = serializeToPointer(conquests);
        try { return readAndFree(library.nativeDrawConquestImage(input)); }
        catch (Exception e) { return fallback.drawConquestImage(conquests); }
        finally { freePointer(input); }
    }

    @Override
    public byte[] drawDescentImage(List<Descent> descents) {
        Pointer input = serializeToPointer(descents);
        try { return readAndFree(library.nativeDrawDescentImage(input)); }
        catch (Exception e) { return fallback.drawDescentImage(descents); }
        finally { freePointer(input); }
    }

    // ==================== drawXXX 多参数方法 ====================

    @Override
    public byte[] drawWarframeSubscribeImage(Map<Integer, String> subscribe,
                                              Map<Integer, String> missionType,
                                              Map<Integer, String> invasionReward) {
        Pointer sp = serializeToPointer(subscribe);
        Pointer mp = serializeToPointer(missionType);
        Pointer ir = serializeToPointer(invasionReward);
        try {
            return readAndFree(library.nativeDrawWarframeSubscribeImage(sp, mp, ir));
        } catch (Exception e) {
            return fallback.drawWarframeSubscribeImage(subscribe, missionType, invasionReward);
        } finally {
            freePointer(sp);
            freePointer(mp);
            freePointer(ir);
        }
    }

    // ==================== 插件元信息 ====================

    @Override
    public String getPluginName() {
        return library.nativeGetPluginName();
    }

    @Override
    public String getPluginVersion() {
        return library.nativeGetPluginVersion();
    }

    @Override
    public void releaseMemory() {
        library.nativeReleaseMemory();
    }

    /**
     * 释放指定原生指针的内存（JNA 专用）
     *
     * @param nativeHandle 需要释放的原生句柄（JNA Pointer 的地址值）
     */
    public void releaseMemory(long nativeHandle) {
        library.nativeReleaseMemory(new Pointer(nativeHandle));
    }

    // ==================== 内部工具方法 ====================

    /**
     * 将 Java 对象序列化为 JSON 并写入 JNA Memory。
     * <p>
     * 调用方必须在 native 调用完成后调用 {@link #freePointer(Pointer)} 释放返回的 Memory。
     * </p>
     *
     * @param obj 要序列化的 Java 对象
     * @return 指向 JSON 数据的 Pointer（格式：[4字节长度][JSON数据]），
     *         传入 null 时返回 {@link Pointer#NULL}
     */
    private static Pointer serializeToPointer(Object obj) {
        if (obj == null) return Pointer.NULL;
        byte[] jsonData = objectMapper.writeValueAsBytes(obj);
        Memory memory = new Memory(jsonData.length + 4);
        memory.setInt(0, jsonData.length);
        memory.write(4, jsonData, 0, jsonData.length);
        return memory;
    }

    /**
     * 从 native 返回的 Pointer 中读取字节数组，然后释放该 Pointer。
     * <p>
     * native 端分配的内存格式：[4字节数据长度][实际数据]，
     * 读取完成后调用 {@link NativeDrawLibrary#nativeFree(Pointer)} 释放。
     * </p>
     *
     * @param pointer native 返回的 Pointer
     * @return 图像字节数组
     * @throws RuntimeException 指针为空或数据为空时抛出，触发调用方的 fallback
     */
    private byte[] readAndFree(Pointer pointer) {
        if (pointer == null || Pointer.NULL.equals(pointer)) {
            throw new RuntimeException("native 返回了空指针，将回退到默认实现");
        }
        try {
            int dataLength = pointer.getInt(0);
            if (dataLength <= 0) {
                throw new RuntimeException("native 返回了空数据，将回退到默认实现");
            }
            return pointer.getByteArray(4, dataLength);
        } finally {
            library.nativeFree(pointer);
        }
    }

    /**
     * 释放非空且非 NULL 的 Pointer 内存。
     * 用于释放 {@link #serializeToPointer(Object)} 分配的输入 Memory。
     */
    private static void freePointer(Pointer ptr) {
        if (ptr != null && !Pointer.NULL.equals(ptr)) {
            Native.free(Pointer.nativeValue(ptr));
        }
    }
}
