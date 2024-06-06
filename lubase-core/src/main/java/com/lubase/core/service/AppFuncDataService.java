package com.lubase.core.service;

import com.lubase.core.model.ButtonRefFormInfo;
import com.lubase.model.DbEntity;
import com.lubase.core.entity.SsButtonEntity;
import com.lubase.core.entity.SsPageEntity;

import java.util.List;

public interface AppFuncDataService {
    /**
     * 获取页面基本信息
     *
     * @param id
     * @return
     */
    SsPageEntity getPageById(String id);

    /**
     * 获取页面关联的表
     *
     * @param pageEntity
     * @return
     */
    String getPageRefTable(SsPageEntity pageEntity);

    /**
     * 获取页面可编辑列
     *
     * @param pageEntity
     * @return
     */
    String getPageCanEditColumn(SsPageEntity pageEntity);

    /**
     * 获取页面按钮信息
     *
     * @param pageId
     * @return
     */
    List<DbEntity> getButtonListByPageId(Long pageId);

    /**
     * 根据funcCode获取关联的表单id。可能是页面按钮也可能是表单按钮
     *
     * @param funcCode
     * @return
     */
    String getFormIdByFuncCode(String funcCode);

    /**
     * @param funcCode
     * @return
     */
    ButtonRefFormInfo getRefFormInfoByFuncCode(String funcCode);

    /**
     * 检查页面是否配置了对应的表单
     *
     * @param pageId
     * @param formId
     * @return
     */
    Boolean checkPageContainsForm(String pageId, String formId);

    /**
     * 根据按钮id获取按钮信息
     *
     * @param btnId
     * @return
     */
    SsButtonEntity getFuncInfoByFuncCode(String btnId);
}
