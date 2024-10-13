package com.exemplo.models.enums;

public enum RequirementType {
    A("a"), // valores feticios
    B("b"),
    C("c");

    private final String value;

    RequirementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RequirementType fromValue(String value) {
        for (RequirementType type : RequirementType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Valor desconhecido: " + value);
    }
}