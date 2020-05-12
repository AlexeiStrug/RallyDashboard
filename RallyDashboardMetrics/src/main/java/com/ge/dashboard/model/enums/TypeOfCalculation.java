package com.ge.dashboard.model.enums;

public enum TypeOfCalculation {
    MIN("min"),
    AVG("avg"),
    MAX("max"),
    BELOW("below");

    private String name;

    TypeOfCalculation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
