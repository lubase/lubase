package com.lubase.orm.operate.impl;

import com.lubase.orm.operate.BaseOperateMode;
import com.lubase.orm.operate.EOperateMode;

public class Less extends BaseOperateMode {
    @Override
    protected EOperateMode getOperateMode() {
        return EOperateMode.Less;
    }

    @Override
    protected String getSqlExpression() {
        return "%s < %s";
    }
}
