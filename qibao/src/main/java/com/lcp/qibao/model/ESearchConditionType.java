package com.lcp.qibao.model;

public enum ESearchConditionType {
    LikeAll(1),
    Equal(2),
    LikeLeft(3),
    LikeRight(4),
    Range(5),
    AllValue(6),
    AnyValue(7);
    private Integer type;

    ESearchConditionType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public static ESearchConditionType fromIndexValue(Integer value) {
        if (value.equals(1)) {
            return ESearchConditionType.LikeAll;
        } else if (value.equals(2)) {
            return ESearchConditionType.Equal;
        } else if (value.equals(3)) {
            return ESearchConditionType.LikeLeft;
        } else if (value.equals(4)) {
            return ESearchConditionType.LikeRight;
        } else if (value.equals(5)) {
            return ESearchConditionType.Range;
        } else if (value.equals(6)) {
            return ESearchConditionType.AllValue;
        } else if (value.equals(7)) {
            return ESearchConditionType.AnyValue;
        } else {
            return null;
        }
    }
}
