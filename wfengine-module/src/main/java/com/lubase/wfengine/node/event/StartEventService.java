package com.lubase.wfengine.node.event;

import com.lubase.orm.model.DbCollection;
import com.lubase.wfengine.auto.entity.WfFInsEntity;
import com.lubase.wfengine.auto.entity.WfTEventEntity;
import com.lubase.wfengine.auto.entity.WfTInsEntity;
import com.lubase.wfengine.auto.entity.WfTaskEntity;
import com.lubase.wfengine.dao.WfFInsDao;
import com.lubase.wfengine.model.ETaskType;
import com.lubase.wfengine.model.OperatorUserModel;
import com.lubase.wfengine.node.BaseNodeService;
import com.lubase.wfengine.service.WorkOperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StartEventService extends BaseNodeService {
    @Autowired
    WorkOperatorService operatorService;

    @Autowired
    WfFInsDao fInsDao;
    @Autowired
    WorkOperatorService workOperatorService;

    @Override
    public ETaskType getTaskType() {
        return ETaskType.StartEvent;
    }

    @Override
    public Boolean runTask(WfFInsEntity fIns, WfTaskEntity taskEntity, WfTInsEntity tIns, WfTEventEntity event, DbCollection bisData) {
        //1、获取申请人信息
        List<OperatorUserModel> userIdList = new ArrayList<>();
        userIdList.add(workOperatorService.getApplyUserModel(fIns));
        //2、创建处理人实例
        operatorService.createOIns(userIdList, fIns, tIns);
        //3、更新流程实例信息
        fInsDao.updateFInsProcessUser(fIns, userIdList);
        return true;
    }
}
