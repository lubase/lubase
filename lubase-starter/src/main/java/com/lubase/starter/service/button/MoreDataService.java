package com.lubase.starter.service.button;

import com.lubase.starter.auto.entity.SsButtonEntity;

import java.util.HashMap;
import java.util.List;

/**
 * 特定类型的按钮对应的方法
 *
 * @author A
 */
public interface MoreDataService {
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
     * 执行自定义方法,需要传递多个对象作为参数
     *
     * @param listMapParam
     * @return
     */
    Object exe(SsButtonEntity button, List<HashMap<String, String>> listMapParam) throws Exception;
}
