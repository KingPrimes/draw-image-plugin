package io.github.kingprimes.model.enums;

import lombok.Getter;

import java.awt.*;

/**
 * 虚空遗物等级枚举
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Getter
public enum VoidEnum {

    /**
     * 古纪
     */
    VoidT1("古纪", new Color(0x514234)),

    /**
     * 前纪
     */
    VoidT2("前纪", new Color(0x75562B)),

    /**
     * 中纪
     */
    VoidT3("中纪", new Color(0x9F9E9E)),

    /**
     * 后纪
     */
    VoidT4("后纪", new Color(0xC1BE39)),

    /**
     * 安魂
     */
    VoidT5("安魂", new Color(0x872A2C)),

    /**
     * 全能
     */
    VoidT6("全能", new Color(0x7f8c8d)),
    ;
    private final String name;
    private final Color color;

    VoidEnum(String name, Color color) {
        this.name = name;
        this.color = color;
    }
}
