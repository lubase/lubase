package com.lubase.core.operate.impl;


import com.lubase.core.operate.BaseOperateMode;
import com.lubase.core.operate.EOperateMode;

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
