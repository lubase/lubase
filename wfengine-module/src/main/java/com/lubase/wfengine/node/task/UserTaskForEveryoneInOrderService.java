package com.lubase.wfengine.node.task;

import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.wfengine.auto.entity.WfFInsEntity;
import com.lubase.wfengine.auto.entity.WfTEventEntity;
import com.lubase.wfengine.auto.entity.WfTInsEntity;
import com.lubase.wfengine.auto.entity.WfTaskEntity;
import com.lubase.wfengine.dao.WfFInsDao;
import com.lubase.wfengine.dao.WfOperatorDao;
import com.lubase.wfengine.model.ETaskType;
import com.lubase.wfengine.model.OperatorUserModel;
import com.lubase.wfengine.node.BaseNodeService;
import com.lubase.wfengine.service.WorkOperatorService;
import com.lubase.wfengine.service.WorkTaskService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserTaskForEveryoneInOrderService extends BaseNodeService {
    @Autowired
    WorkTaskService taskService;

    @Autowired
    WorkOperatorService operatorService;

    @Autowired
    WfFInsDao fInsDao;

    @Autowired
    WfOperatorDao operatorDao;

    @Override
    public ETaskType getTaskType() {
        return ETaskType.UserTaskForEveryoneInOrder;
    }

    @SneakyThrows
    @Override
    public Boolean runTask(WfFInsEntity fIns, WfTaskEntity taskEntity, WfTInsEntity tIns, WfTEventEntity event, DbCollection bisData) {
        //1、获取处理人
        List<OperatorUserModel> userIdList = taskService.getNodeProcessUserList(fIns, taskEntity, bisData);
        if (userIdList.size() == 0) {
            throw new WarnCommonException("未找到处理人");
        }
        //2、第一个人创建处理人实例
        List<OperatorUserModel> firstUserList = new ArrayList<>();
        firstUserList.add(userIdList.get(0));
        operatorService.createOIns(firstUserList, fIns, tIns);
        //3、为其他人生成未启用任务
        if (userIdList.size() > 1) {
            userIdList.remove(0);
            operatorService.createUnEnabledOIns(userIdList, fIns, tIns);
        }
        //4、更新流程实例信息
        fInsDao.updateFInsProcessUser(fIns, userIdList);
        return true;
    }
}
