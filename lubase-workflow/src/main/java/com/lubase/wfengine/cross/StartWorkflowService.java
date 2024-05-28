package com.lubase.wfengine.cross;

import com.lubase.core.exception.WarnCommonException;
import com.lubase.starter.extend.CrossComponentService;
import com.lubase.wfengine.service.StartFlow;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class StartWorkflowService implements CrossComponentService {

    @Autowired
    StartFlow startFlow;

    @SneakyThrows
    @Override
    public String getData(String startParam) {
        log.info("启动参数：{}", startParam);
        if (StringUtils.isEmpty(startParam) || !startParam.contains(",")) {
            throw new WarnCommonException("参数错误");
        }
        var param = startParam.split(",");
        if (param.length < 3) {
            throw new WarnCommonException("参数错误");
        }
        String serviceId = param[0];
        String dataId = param[1];
        String userId = param[2];
        Integer result = startFlow.startWfByApi(serviceId, dataId, userId);
        return result.toString();
    }

    @Override
    public String getServiceIdentification() {
        return "773210250068103168";
    }

    @Override
    public String getDescription() {
        return "流程引擎：通过跨服务引擎启动流程";
    }
}