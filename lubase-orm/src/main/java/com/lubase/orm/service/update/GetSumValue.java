package com.lubase.orm.service.update;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
 * @Description:
 */
public class GetSumValue extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        String ctData = FunctionUtils.getStringValue(arg1, env);
        //是否去掉最大值
        Boolean highValue = FunctionUtils.getStringValue(arg2, env).equals("h.1");
        //是否去掉最小值
        Boolean lowValue = FunctionUtils.getStringValue(arg3, env).equals("l.1");
        Double sumValue = 0.0;
        List<Double> list = new ArrayList<>();
        for (String ct : ctData.split(",")) {
            if (StringUtils.isEmpty(ct) || ct.equals("0")) {
                list.add(0.0);
            }
            list.add(Double.parseDouble(ct));
        }
        Collections.sort(list);
        if (list.size() > 1 && highValue) {
            list.remove(list.size() - 1);
        }
        if (list.size() > 1 && lowValue) {
            list.remove(0);
        }
        for (Double d : list) {
            sumValue += d;
        }
        return new AviatorDouble(sumValue);
    }

    @Override
    public String getName() {
        return "a_sum";
    }
}
