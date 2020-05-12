package com.ge.dashboard.model.enums;

public enum TypeOfRequestEnum {
    RELEASE("release"),
    ITERATION("iteration"),
    PROJECT("project"),
    SP_PER_ITERATION_MAP("sp_per_iteration_map"),
    SP_PER_ITERATION_LIST("sp_per_iteration_list"),
    SP_BACKLOG("sp_backlog");

    private String name;

    TypeOfRequestEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
