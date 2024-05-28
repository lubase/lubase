package com.lubase.wfengine.node.event;

import com.lubase.core.model.DbCollection;
import com.lubase.wfengine.auto.entity.WfFInsEntity;
import com.lubase.wfengine.auto.entity.WfTEventEntity;
import com.lubase.wfengine.auto.entity.WfTInsEntity;
import com.lubase.wfengine.auto.entity.WfTaskEntity;
import com.lubase.wfengine.model.ETaskType;
import com.lubase.wfengine.model.OperatorUserModel;
import com.lubase.wfengine.node.BaseNodeService;
import com.lubase.wfengine.service.WorkOperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EndEventService extends BaseNodeService {
    @Autowired
    WorkOperatorService operatorService;

    @Override
    public ETaskType getTaskType() {
        return ETaskType.EndEvent;
    }

    @Override
    public Boolean runTask(WfFInsEntity fIns, WfTaskEntity taskEntity, WfTInsEntity tIns, WfTEventEntity event, DbCollection bisData) {
        //1、为了让审批记录可以提现结束节点，增加系统用户
        List<OperatorUserModel> userIdList = new ArrayList<>();
        OperatorUserModel userModel = new OperatorUserModel();
        userModel.setUserId("System");
        userModel.setUserName("System");
        userIdList.add(userModel);
        //2、创建处理人实例
        operatorService.createOInsForEndNode(userIdList, fIns, tIns);
        return true;
    }
}
