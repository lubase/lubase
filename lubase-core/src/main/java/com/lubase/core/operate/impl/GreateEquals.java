package com.lubase.core.operate.impl;

import com.lubase.core.operate.BaseOperateMode;
import com.lubase.core.operate.EOperateMode;

public class GreateEquals extends BaseOperateMode {
    @Override
    protected EOperateMode getOperateMode() {
        return EOperateMode.GreateEquals;
    }

    @Override
    protected String getSqlExpression() {
        return "%s >= %s";
    }
}
