package com.lcp.core.operate.impl;

import com.lcp.core.operate.BaseOperateMode;
import com.lcp.core.operate.EOperateMode;

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
