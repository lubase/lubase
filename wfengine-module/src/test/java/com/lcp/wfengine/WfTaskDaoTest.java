package com.lcp.wfengine;

import com.lubase.wfengine.auto.entity.WfTaskEntity;
import com.lubase.wfengine.dao.WfTaskDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WfTaskDaoTest {

    @Autowired
    WfTaskDao taskDao;

    @Test
    void testGetStartNode() {
        String flowId = "769440101133455360";
        WfTaskEntity taskEntity = taskDao.getStartTask(flowId);
    }

    @Test
    void testUpdateTInsStatus() {
        Long id = 771074635562225664L;
        taskDao.updateTInsStatus(id);
    }

    @Test
    void testGetInfo() {
        String id = "918980703566172171";
        WfTaskEntity taskEntity = taskDao.getTaskEntityById(id);
        assert taskEntity.getRebuild_operator();
    }

}
