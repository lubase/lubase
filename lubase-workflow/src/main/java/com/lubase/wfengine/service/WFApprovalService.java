package com.lubase.wfengine.service;

import com.lubase.core.model.DbCollection;
import com.lubase.wfengine.model.CustomPageSetting;
import com.lubase.wfengine.model.WFApprovalFormVO;

/**
 * 待办处理服务
 */
public interface WFApprovalService {
    /**
     * 获取待处理实例自定义页面配置信息
     *
     * @param oInsId
     * @param userId
     * @return
     */
    CustomPageSetting getRefPageSetting(String oInsId, String userId);

    /**
     * 获取待处理任务的详情
     *
     * @param oInsId
     * @param userId
     * @return
     */
    WFApprovalFormVO getApprovalForm(String oInsId, String userId);

    /**
     * 根据流程实例获取审批表单，用于审批监控
     *
     * @param fInsId
     * @return
     */
    WFApprovalFormVO getApprovalHistoryForm(String fInsId,String oInsId);

    /**
     * 获取流程的审批记录
     * @param fInsId
     * @return
     */
    DbCollection getApprovalList(String fInsId);
}
