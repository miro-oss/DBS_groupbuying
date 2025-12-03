package com.example.groupbuying.domain.groupbuy.enums;

public enum FormStatus {
    OPEN("모집중"),
    CLOSED("마감");

    private final String description;

    FormStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}