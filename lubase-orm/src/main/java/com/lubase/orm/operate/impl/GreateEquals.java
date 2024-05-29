package com.lubase.orm.operate.impl;

import com.lubase.orm.operate.BaseOperateMode;
import com.lubase.orm.operate.EOperateMode;

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
