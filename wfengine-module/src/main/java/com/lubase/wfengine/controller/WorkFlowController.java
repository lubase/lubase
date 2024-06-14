package com.lubase.wfengine.controller;

import com.lubase.model.DbEntity;
import com.lubase.orm.exception.ParameterNotFoundException;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.extend.IColumnRemoteService;
import com.lubase.orm.model.LoginUser;
import com.lubase.orm.service.AppHolderService;
import com.lubase.core.config.PassToken;
import com.lubase.core.response.ResponseData;
import com.lubase.wfengine.model.WFCmdRunModel;
import com.lubase.wfengine.service.WorkFlowService;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    @Qualifier("userColumnServiceImpl")
    IColumnRemoteService remoteServiceById;

    @Autowired
    @Qualifier("userInfoByCodeServiceImpl")
    IColumnRemoteService remoteServiceByCode;

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
                DbEntity userInfo = remoteServiceById.getCacheDataByKey(userId);
                if (userInfo == null) {
                    userInfo = remoteServiceByCode.getCacheDataByKey(userId);
                }
                if (userInfo == null) {
                    throw new WarnCommonException("用户" + userId + "不存在");
                }
                loginUser = new LoginUser();
                loginUser.setId(userInfo.getId());
                loginUser.setCode(userInfo.get("user_code").toString());
                loginUser.setName(userInfo.get("user_name").toString());
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
