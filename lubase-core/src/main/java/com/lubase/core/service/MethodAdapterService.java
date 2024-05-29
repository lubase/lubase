package com.lubase.core.service;

import java.util.HashMap;
import java.util.List;

/**
 * @author A
 */

/**
 * 不同类型的按钮和方法适配服务
 */
public interface MethodAdapterService {
    /**
     * 免登录访问方法
     *
     * @param methodId
     * @param mapParam
     * @return
     */
    Object exeMethodByIdNoRight(Long methodId, HashMap<String, String> mapParam);

    /**
     * 通过方法ID执行方法
     *
     * @param pageId
     * @param methodId
     * @param mapParam
     * @return
     */
    Object exeMethodById(String pageId, Long methodId, HashMap<String, String> mapParam);

    /**
     * 通过方法ID执行方法
     *
     * @param appId
     * @param pageId
     * @param dataSourceId
     * @param queryParamList
     * @return
     */
    Object exeDataSourceById(Long appId, Long pageId, Long dataSourceId, String... queryParamList);

    /**
     * 通过方法ID执行方法
     *
     * @param pageId
     * @param methodId
     * @param listMapParam
     * @return
     */
    Object exeMethodById(String pageId, Long methodId, List<HashMap<String, String>> listMapParam);


    /**
     * 通过方法ID执行方法
     *
     * @param pageId
     * @param btnCode  按钮code
     * @param mapParam
     * @return
     */
    Object exeMethodByFuncCode(Long pageId, String btnCode, HashMap<String, String> mapParam);

    /**
     * 通过方法ID执行方法
     *
     * @param pageId
     * @param btnCode      按钮code
     * @param listMapParam
     * @return
     */
    Object exeMethodByFuncCode(Long pageId, String btnCode, List<HashMap<String, String>> listMapParam);
}
