package com.lubase.core.model;

/**
 * @author A
 */

public enum EPageType {
    /**
     * 一级页面
     */
    OneLevelPage(1),
    /**
     * 二级页面
     */
    TwoLevelPage(2),
    /**
     * 公共页面
     */
    CommonPage(3),
    /**
     * 页面组
     */
    PageGroup(4);

    private int type;

    EPageType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
