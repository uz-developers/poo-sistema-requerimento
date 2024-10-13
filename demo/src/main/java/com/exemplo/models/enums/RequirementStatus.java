package com.exemplo.models.enums;

public enum RequirementStatus {
    EM_ANDAMENTO("Em andamento"),
    NAO_INICIADO("Nao iniciado"),
    CONCLUIDO("Concluido");

    private final String value;

    RequirementStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RequirementStatus fromValue(String value) {
        for (RequirementStatus status : RequirementStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Valor desconhecido: " + value);
    }
}
