package com.lubase.core.extend;


import com.lubase.orm.exception.ParameterNotFoundException;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 实现inovke方法需要继承的接口
 *
 * @author A
 */
public interface IInvokeMethod extends IBaseInvoke {
    /**
     * 执行自定义方法，需要传递单个对象作为参数
     *
     * @param mapParam
     * @return
     */
    Object exe(HashMap<String, String> mapParam) throws Exception;

    /**
     * 执行自定义方法,需要传递多个对象作为参数
     *
     * @param listMapParam
     * @return
     */
    default Object exe(List<HashMap<String, String>> listMapParam) throws Exception {
        return null;
    }

    /**
     * 检查参数是否存在，如果不存在会抛出ParameterNotFoundException
     *
     * @param key
     * @param mapParam
     */
    @SneakyThrows
    default String checkAndGetParam(String key, HashMap<String, String> mapParam) {
        if (mapParam.containsKey(key) && !StringUtils.isEmpty(mapParam.get(key))) {
            return mapParam.get(key);
        } else {
            throw new ParameterNotFoundException(key);
        }
    }
}
