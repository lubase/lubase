package com.lubase.orm.service.update;

import com.alibaba.druid.util.StringUtils;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.lubase.orm.util.TypeConverterUtils;

import java.time.LocalDateTime;
import java.util.Map;

/*
 * @Description: 日期加减函数，支持 DAY, MONTH, YEAR
 */
public class GetDateAddValue extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        String departed = FunctionUtils.getStringValue(arg1, env);
        int number = FunctionUtils.getNumberValue(arg2, env).intValue();
        String date = FunctionUtils.getStringValue(arg3, env);
        if (StringUtils.isEmpty(date)) {
            return new AviatorString("");
        }
        LocalDateTime dtvalue = TypeConverterUtils.object2LocalDateTime(date);
        if (dtvalue == null) {
            return new AviatorString("");
        }
        if (departed.equalsIgnoreCase("DAY")) {
            dtvalue = dtvalue.plusDays(number);
        } else if (departed.equalsIgnoreCase("MONTH")) {
            dtvalue = dtvalue.plusMonths(number);
        } else if (departed.equalsIgnoreCase("YEAR")) {
            dtvalue = dtvalue.plusYears(number);
        } else if (departed.equalsIgnoreCase("WEEK")) {
            dtvalue = dtvalue.plusWeeks(number);
        } else if (departed.equalsIgnoreCase("HOUR")) {
            dtvalue = dtvalue.plusHours(number);
        } else if (departed.equalsIgnoreCase("MINUTE")) {
            dtvalue = dtvalue.plusMinutes(number);
        } else if (departed.equalsIgnoreCase("SECOND")) {
            dtvalue = dtvalue.plusSeconds(number);
        } else {
            return new AviatorString("");
        }
        return new AviatorString(TypeConverterUtils.object2LocalDateTime2String(dtvalue, "yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String getName() {
        return "a_w_date_add";
    }
}
