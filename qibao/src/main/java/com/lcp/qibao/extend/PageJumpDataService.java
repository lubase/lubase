package com.lcp.qibao.extend;

import com.lcp.core.exception.ParameterNotFoundException;
import com.lcp.coremodel.DbEntity;
import com.lcp.qibao.auto.entity.SsButtonEntity;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.util.HashMap;

public interface PageJumpDataService {

    /**
     * 跳转的页面地址
     */
    final String NAVCODE = "__navCode";
    /**
     * 原始数据id
     */
    final String ORIGINALID="__originalDataId";

    /**
     * 方法唯一id
     *
     * @return
     */
    String getId();

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
    DbEntity convertClientData(SsButtonEntity button, HashMap<String, String> mapParam) throws Exception;

    @SneakyThrows
    default String checkAndGetParam(String key, HashMap<String, String> mapParam) {
        if (mapParam.containsKey(key) && !StringUtils.isEmpty(mapParam.get(key))) {
            return mapParam.get(key);
        } else {
            throw new ParameterNotFoundException(key);
        }
    }
}
