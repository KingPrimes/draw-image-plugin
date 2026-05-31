package io.github.kingprimes.model.worldstate;

import io.github.kingprimes.utils.TimeUtils;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * 钢铁奖励轮换
 *
 * <p>此循环计算方式参考 <a href="https://github.com/WFCD/warframe-worldstate-parser">warframe-worldstate-parser</a> 项目</p>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Getter
public final class SteelPathOffering implements Cycle {
    private static final LocalDateTime START_DATE = LocalDateTime.of(2020, 11, 16, 0, 0, 0);
    private static final List<SteelPathReward> rotation = new ArrayList<>();
    private static final List<SteelPathReward> evergreens = new ArrayList<>();

    static {

        // Rotation 数据
        rotation.add(new SteelPathReward("Umbra Forma 蓝图", 150));
        rotation.add(new SteelPathReward("50,000 赤毒", 55));
        rotation.add(new SteelPathReward("组合枪裂罅 Mod", 75));
        rotation.add(new SteelPathReward("3x Forma", 75));
        rotation.add(new SteelPathReward("Zaw 裂罅 Mod", 75));
        rotation.add(new SteelPathReward("30,000 内融核心", 150));
        rotation.add(new SteelPathReward("步枪裂罅 Mod", 75));
        rotation.add(new SteelPathReward("霰弹枪裂罅 Mod", 75));

        // Evergreen 数据
        evergreens.add(new SteelPathReward("裂罅破解器", 20));
        evergreens.add(new SteelPathReward("军神护肩 蓝图", 15));
        evergreens.add(new SteelPathReward("军神胸甲 蓝图", 25));
        evergreens.add(new SteelPathReward("军神头盔 蓝图", 20));
        evergreens.add(new SteelPathReward("军神护胫 蓝图", 25));
        evergreens.add(new SteelPathReward("10,000 赤毒", 15));
        evergreens.add(new SteelPathReward("主要武器赋能槽连接器", 15));
        evergreens.add(new SteelPathReward("次要武器赋能槽连接器", 15));
        evergreens.add(new SteelPathReward("遗物组合包", 15));
        evergreens.add(new SteelPathReward("架式 Forma 蓝图", 10));
        evergreens.add(new SteelPathReward("三轨幻纹", 3));
        evergreens.add(new SteelPathReward("颅骨幻纹", 85));
        evergreens.add(new SteelPathReward("制衡", 35));
        evergreens.add(new SteelPathReward("Teshin 摇头娃娃", 35));
        evergreens.add(new SteelPathReward("Gauss 战斗姿态浮印", 15));
        evergreens.add(new SteelPathReward("Grendel 战斗姿态浮印", 15));
        evergreens.add(new SteelPathReward("Protea 战斗姿态浮印", 15));
        evergreens.add(new SteelPathReward("Orokin 茶具", 15));
        evergreens.add(new SteelPathReward("Xaku 战斗姿态浮印", 15));
    }

    /**
     * 开始时间
     */
    private final LocalDateTime activation;
    /**
     * 结束时间
     */
    private final Instant expiry;
    /**
     * 剩余时间
     */
    private final String remaining;
    private final Incursions incursions;

    /**
     * 当前奖励
     */
    private SteelPathReward currentReward;
    /**
     * 下一个奖励
     */
    private SteelPathReward nextReward;

    public SteelPathOffering() {

        // 计算当前周期索引
        long secondsSinceStart = ChronoUnit.SECONDS.between(START_DATE, LocalDateTime.now(ZoneOffset.UTC));
        long eightWeeks = 4838400; // 8周的秒数
        long sevenDays = 604800;   // 7天的秒数

        long ind = (secondsSinceStart % eightWeeks) / sevenDays;

        // 设置当前奖励
        if (!rotation.isEmpty()) {
            this.currentReward = rotation.get((int) (ind % rotation.size()));
            this.nextReward = rotation.get((int) ((ind + 1) % rotation.size()));
        }

        // 设置激活和过期时间
        this.activation = TimeUtils.getFirstDayOfWeek();
        this.expiry = TimeUtils.getLastDayOfWeek().atZone(ZoneOffset.UTC).toInstant();

        // 计算剩余时间
        this.remaining = TimeUtils.timeDeltaToString(this.expiry.toEpochMilli() - System.currentTimeMillis());

        // 设置时间信息
        LocalDateTime startOfDay = TimeUtils.getStartOfDay();
        this.incursions = new Incursions(
                "spi:" + startOfDay.toInstant(ZoneOffset.UTC).toEpochMilli(),
                startOfDay,
                TimeUtils.getEndOfDay()
        );
    }

    /**
     * 常驻奖励池
     *
     * @return 常驻奖励
     */
    public List<SteelPathReward> getEvergreens() {
        return evergreens;
    }

    /**
     * 内部类表示 Steel Path 奖励
     *
     * @param name 奖励名称
     * @param cost 奖励数量
     */
    public record SteelPathReward(String name, int cost) {
        @Override
        public String toString() {
            return name + " (" + cost + ")";
        }
    }

    /**
     * 内部类表示日期信息
     *
     * @param id         唯一标识符
     * @param activation 开始时间
     * @param expiry     结束时间
     */
    private record Incursions(String id, LocalDateTime activation, LocalDateTime expiry) {
    }
}
