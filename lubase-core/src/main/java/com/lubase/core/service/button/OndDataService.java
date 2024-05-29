package com.lubase.core.service.button;

import com.lubase.orm.exception.ParameterNotFoundException;
import com.lubase.core.entity.SsButtonEntity;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * 特定类型的按钮对应的方法
 *
 * @author A
 */
public interface OndDataService {
    String idHandle = "id";
    String logicDeleteHandle = "delete_tag";

    /**
     * 获取按钮的类型
     *
     * @return
     */
    String getButtonType();

    /**
     * 设置方法描述
     *
     * @return
     */
    String getDescription();

    /**
     * 执行自定义方法，需要传递单个对象作为参数
     *
     * @param mapParam
     * @return
     */
    Object exe(SsButtonEntity button, HashMap<String, String> mapParam) throws Exception;

    @SneakyThrows
    default String checkAndGetParam(String key, HashMap<String, String> mapParam) {
        if (mapParam.containsKey(key) && !StringUtils.isEmpty(mapParam.get(key))) {
            return mapParam.get(key);
        } else {
            throw new ParameterNotFoundException(key);
        }
    }
}
