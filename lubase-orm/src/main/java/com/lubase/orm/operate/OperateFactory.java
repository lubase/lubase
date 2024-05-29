package com.lubase.orm.operate;

import com.lubase.orm.operate.impl.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author A
 */
@Component
public class OperateFactory {
    static Map<EOperateMode, BaseOperateMode> operateModeMap = new HashMap<>();

    public OperateFactory() {
        operateModeMap.put(EOperateMode.Equals, new Equals());
        operateModeMap.put(EOperateMode.LessEquals, new LessEquals());
        operateModeMap.put(EOperateMode.Less, new Less());
        operateModeMap.put(EOperateMode.Great, new Great());
        operateModeMap.put(EOperateMode.GreateEquals, new GreateEquals());
        operateModeMap.put(EOperateMode.NotEquals, new NotEquals());
        operateModeMap.put(EOperateMode.IsNotNull, new IsNotNull());
        operateModeMap.put(EOperateMode.IsNull, new IsNull());
        operateModeMap.put(EOperateMode.In, new In());
        operateModeMap.put(EOperateMode.NotIn, new NotIn());
        operateModeMap.put(EOperateMode.LikeAll, new LikeAll());
        operateModeMap.put(EOperateMode.LikeLeft, new LikeLeft());
        operateModeMap.put(EOperateMode.LikeRight, new LikeRight());
    }

    @SneakyThrows
    public BaseOperateMode getParseToolByOperateMode(EOperateMode operateMode) {
        //TODO:默认为等于
        if (operateMode == null) {
            operateMode = EOperateMode.Equals;
        }
        if (operateModeMap.containsKey(operateMode)) {
            return operateModeMap.get(operateMode);
        } else {
            if (operateMode == null) {
                throw new Exception("operateMode 不能为空");
            }
            throw new Exception(String.format("%s 没有实现", operateMode.toString()));
        }
    }
}
