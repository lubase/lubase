package com.lubase.wfengine.service;

import com.lubase.wfengine.auto.entity.WfCallbackEntity;
import com.lubase.wfengine.auto.entity.WfFInsEntity;

/**
 * 业务数据更新服务
 */
public interface BisDataUpdateService {
    /**
     * 流程开始事件
     *
     * @param fIns 流程实例
     */
    WfCallbackEntity sendStartEvent(WfFInsEntity fIns);

    /**
     * 流程正常结束事件
     *
     * @param fIns 流程实例
     */
    void sendEndEvent(WfFInsEntity fIns);

    /**
     * 流程拒绝事件
     *
     * @param fIns 流程实例
     */
    void sendRejectEvent(WfFInsEntity fIns,String reject_update_content);

    /**
     * 流程废弃事件
     *
     * @param fIns 流程实例
     */
    void sendGiveUpEvent(WfFInsEntity fIns,String give_up_update_content);

    /**
     * 流程命令事件
     *
     * @param fIns          流程实例
     * @param updateContent 更新内容
     */
    void sendCommandEvent(WfFInsEntity fIns, String updateContent, String updateMemo);

    /**
     * 流程流转事件
     *
     * @param fIns          流程实例
     * @param updateContent 更新内容
     */
    void sendLinkEvent(WfFInsEntity fIns, String updateContent, String updateMemo);
}
