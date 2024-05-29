package com.lubase.core.service;

import com.lubase.model.DbEntity;
import com.lubase.core.util.ClientMacro;

import java.util.List;

public interface RenderTreeService {
    /**
     * 根据页面code获取页面tree的数据
     *
     * @param pageId      页面主键
     * @param clientMacro 客户端宏变量
     * @return
     */
    List<DbEntity> getTreeDataByPageId(String pageId, ClientMacro clientMacro);
}
