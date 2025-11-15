package io.github.kingprimes.model.enums;

import lombok.Getter;

/**
 * Market 支持的平台
 *
 * @author KingPrimes
 * @version 1.0.3
 */
@Getter
public enum MarketPlatformEnum {
    PC("pc"),
    PS4("ps4"),
    XBOX("xbox"),
    SWITCH("switch"),
    MOBILE("mobile"),
    ;
    private final String platform;

    MarketPlatformEnum(String platform) {
        this.platform = platform;
    }
}

