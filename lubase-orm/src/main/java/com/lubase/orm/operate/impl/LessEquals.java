package com.lubase.orm.operate.impl;

import com.lubase.orm.operate.BaseOperateMode;
import com.lubase.orm.operate.EOperateMode;

/**
 * <=
 * @author A
 */
public class LessEquals extends BaseOperateMode {
    @Override
    protected EOperateMode getOperateMode() {
        return EOperateMode.LessEquals;
    }

    @Override
    protected String getSqlExpression() {
        return "%s <= %s";
    }
}
