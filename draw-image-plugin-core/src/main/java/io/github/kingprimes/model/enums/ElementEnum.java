package io.github.kingprimes.model.enums;

import lombok.Getter;

import java.awt.*;

/**
 * Warframe 元素枚举
 * <br/>
 * 使用 Warframe_Font_Icon 字体图标
 * <p>
 * Name: 元素名称
 * Color: 元素颜色
 * Icon: 元素图标 ，使用 unicode字符表示加载
 *
 * @author KingPrimes
 * @version 1.0.3
 */
@Getter
public enum ElementEnum {
    ELECTRICITY("电击", new Color(0x955ab9), "\ue601"),
    IMPACT("冲击", new Color(0x929595), "\ue602"),
    RADIATION("辐射", new Color(0x9E940C), "\ue603"),
    MAGNETIC("磁力", new Color(0x50575e), "\ue604"),
    COLD("冰冻", new Color(0x2c53b5), "\ue605"),
    TOXIN("毒素", new Color(0x4f822f), "\ue606"),
    HEAT("火焰", new Color(0xe56e0c), "\ue607"),
    PUNCTURE("穿刺", new Color(0x8d8676), "\ue608"),
    SLASH("切割", new Color(0x876769), "\ue609"),
    BLAST("爆炸", new Color(0xa86668), "\ue610"),
    CORROSIVE("腐蚀", new Color(0xb2ca01), "\ue611"),
    GAS("毒气", new Color(0x3ac490), "\ue612"),
    VIRAL("病毒", new Color(0xe374b6), "\ue613"),
    VOID("虚空", new Color(0x016468), "\ue614"),
    TAU("TAU", new Color(0xdf191e), "\ue615"),
    TRUE("TRUE", new Color(0x66582e), "\ue616"),

    ;

    final String NAME;
    final Color COLOR;
    final String ICON;

    ElementEnum(String name, Color color, String icon) {
        this.NAME = name;
        this.COLOR = color;
        this.ICON = icon;
    }
}
