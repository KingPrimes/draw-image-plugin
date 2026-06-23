package io.github.kingprimes.model.enums;

import lombok.Getter;

/**
 * 稀有度枚举
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Getter
public enum RarityEnum {
    /**
     * 常见
     */
    COMMON("常见"),

    /**
     * 罕见
     */
    UNCOMMON("罕见"),

    /**
     * 稀有
     */
    RARE("稀有"),

    /**
     * 传奇
     */
    LEGENDARY("传奇");
    final String name;

    RarityEnum(String s) {
        name = s;
    }
}
