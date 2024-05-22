package com.lcp.qibao.service;

import com.lcp.qibao.model.DisplayListVO;

/**
 * 客户端个性化配置服务
 */
public interface PersonalizationDataService {
    /**
     * 获取用户在某一个页面的配置列表
     *
     * @param pageId
     * @param accountId
     * @return
     */
    DisplayListVO getDisplaySetting(Long pageId, Long accountId);

    /**
     * 设置用户在某一个页面的配置列表
     *
     * @param pageId
     * @param accountId
     * @param columnIds
     * @param lockColumnCount 锁定列数量
     * @return
     */
    DisplayListVO saveDisplayColumn(Long pageId, Long accountId, String columnIds, int lockColumnCount);

    /**
     * 自定义列宽设置。
     *
     * @param pageId
     * @param accountId
     * @param columnWidthSetting，此字段服务器端不解析，所以原封不动的存储和返回即可
     * @return
     */
    DisplayListVO saveColumnWidthSetting(Long pageId, Long accountId, String columnWidthSetting);
}
