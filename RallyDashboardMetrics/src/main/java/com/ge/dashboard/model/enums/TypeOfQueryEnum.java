package com.ge.dashboard.model.enums;

public enum TypeOfQueryEnum {
    ITERATION("Iteration"),
    HIERARCHICAL("HierarchicalRequirement"),
    RELEASE("Release"),
    PROJECT("Project");

    private String name;

    TypeOfQueryEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
