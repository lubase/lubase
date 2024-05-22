package com.lcp.core.operate.impl;

import com.lcp.core.operate.BaseOperateMode;
import com.lcp.core.operate.EOperateMode;

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
