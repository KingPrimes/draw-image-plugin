package io.github.kingprimes.model.enums;

import lombok.Getter;

import java.awt.*;

/**
 * Warframe 派系:Faction 枚举
 * <br/>
 * 使用 Warframe_Font_Icon 字体图标
 *
 * @author KingPrimes
 * @version 1.0.0
 * @see <a href="https://wiki.warframe.com/w/Factions">Warframe Faction</a>
 */
@Getter
public enum FactionEnum {
    /**
     * Grineer
     */
    FC_GRINEER("Grineer", new Color(0x870507), "\ue401"),

    /**
     * Corpus
     */
    FC_CORPUS("Corpus", new Color(0x5AB0CB), "\ue402"),

    /**
     * Infestation
     */
    FC_INFESTATION("Infested", new Color(0x61814B), "\ue403"),
    /**
     * 奥罗金
     */
    FC_OROKIN("奥罗金", new Color(0xB6A019), "\ue404"),
    /**
     * 堕落者
     */
    FC_CORRUPTED("堕落者", new Color(0xB6A019), "\ue404"),

    /**
     * Sentient
     */
    FC_SENTIENT("Sentient", new Color(0x931B1B), "\ue405"),

    /**
     * 合一众
     */
    FC_NARMER("合一众", new Color(0xA35F27), "\ue409"),

    /**
     * 低语者
     */
    FC_MURMUR("低语者", new Color(0xB59B6D), "\ue410"),

    /**
     * 炽蛇军
     */
    FC_SCALDRA("炽蛇军", new Color(0xDC8E12), "\ue411"),

    /**
     * 科腐者
     */
    FC_TECHROT("科腐者", new Color(0x17A36A), "\ue412"),

    /**
     * 双衍王境
     */
    FC_DUVIRI("双衍王境", new Color(0x000000), ""),

    /**
     * 墙中人
     */
    FC_MITW("墙中人", new Color(0x000000), ""),

    /**
     * TENNO
     */
    FC_TENNO("TENNO", new Color(0x000000), "\ue400"),

    FC_CROSSFIRE("多方交战", new Color(0x591A9C), "\ue413"),

    /**
     * 未知派系
     */
    FC_NONE("未知派系", new Color(0x000000), ""),
    ;
    private final String name;
    private final Color color;
    private final String icon;

    FactionEnum(String name, Color color, String icon) {
        this.name = name;
        this.color = color;
        this.icon = icon;
    }
}
