package io.github.kingprimes.model.enums;

import lombok.Getter;

import java.awt.*;

@Getter
public enum ElementEnum {
    ELECTRICITY("电击", new Color(0x955ab9), "\ue905"),
    IMPACT("冲击", new Color(0x929595), "\ue907"),
    RADIATION("辐射", new Color(0x9E940C), "\ue90c"),
    MAGNETIC("磁力", new Color(0x50575e), "\ue908"),
    COLD("冰冻", new Color(0x2c53b5), "\ue900"),
    TOXIN("毒素", new Color(0x4f822f), "\ue90d"),
    HEAT("火焰", new Color(0xe56e0c), "\ue906"),
    PUNCTURE("穿刺", new Color(0x8d8676), "\ue90e"),
    SLASH("切割", new Color(0x876769), "\ue90f"),
    BLAST("爆炸", new Color(0xa86668), "\ue910"),
    CORROSIVE("腐蚀", new Color(0xb2ca01), "\ue911"),
    GAS("毒气", new Color(0x3ac490), "\ue912"),
    VIRAL("病毒", new Color(0xe374b6), "\ue913"),
    VOID("虚空", new Color(0x016468), "\ue914"),
    TAU("TAU", new Color(0xdf191e), "\ue915"),
    TRUE("TRUE", new Color(0x66582e), "\ue916"),

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
