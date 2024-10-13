package com.exemplo.models.enums;

public enum RegimeEnum {
    LABORAL("laboral"),
    POS_LABORAL("pos-laboral");

    private final String value;

    RegimeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // de String para enum
    public static RegimeEnum fromValue(String value) {
        for (RegimeEnum regime : RegimeEnum.values()) {
            if (regime.getValue().equals(value)) {
                return regime;
            }
        }
        throw new IllegalArgumentException("Valor desconhecido: " + value);
    }
}
