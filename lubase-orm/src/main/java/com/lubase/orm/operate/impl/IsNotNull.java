package com.lubase.orm.operate.impl;

import com.lubase.orm.operate.BaseOperateMode;
import com.lubase.orm.operate.EOperateMode;

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
