package com.lcp.wfengine;

import com.lubase.core.exception.WarnCommonException;
import com.lubase.core.extend.IColumnRemoteService;
import com.lubase.core.model.DbCollection;
import com.lubase.core.model.LoginUser;
import com.lubase.core.service.AppHolderService;
import com.lubase.core.service.DataAccess;
import com.lubase.model.DbEntity;
import com.lubase.wfengine.auto.entity.WfFInsEntity;
import com.lubase.wfengine.model.WFCmdRunModel;
import com.lubase.wfengine.remote.RemoteBisDataService;
import com.lubase.wfengine.service.WorkFlowService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class WorkFlowServiceTest {

    @Autowired
    WorkFlowService flowService;

    @Autowired
    RemoteBisDataService remoteBisDataService;

    @Autowired
    AppHolderService appHolderService;

    @Test
    void testAll() {
        testStartWf();
    }

    @Test
    void testStartWf() {
        LoginUser user = new LoginUser();
        user.setCode("admin1");
        user.setName("admin1_aa");
        user.setId(688164070687248384L);
        appHolderService.setUser(user);


        String serviceId = "769343035275218944";
        String dataId = "811659013786701824";
        String userId = "688164070687248384";  //   688164070687248384 admin1
        flowService.startWf(serviceId, dataId, userId);
    }

    @Test
    void testRestartWf() {
        String oInsId = "814555743658184704";
        String userId = "688164070687248384";
        assert flowService.restartWf(oInsId, userId) == 1;
    }

    @Test
    void testRun() {
        String userId = "688164070687248384"; //admin1
        WFCmdRunModel runModel = new WFCmdRunModel();
        runModel.setOInsId("771676722213949440");
        runModel.setCmdId("771671412879396864");
        runModel.setCmdMemo("模拟用户提交");
        flowService.run(userId, runModel);

    }

    @Test
    void testTransfer() {
        String userId = "688163728524316672"; //admin1
        WFCmdRunModel runModel = new WFCmdRunModel();
        runModel.setOInsId("916577428627787776");
        runModel.setCmdId("916477796660809728");
        runModel.setCmdMemo("模拟用户转办");
        runModel.setDesignationUser("admin1");
        flowService.run(userId, runModel);
    }


    @Autowired
    DataAccess dataAccess;

    @Test
    void testLoadBisData() {
        String finsId = "813820583820136448";
        List<WfFInsEntity> list = dataAccess.queryById(WfFInsEntity.TABLE_CODE, Long.parseLong(finsId)).getGenericData(WfFInsEntity.class);
        assert list.size() == 1;
        WfFInsEntity fIns = list.get(0);
        DbCollection bisColl = remoteBisDataService.getBisData(fIns.getService_id(), fIns.getData_id());
        assert bisColl.getData().size() == 1;
        System.out.println(bisColl.getData().get(0));
    }

    @Autowired
    @Qualifier("userColumnServiceImpl")
    IColumnRemoteService columnRemoteService;

    @SneakyThrows
    @Test
    void testGetUserInfo() {
        String userId = "812983058956292096";
        DbEntity userEntity = columnRemoteService.getCacheDataByKey(userId);
        if (userEntity == null) {
            throw new WarnCommonException(String.format("未找到用户%s，请检查流程设置和业务数据", userId));
        }
        System.out.println("tableKey is " + userEntity.get(columnRemoteService.tableKey()));
        String userName = userEntity.get(columnRemoteService.displayCol()).toString();
        System.out.println("userName is " + userName);
        System.out.println(userEntity);
    }

    @Test
    void testAutoProcessTimeout() {
        flowService.processTimeoutTaskIns();
    }
}
