package io.github.kingprimes.model.enums;

import lombok.Getter;

@Getter
public enum StateIconEnum {
    COLD("\ue100"),
    SUN("\ue101"),
    NIGHT("\ue102");
    final String ICON;

    StateIconEnum(String icon) {
        ICON = icon;
    }

}
