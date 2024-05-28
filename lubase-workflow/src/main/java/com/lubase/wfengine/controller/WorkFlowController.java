package com.lubase.wfengine.controller;

import com.lubase.core.exception.ParameterNotFoundException;
import com.lubase.core.model.LoginUser;
import com.lubase.core.service.AppHolderService;
import com.lubase.starter.config.PassToken;
import com.lubase.starter.response.ResponseData;
import com.lubase.wfengine.model.WFCmdRunModel;
import com.lubase.wfengine.service.WorkFlowService;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("wft")
public class WorkFlowController {

    @Autowired
    WorkFlowService workFlowService;
    @Autowired
    AppHolderService holderService;

    /**
     * @param serviceId 业务场景id
     * @param dataId    业务数据主键
     * @return
     */
    @SneakyThrows
    @RequestMapping(value = "start", method = RequestMethod.POST)
    public ResponseData<Integer> start(@NonNull String serviceId, @NonNull String dataId) {
        if (StringUtils.isEmpty(serviceId) || StringUtils.isEmpty(dataId)) {
            throw new ParameterNotFoundException("serviceId和dataId不能为空");
        }
        String userId = holderService.getUser().getId().toString();
        Integer result = workFlowService.startWf(serviceId, dataId, userId);
        return ResponseData.success(result);
    }

    @PassToken
    @SneakyThrows
    @RequestMapping(value = "manualStart", method = RequestMethod.POST)
    public ResponseData<Integer> manualStart(@NonNull String serviceId, @NonNull String dataId, @NonNull String userId) {
        if (StringUtils.isEmpty(serviceId) || StringUtils.isEmpty(dataId) || StringUtils.isEmpty(userId)) {
            throw new ParameterNotFoundException("serviceId和dataId不能为空");
        }
        try {
            LoginUser loginUser = holderService.getUser();
            if (loginUser == null) {
                loginUser = new LoginUser();
                loginUser.setId(Long.parseLong(userId));
                holderService.setUser(loginUser);
            }
        } catch (Exception exception) {
            log.error("manualStart流程引擎初始化用户信息失败", exception);
        }
        Integer result = workFlowService.startWf(serviceId, dataId, userId);
        return ResponseData.success(result);
    }

    /**
     * 流程流转
     *
     * @param runModel
     * @return
     */
    @SneakyThrows
    @RequestMapping(value = "run", method = RequestMethod.POST)
    public ResponseData<Integer> run(@RequestBody WFCmdRunModel runModel) {
        if (StringUtils.isEmpty(runModel.getOInsId()) || StringUtils.isEmpty(runModel.getCmdId())) {
            throw new ParameterNotFoundException("oInsId和cmdId不能为空");
        }
        String userId = holderService.getUser().getId().toString();
        Integer result = workFlowService.run(userId, runModel);
        return ResponseData.success(result);
    }

    /**
     * 流程流转
     *
     * @param runModel
     * @return
     */
    @SneakyThrows
    @RequestMapping(value = "saveData", method = RequestMethod.POST)
    public ResponseData<Integer> saveData(@RequestBody WFCmdRunModel runModel) {
        if (StringUtils.isEmpty(runModel.getOInsId())
                || (StringUtils.isEmpty(runModel.getCmdMemo()) && runModel.getData() == null)) {
            throw new ParameterNotFoundException("oInsId");
        }
        String userId = holderService.getUser().getId().toString();
        Integer result = workFlowService.saveData(userId, runModel);
        return ResponseData.success(result);
    }

    @PassToken
    @RequestMapping(value = "autoProcessTimeoutTIns", method = RequestMethod.GET)
    public ResponseData<Integer> saveData() {
        workFlowService.processTimeoutTaskIns();
        return ResponseData.success(1);
    }
}
