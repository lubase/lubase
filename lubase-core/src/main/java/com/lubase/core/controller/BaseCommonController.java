package com.lubase.core.controller;

import com.lubase.orm.exception.ParameterNotFoundException;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 提供基础的公共方法
 *
 * @author ss
 */
public class BaseCommonController {

    /**
     * 获取参数
     *
     * @param key        参数key
     * @param map        参数map
     * @param checkEmpty 是否检查参数是否为空
     * @return 参数值
     */
    @SneakyThrows
    public String getParam(@NonNull String key, Map<String, String> map, Boolean checkEmpty) {
        String val = "";
        if (map.containsKey(key)) {
            val = map.get(key);
        }
        if (checkEmpty && StringUtils.isEmpty(val)) {
            throw new ParameterNotFoundException(key);
        }
        return val;
    }
}
