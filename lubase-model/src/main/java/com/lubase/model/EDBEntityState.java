package com.lubase.model;

/**
 * 数据实体的状态
 *
 * @author A
 */

public enum EDBEntityState {
    /**
     * 没有修改
     */
    UnChanged(0),
    /**
     * 新增
     */
    Added(1),
    /**
     * 修改
     */
    Modified(2),
    /**
     * 删除
     */
    Deleted(4);

    private int index;

    EDBEntityState(int index) {
        this.index = index;
    }

    public Integer getIndex() {
        return this.index;
    }

    public static EDBEntityState fromIndex(int index) {
        if (index == 1) {
            return EDBEntityState.Added;
        } else if (index == 2) {
            return EDBEntityState.Modified;
        } else if (index == 4) {
            return EDBEntityState.Deleted;
        } else {
            return EDBEntityState.UnChanged;
        }
    }
}
