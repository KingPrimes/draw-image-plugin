package io.github.kingprimes.model.enums;

import lombok.Getter;

/**
 * 图标枚举
 * <p>使用 Warframe_Font_Icon 字体图标</p>
 *
 * @author KingPrimes
 * @version 1.0.3
 */
@Getter
public enum IconEnum {
    /**
     * 😊图标
     */
    SMILE("\ue300"),
    /**
     * 😐图标
     */
    MEH("\ue302"),
    /**
     * 数量图标
     */
    CUBES("\ue304"),
    /**
     * 杜卡币图标
     */
    DUCATS("\ue305"),
    /**
     * 星币图标
     */
    CREDITS("\ue306"),
    /**
     * 阿耶精华图标
     */
    AYAN("\ue308"),
    /**
     * 白金图标
     */
    PLATINUM("\ue309"),
    /**
     * 雪花图标
     */
    COLD("\ue100"),
    /**
     * 太阳图标
     */
    SUN("\ue101"),
    /**
     * 月亮图标
     */
    NIGHT("\ue102");

    final String icon;

    IconEnum(String icon) {
        this.icon = icon;
    }
}
