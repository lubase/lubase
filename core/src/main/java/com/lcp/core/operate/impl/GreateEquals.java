package com.lcp.core.operate.impl;

import com.lcp.core.operate.BaseOperateMode;
import com.lcp.core.operate.EOperateMode;

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
