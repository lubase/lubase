package com.lcp.wfengine;

import com.alibaba.fastjson.JSON;
import com.lubase.core.exception.WarnCommonException;
import com.lubase.core.extend.IColumnRemoteService;
import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
import com.lubase.model.DbEntity;
import com.lubase.wfengine.auto.entity.WfFInsEntity;
import com.lubase.wfengine.auto.entity.WfOInsEntity;
import com.lubase.wfengine.auto.entity.WfTaskEntity;
import com.lubase.wfengine.dao.WfOperatorDao;
import com.lubase.wfengine.model.EProcessStatus;
import com.lubase.wfengine.model.OperatorUserModel;
import com.lubase.wfengine.model.WFCmdRunModel;
import com.lubase.wfengine.service.WorkOperatorService;
import com.lubase.wfengine.service.WorkTaskService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class WfOInsTest {

    @Autowired
    WfOperatorDao operatorDao;

    @Autowired
    WorkOperatorService operatorService;
    @Autowired
    WorkTaskService taskService;
    @Autowired
    @Qualifier("userColumnServiceImpl")
    IColumnRemoteService remoteServiceById;

    @Autowired
    @Qualifier("userInfoByCodeServiceImpl")
    IColumnRemoteService remoteServiceByCode;

    @SneakyThrows
    @Test
    void testGetUser() {
        String val = "690520495543554048";
        DbEntity userEntity = remoteServiceByCode.getCacheDataByKey(val);
        if (userEntity == null) {
            userEntity = remoteServiceById.getCacheDataByKey(val);
            if (userEntity == null) {
                throw new WarnCommonException(String.format("未找到用户%s，请检查流程设置和业务数据", val));
            }
        }
        System.out.println(JSON.toJSONString(userEntity));
        System.out.println("222");
        val = "GW00139478";
        userEntity = remoteServiceByCode.getCacheDataByKey(val);
        if (userEntity == null) {
            userEntity = remoteServiceById.getCacheDataByKey(val);
            if (userEntity == null) {
                throw new WarnCommonException(String.format("未找到用户%s，请检查流程设置和业务数据", val));
            }
        }
        System.out.println(JSON.toJSONString(userEntity));
    }

    @Test
    void testUpdateOIns() {
        String id = "771074656969953280";
        WfOInsEntity oIns = operatorDao.getOInstanceById(id);
        assert oIns != null;
        assert oIns.getProcess_status().equals(EProcessStatus.UnProcess.getStatus());
        WFCmdRunModel runModel = new WFCmdRunModel();
        runModel.setCmdId("cmd");
        runModel.setCmdMemo("memo");
        Integer result = operatorService.updateOInsStatus(oIns.getId(), runModel);
        assert result == 1;
        oIns = operatorDao.getOInstanceById(id);
        assert oIns.getProcess_status().equals(EProcessStatus.Processed.getStatus());

    }

    @Test
    void test1() {
        List<DbEntity> realUserList = operatorDao.getNodeRealProcessUserList("955240900261318656", "769461010686808064");
        System.out.println(JSON.toJSONString(realUserList));
    }

    @Test
    void test2() {
        List<OperatorUserModel> userModelList = operatorDao.getNodeValidProcessUserList("925461805508071424", "769461010686808064");
        System.out.println(JSON.toJSONString(userModelList));
    }

    @Autowired
    DataAccess dataAccess;

    @Test
    void testGetUserList() {
        WfFInsEntity fIns = dataAccess.queryById("wf_fins", 925474069938180096L).getGenericData(WfFInsEntity.class).stream().findFirst().orElse(null);
        WfTaskEntity taskEntity = dataAccess.queryById("wf_task", 771670065769615360L).getGenericData(WfTaskEntity.class).stream().findFirst().orElse(null);
        DbCollection bisData = dataAccess.queryById("bs_demo", 806191813860790272L);
        List<OperatorUserModel> userModelList = taskService.getNodeProcessUserList(fIns, taskEntity, bisData);
        System.out.println(JSON.toJSONString(userModelList));
    }
}
