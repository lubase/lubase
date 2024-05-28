package com.lubase.starter.service.userright.model;

public enum ERoleType {

    /**
     * 超级管理员
     */
    SupperAdministrator(1),
    /**
     * 应用管理员
     */
    AppAdministrator(2),
    /**
     * 应用内普通角色
     */
    Other(3);
    private Integer index;

    ERoleType(Integer index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return index.toString();
    }
}
