package com.lubase.orm.service.update;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

/**
 * 计算列：计算表达式中 a_w 函数，用于获取固定位数的流水号，调用方式 a_w(currentIndex,length)
 */
public class GetNextIndex extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        int currentIndex = FunctionUtils.getNumberValue(arg1, env).intValue();
        int noLength = FunctionUtils.getNumberValue(arg2, env).intValue();
        String tmpValue = "0000000000";
        while (tmpValue.length() < noLength) {
            tmpValue = tmpValue + "0000000000";
        }
        tmpValue = tmpValue + (currentIndex + 1);
        return new AviatorString(tmpValue.substring(tmpValue.length() - noLength));
    }

    @Override
    public String getName() {
        return "a_w";
    }
}
