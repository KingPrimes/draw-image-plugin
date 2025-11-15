package io.github.kingprimes.model.enums;

import lombok.Getter;

/**
 * 交易状态
 *
 * @author KingPrimes
 * @version 1.0.3
 */
@Getter
public enum TransactionEnum {
    SELL("sell"),
    BUY("buy"),
    ALL("all"),
    NONE("none"),
    ;

    private final String name;

    TransactionEnum(String name) {
        this.name = name;
    }
}
