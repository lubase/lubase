package com.lubase.orm.operate.impl;

import com.lubase.orm.operate.BaseOperateMode;
import com.lubase.orm.operate.EOperateMode;

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
