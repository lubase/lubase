package com.lubase.wfengine.remote;
import com.lubase.core.exception.WarnCommonException;
import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
import com.lubase.starter.extend.WorkFlowExtendForService;
import com.lubase.wfengine.auto.entity.WfServiceEntity;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestWFRemoteService implements WorkFlowExtendForService {
    @Autowired
    DataAccess dataAccess;

    @Override
    public String getServiceId() {
        return "769343035275218944222";
    }

    @Override
    public String getDescription() {
        return "模拟员工入职流程";
    }

    @SneakyThrows
    @Override
    public DbCollection getData(String serviceId, String dataId) {
        DbCollection collService = dataAccess.queryById(WfServiceEntity.TABLE_CODE, Long.parseLong(serviceId), WfServiceEntity.COL_MAIN_TABLE);
        if (collService.getData().size() == 0) {
            throw new WarnCommonException(String.format("流程模板配置不正确, serviceId is %s", serviceId));
        }
        String mainTable = collService.getData().get(0).get(WfServiceEntity.COL_MAIN_TABLE).toString();
        DbCollection collection = dataAccess.queryById(mainTable, Long.parseLong(dataId));
        if (collection.getData().size() == 0) {
            throw new WarnCommonException(String.format("未找到业务数据，请联系管理员, dataId is %s", dataId));
        }
        return collection;
    }

    @Override
    public void registerEvent(String eventType, String msg) {
        log.warn(String.format("远端服务：收到流程引擎消息。type is %s ,data id is %s", eventType, msg));
    }
}
