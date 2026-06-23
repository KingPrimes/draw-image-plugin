package io.github.kingprimes.model.enums;

import lombok.Getter;

/**
 * Warframe Mod 槽位枚举<br/>
 * 使用 Warframe_Font_Icon 字体图标
 *
 * @author KingPrimes
 * @version 1.0.3
 */
@Getter
public enum PolarityEnum {

    MADURAI("\ue200"),
    VAZARIN("\ue202"),
    NARAMON("\ue203"),
    ZENURIK("\ue204"),
    UNAIRU("\ue205"),
    PENJAGA("\ue206"),
    UMBRA("\ue207"),
    Any("\ue208");
    private final String icon;

    PolarityEnum(String icon) {
        this.icon = icon;
    }

}
