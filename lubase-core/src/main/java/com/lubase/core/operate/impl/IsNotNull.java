package com.lubase.core.operate.impl;

import com.lubase.core.operate.BaseOperateMode;
import com.lubase.core.operate.EOperateMode;

public class IsNotNull extends BaseOperateMode {
    @Override
    protected EOperateMode getOperateMode() {
        return EOperateMode.IsNotNull;
    }

    @Override
    protected String getSqlExpression() {
        return "%s is not null";
    }
}
