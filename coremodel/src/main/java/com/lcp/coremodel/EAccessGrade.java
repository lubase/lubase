package com.lcp.coremodel;

/**
 * 字段的访问级别
 *
 * @author A
 */

public enum EAccessGrade {
    /**
     * 不可见
     */
    Invisible(0),
    /**
     * 只读
     */
    Read(2),
    /**
     * 新增可写，编辑不可写
     */
    NewToWrite(3),
    /**
     * 编辑权限
     */
    Write(4);
    private Integer index;

    EAccessGrade(Integer index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public static EAccessGrade fromIndex(int index) {
        if (index == 0) {
            return Invisible;
        } else if (index == 2) {
            return Read;
        } else if (index == 3) {
            return NewToWrite;
        } else if (index == 4) {
            return Write;
        }
        return null;
    }

}
