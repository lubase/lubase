package com.lubase.core.operate.impl;

import com.lubase.core.operate.BaseOperateMode;
import com.lubase.core.operate.EOperateMode;

public class LikeAll extends BaseOperateMode {
    @Override
    protected EOperateMode getOperateMode() {
        return EOperateMode.LikeAll;
    }

    @Override
    protected String getSqlExpression() {
        return "%s like %s";
    }

    @Override
    public Object getLogicValue(Object filterValue) {
        return "%" + filterValue.toString() + "%";
    }
}
