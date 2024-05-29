package com.lcp.wfengine;

import com.alibaba.fastjson.JSON;
import com.lubase.wfengine.auto.entity.WfLinkEntity;
import com.lubase.wfengine.auto.entity.WfTaskEntity;
import com.lubase.wfengine.dao.WfLinkDao;
import com.lubase.wfengine.dao.WfTaskDao;
import com.lubase.wfengine.model.NextTaskEntity;
import com.lubase.wfengine.model.WFCmdRunModel;
import com.lubase.wfengine.node.BaseNodeService;
import com.lubase.wfengine.service.WorkTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class WorkTaskServiceTest {

    @Autowired
    WorkTaskService taskService;

    @Autowired
    WfTaskDao taskDao;
    @Autowired
    WfLinkDao linkDao;

    @Test
    void taskGetNextTask() {
        String flowId = "769440101133455360";
        WfTaskEntity taskEntity = taskDao.getStartTask(flowId);
        List<NextTaskEntity> list = taskService.getNextTasks2(taskEntity, null, null);
        System.out.println(JSON.toJSONString(list));
        assert list.size() == 1;
    }

    @Test
    void taskGetNextTask2() {
        WfTaskEntity taskEntity = taskDao.getTaskEntityById("769461010686808064");
        WFCmdRunModel runModel = new WFCmdRunModel();
        runModel.setCmdId("769465475884126208");
        List<NextTaskEntity> nextTasks = taskService.getNextTasks2(taskEntity, null, runModel);
        assert nextTasks.size() == 1;
    }

    @Test
    void taskGetNextTask3() {
        String taskId = "769461010686808064"; //科长审核节点
        WFCmdRunModel runModel = new WFCmdRunModel();
        runModel.setCmdId("769465475884126208");//审核
        WfTaskEntity taskEntity = taskDao.getTaskEntityById(taskId);

        List<WfLinkEntity> links = linkDao.getNextLinkForTask(taskEntity.getFlow_id(), taskEntity.getId().toString(), runModel.getCmdId());
        System.out.println(JSON.toJSONString(links));
        assert links.size() == 1;

        List<NextTaskEntity> list = taskService.getNextTasks2(taskEntity, null, runModel);
        System.out.println(JSON.toJSONString(list));
        assert list.size() == 1;
    }

    @Test
    void testGetTaskNodeInfo() {
        String flowId = "769440101133455360";
        WfTaskEntity taskEntity = taskDao.getStartTask(flowId);
        BaseNodeService nodeService = taskService.getTaskNodeInfo(taskEntity);
        assert nodeService != null;
    }
}
