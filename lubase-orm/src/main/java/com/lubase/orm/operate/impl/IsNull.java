package com.lubase.orm.operate.impl;

import com.lubase.orm.operate.BaseOperateMode;
import com.lubase.orm.operate.EOperateMode;

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
