package com.lcp.core.operate.impl;

import com.lcp.core.operate.BaseOperateMode;
import com.lcp.core.operate.EOperateMode;

public class LikeRight extends BaseOperateMode {
    @Override
    protected EOperateMode getOperateMode() {
        return EOperateMode.LikeRight;
    }

    @Override
    protected String getSqlExpression() {
        return "%s like %s";
    }

    @Override
    public Object getLogicValue(Object filterValue) {
        return "%" + filterValue.toString();
    }
}
