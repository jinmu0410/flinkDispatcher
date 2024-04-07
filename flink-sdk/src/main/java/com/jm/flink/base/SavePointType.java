
package com.jm.flink.base;

import java.util.Arrays;

public enum SavePointType {
    TRIGGER("trigger"),
    DISPOSE("dispose"),
    STOP("stop"),
    CANCEL("cancel");

    private String value;

    SavePointType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SavePointType get(String value) {
        return Arrays.stream(SavePointType.values())
                .filter(type -> type.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElse(SavePointType.TRIGGER);
    }
}
