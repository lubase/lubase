package com.lubase.starter.service.userright.model;

public enum EAssignType {
    User("1"),
    Org("2");

    private String assignType;

    EAssignType(String type) {
        this.assignType = type;
    }

    public String getAssignType() {
        return this.assignType;
    }
}
