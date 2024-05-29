package com.lubase.wfengine.service;

import com.lubase.wfengine.auto.entity.WfTEventEntity;

import java.util.List;

public interface WfTEventService {
    /**
     * 事件处理中
     *
     * @param id
     * @return
     */
    Integer eventProcessing(Long id);

    /**
     * 事件处理完成
     *
     * @param id
     * @return
     */
    Integer eventProcess(Long id);

    /**
     * 获取指定数量的未处理事件
     *
     * @param count
     * @return
     */
    List<WfTEventEntity> getUnProcessEvent(Integer count);
}
