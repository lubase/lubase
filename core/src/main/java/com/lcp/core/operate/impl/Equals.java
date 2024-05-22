package com.lcp.core.operate.impl;


import com.lcp.core.operate.BaseOperateMode;
import com.lcp.core.operate.EOperateMode;

/**
 * @author A
 */
public class Equals extends BaseOperateMode {

    @Override
    protected EOperateMode getOperateMode() {
        return EOperateMode.Equals;
    }

    @Override
    protected String getSqlExpression() {
        return "%s = %s";
    }
}
