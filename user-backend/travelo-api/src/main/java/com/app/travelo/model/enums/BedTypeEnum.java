package com.app.travelo.model.enums;

public enum BedTypeEnum {
    KING("king"),
    QUEEN("queen"),
    DOUBLE("double"),
    SINGLE("single");

    public final String label;
    BedTypeEnum(String suite) {
        this.label = suite;
    }
}
