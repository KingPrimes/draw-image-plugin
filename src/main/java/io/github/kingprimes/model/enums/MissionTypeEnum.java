package io.github.kingprimes.model.enums;

import lombok.Getter;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Warframe 任务类型枚举
 *
 * @author KingPrimes
 * @version 1.0.0
 * @see <a href="https://wiki.warframe.com/w/Mission">任务类型列表</a>
 */

@Getter
public enum MissionTypeEnum {

    /**
     * 刺杀
     */
    MT_ASSASSINATION("刺杀", 0),

    /**
     * 歼灭
     */
    MT_EXTERMINATION("歼灭", 1),

    /**
     * 生存
     */
    MT_SURVIVAL("生存", 2),

    /**
     * 救援
     */
    MT_RESCUE("救援", 3),


    /**
     * 破坏
     */
    MT_SABOTAGE("破坏", 4),

    /**
     * 捕获
     */
    MT_CAPTURE("捕获", 5),

    /**
     * 未知
     */
    MT_DEFAULT("未知", 6),

    /**
     * 间谍
     */
    MT_INTEL("间谍", 7),

    /**
     * 防御
     */
    MT_DEFENSE("防御", 8),

    /**
     * 移动防御
     */
    MT_MOBILE_DEFENSE("移动防御", 9),

    /**
     * 武形秘仪
     */
    MT_PVP("武形秘仪", 10),

    /**
     * 黑暗地带
     */
    MT_SECTOR("黑暗地带", 11),

    /**
     * 拦截
     */
    MT_TERRITORY("拦截", 13),

    /**
     * 清巢
     */
    MT_HIVE("清巢", 15),

    /**
     * 劫持
     */
    MT_RETRIEVAL("劫持", 14),

    /**
     * 挖掘
     */
    MT_EXCAVATE("挖掘", 17),

    /**
     * 资源回收
     */
    MT_SALVAGE("资源回收", 21),

    /**
     * 竞技场
     */
    MT_ARENA("竞技场", 22),

    /**
     * 追击
     */
    MT_PURSUIT("追击", 24),

    /**
     * 强袭
     */
    MT_ASSAULT("强袭", 26),

    /**
     * 叛逃
     */
    MT_EVACUATION("叛逃", 27),

    /**
     * 自由漫步
     */
    MT_LANDSCAPE("自由漫步", 28),

    /**
     * 中断
     */
    MT_ARTIFACT("中断", 33),

    /**
     * 中断
     */
    MT_DISRUPTION("中断", 32),

    /**
     * 虚空洪流
     */
    MT_VOID_FLOOD("虚空洪流", 34),

    /**
     * 虚空覆涌
     */
    MT_VOID_CASCADE("虚空覆涌", 35),

    /**
     * 虚空决战
     */
    MT_VOID_ARMAGEDDON("虚空决战", 36),

    /**
     * 元素转换
     */
    MT_ALCHEMY("元素转换", 38),

    /**
     * 异化区
     */
    MT_CAMBIRE("异化区", 39),

    /**
     * 传承种收割
     */
    MT_LEGACYTE_HARVEST("传承种收割", 40),

    /**
     * 祈运坛防御
     */
    MT_SHRINE_DEFENSE("祈运坛防御", 41),

    /**
     * 对战
     */
    MT_FACEOFF("对战", 42),

    /**
     * 前哨战
     */
    MT_SKIRMISH("前哨战", 60),

    /**
     * 爆发
     */
    MT_VOLATILE("爆发", 61),

    /**
     * 奥菲斯
     */
    MT_ORPHEUS("奧菲斯", 62),

    /**
     * 扬升
     */
    MT_ASCENSION("扬升", 90),

    /**
     * 中继站
     */
    MT_RELAY("中继站", 100),
    ;
    private final String name;
    private final int order;

    MissionTypeEnum(String name, int order) {
        this.name = name;
        this.order = order;
    }

    /**
     * 获取按order字段排序的所有枚举值列表
     *
     * @return 按照order升序排列的MissionTypeEnum枚举值列表
     */
    public static List<MissionTypeEnum> getOrderedValues() {
        return Arrays.stream(values())
                .sorted(Comparator.comparingInt(MissionTypeEnum::getOrder))
                .toList();
    }

    /**
     * 根据任务类型获取对应的颜色
     *
     * @return 代表该任务类型的Color对象
     */
    public static Color getColor(MissionTypeEnum missionTypeEnum) {
        return switch (missionTypeEnum) {
            case MT_ASSASSINATION -> new Color(0xff6b6b); // 刺杀 - 红色
            case MT_EXTERMINATION -> new Color(0xff9f43); // 歼灭 - 橙色
            case MT_SURVIVAL -> new Color(0xCDA241); // 生存 - 黄色
            case MT_RESCUE -> new Color(0x1dd1a1); // 救援 - 绿色
            case MT_SABOTAGE -> new Color(0x54a0ff); // 破坏 - 蓝色
            case MT_CAPTURE -> new Color(0x5f27cd); // 捕获 - 紫色
            case MT_INTEL -> new Color(0x00d2d3); // 间谍 - 青色
            case MT_DEFENSE -> new Color(0xff9ff3); // 防御 - 粉色
            case MT_MOBILE_DEFENSE -> new Color(0x75B1E6); // 移动防御 - 灰色
            case MT_TERRITORY -> new Color(0x01a3a4); // 拦截 - 深青色
            case MT_HIVE -> new Color(0x8395a7); // 清巢 - 石板灰
            case MT_RETRIEVAL -> new Color(0xffeaa7); // 劫持 - 浅黄
            case MT_EXCAVATE -> new Color(0xdda0dd); // 挖掘 - 梅花色
            case MT_SALVAGE -> new Color(0xa29bfe); // 资源回收 - 紫罗兰
            case MT_ASSAULT -> new Color(0xff7675); // 强袭 - 浅红
            case MT_EVACUATION -> new Color(0xfdcb6e); // 叛逃 - 金色
            case MT_ARTIFACT, MT_DISRUPTION -> new Color(0xe17055); // 中断 - 橘红
            case MT_VOID_FLOOD -> new Color(0x6c5ce7); // 虚空洪流 - 靛蓝
            case MT_VOID_CASCADE -> new Color(0xa29bfe); // 虚空覆涌 - 熏衣草
            case MT_VOID_ARMAGEDDON -> new Color(0x2d3436); // 虚空决战 - 深灰
            case MT_ALCHEMY -> new Color(0x00b894); // 元素转换 - 绿松石
            case MT_CAMBIRE -> new Color(0xe84393); // 异化区 - 热粉
            case MT_SHRINE_DEFENSE -> new Color(0xfd79a8); // 祈运坛防御 - 粉红
            case MT_FACEOFF -> new Color(0xe67e22); // 对战 - 胡萝卜色
            case MT_SKIRMISH -> new Color(0xe74c3c); // 前哨战 - 珊瑚红
            case MT_VOLATILE -> new Color(0xf39c12); // 爆发 - 太阳花黄
            case MT_ORPHEUS -> new Color(0x9b59b6); // 奥菲斯 - 紫水晶
            case MT_ASCENSION -> new Color(0x3498db); // 扬升 - 湛蓝
            default -> Color.BLACK; // 默认黑色
        };
    }
}
