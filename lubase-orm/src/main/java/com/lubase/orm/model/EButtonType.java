package com.lubase.orm.model;

/**
 * 按钮类型。只逻辑了必要的在后端编码中用到的类型
 */
public enum EButtonType {
    /**
     * 弹窗新增
     */
    Add(20),
    /**
     * 弹窗编辑
     */
    Edit(21),
        /**
     * 左树编辑
     */
    TreeEdit(22),
    /**
     * 查看详情
     */
    ViewInfo(25),
    /**
     * 删除单条
     */
    DeleteOne(26),
    /**
     * 删除多条
     */
    DeleteMore(27);

    private int index;

    EButtonType(int index) {
        this.index = index;
    }

    public Integer getIndex() {
        return this.index;
    }

    public String getStringValue() {
        return String.valueOf(this.index);
    }
}
