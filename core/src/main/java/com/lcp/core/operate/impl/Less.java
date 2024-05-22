package com.lcp.core.operate.impl;

import com.lcp.core.operate.BaseOperateMode;
import com.lcp.core.operate.EOperateMode;

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
