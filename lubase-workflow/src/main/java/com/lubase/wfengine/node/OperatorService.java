package com.lubase.wfengine.node;

import com.lubase.core.model.DbCollection;
import com.lubase.wfengine.auto.entity.WfFInsEntity;
import com.lubase.wfengine.auto.entity.WfOperEntity;
import com.lubase.wfengine.auto.entity.WfTaskEntity;
import com.lubase.wfengine.model.EOperatorType;
import com.lubase.wfengine.model.OperatorUserModel;

import java.util.List;

/**
 * 任务处理者服务
 */
public interface OperatorService {
    /**
     * 获取处理者类型
     *
     * @return
     */
    EOperatorType getOperatorType();

    /**
     * 根据任务节点配置的处理者获取具体的处理人
     *
     * @param taskEntity
     * @param operEntity
     * @param fIns
     * @param bisData
     * @return
     */
    List<OperatorUserModel> getUserIdList(WfTaskEntity taskEntity, WfOperEntity operEntity, WfFInsEntity fIns, DbCollection bisData);
}
