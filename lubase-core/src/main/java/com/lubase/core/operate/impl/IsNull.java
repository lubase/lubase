package com.lubase.core.operate.impl;

import com.lubase.core.operate.BaseOperateMode;
import com.lubase.core.operate.EOperateMode;

public class IsNull extends BaseOperateMode {
    @Override
    protected EOperateMode getOperateMode() {
        return EOperateMode.IsNull;
    }

    @Override
    protected String getSqlExpression() {
        return "%s is null";
    }
}
