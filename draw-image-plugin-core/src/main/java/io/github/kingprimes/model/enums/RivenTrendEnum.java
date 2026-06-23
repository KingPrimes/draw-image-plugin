package io.github.kingprimes.model.enums;

import lombok.Getter;

/**
 * 紫卡倾向枚举
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@Getter
public enum RivenTrendEnum {
    /**
     * 1点
     */
    RIVEN_TREND_1("●○○○○"),
    /**
     * 2点
     */
    RIVEN_TREND_2("●●○○○"),

    /**
     * 3点
     */
    RIVEN_TREND_3("●●●○○"),

    /**
     * 4点
     */
    RIVEN_TREND_4("●●●●○"),

    /**
     * 5点
     */
    RIVEN_TREND_5("●●●●●"),
    ;

    final String doc;

    RivenTrendEnum(String doc) {
        this.doc = doc;
    }


    /**
     * 获取紫卡倾向点数
     *
     * @param dot 倾向点数
     * @return 对应倾向点数的文档描述字符串
     */
    public static String getRivenTrendDot(double dot) {
        if (dot < 0.7) {
            return RIVEN_TREND_1.doc;
        } else if (dot >= 0.7 && dot < 0.9) {
            return RIVEN_TREND_2.doc;
        } else if (dot >= 0.9 && dot < 1.15) {
            return RIVEN_TREND_3.doc;
        } else if (dot >= 1.15 && dot < 1.3) {
            return RIVEN_TREND_4.doc;
        } else if (dot >= 1.3) {
            return RIVEN_TREND_5.doc;
        }
        return null;
    }
}
