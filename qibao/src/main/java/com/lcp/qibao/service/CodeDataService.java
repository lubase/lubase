package com.lcp.qibao.service;

import com.lcp.qibao.auto.entity.DmCodeEntity;
import com.lcp.qibao.model.CodeDataTypeVO;

import java.util.List;

/**
 * 代码表服务
 *
 * @author A
 */
public interface CodeDataService {
    /**
     * 页面初始时获取全部的代码表
     *
     * @return
     */
    List<CodeDataTypeVO> getCodeListForAppSetting(String appId);

    /**
     * 根据应用获取代码表
     *
     * @param appId
     * @return
     */
    List<CodeDataTypeVO> getCodeListByAppId(Long appId);

    /**
     * 根据typeid 获取代码表集合
     *
     * @param typeId
     * @return
     */
    List<DmCodeEntity> getCodeListByTypeId(Long typeId);
}
