package com.lubase.starter.controller;


import com.lubase.core.service.IDGenerator;
import com.lubase.starter.config.PassToken;
import com.lubase.starter.model.InvokeMethodParamDTO;
import com.lubase.starter.model.InvokeMethodParamMoreDataDTO;
import com.lubase.starter.response.ResponseData;
import com.lubase.starter.service.MethodAdapterService;
import com.lubase.starter.util.InvokeDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author A
 * @since 2021-12-08
 */
@RestController
@RequestMapping("/invoke")
public class InvokeController {


    @Autowired
    MethodAdapterService methodAdapterService;

    @Autowired
    InvokeDataSourceService invokeDataSourceService;

    @Autowired
    CacheManager cacheManager;
    @Autowired
    IDGenerator idGenerator;

    @RequestMapping(value = "/func", method = RequestMethod.POST)
    public ResponseData<Object> invokeOneData(@RequestBody InvokeMethodParamDTO methodParamModel) {
        if (StringUtils.isEmpty(methodParamModel.getFuncCode())) {
            return ResponseData.parameterNotFound("funcCode or pageId");
        }
        Long pageId = 0L;
        if (!StringUtils.isEmpty(methodParamModel.getPageId())) {
            pageId = methodParamModel.getPageId();
        }
        //通过变通方式来传递客户端宏变量
        if (methodParamModel.getData() == null) {
            methodParamModel.setData(new HashMap<>());
        }
        methodParamModel.getData().put("__client_macro", methodParamModel.getClientMacro());
        if (methodParamModel.getAppId() != null && methodParamModel.getAppId() > 0) {
            methodParamModel.getData().put("appId", methodParamModel.getAppId().toString());
        }
        return ResponseData.success(methodAdapterService.exeMethodByFuncCode(pageId, methodParamModel.getFuncCode(), methodParamModel.getData()));
    }

    @RequestMapping(value = "/method", method = RequestMethod.POST)
    public ResponseData<Object> method(@RequestBody InvokeMethodParamDTO methodParamModel) {
        if (methodParamModel == null || StringUtils.isEmpty(methodParamModel.getMethodId())
                || StringUtils.isEmpty(methodParamModel.getPageId())) {
            return ResponseData.parameterNotFound("methodId or pageId");
        }
        if (methodParamModel.getAppId() != null && methodParamModel.getAppId() > 0) {
            methodParamModel.getData().put("appId", methodParamModel.getAppId().toString());
        }
        return ResponseData.success(methodAdapterService.exeMethodById(methodParamModel.getPageId().toString(), methodParamModel.getMethodId(), methodParamModel.getData()));
    }

    @RequestMapping(value = "/datasource", method = RequestMethod.POST)
    public ResponseData<Object> datasource(@RequestBody InvokeMethodParamDTO methodParamModel) {
        if (methodParamModel == null || StringUtils.isEmpty(methodParamModel.getMethodId())
                || StringUtils.isEmpty(methodParamModel.getAppId()) || StringUtils.isEmpty(methodParamModel.getPageId())) {
            return ResponseData.parameterNotFound("methodId or pageId");
        }
        List<String> listParams = new ArrayList<>();
        for (Integer i = 1; i <= 10; i++) {
            if (!methodParamModel.getData().containsKey("p" + i)) {
                break;
            }
            listParams.add(methodParamModel.getData().get("p" + i));
        }
        String[] queryParams = new String[listParams.size()];
        for (Integer i = 0; i < listParams.size(); i++) {
            queryParams[i] = listParams.get(i);
        }
        Object returnObj = methodAdapterService.exeDataSourceById(methodParamModel.getAppId(), methodParamModel.getPageId(), methodParamModel.getMethodId(), queryParams);
        return ResponseData.success(returnObj);
    }

    @RequestMapping(value = "/funcMoreData", method = RequestMethod.POST)
    public ResponseData<Object> invokeMoreData(@RequestBody InvokeMethodParamMoreDataDTO methodParamModel) {
        if (StringUtils.isEmpty(methodParamModel.getFuncCode())) {
            return ResponseData.parameterNotFound("funcCode");
        }
        //通过变通方式来传递客户端宏变量
        if (methodParamModel.getDataset() != null && methodParamModel.getDataset().size() > 0) {
            for (HashMap<String, String> map : methodParamModel.getDataset()) {
                map.put("__client_macro", methodParamModel.getClientMacro());
            }
        }
        return ResponseData.success(methodAdapterService.exeMethodByFuncCode(methodParamModel.getPageId(), methodParamModel.getFuncCode(), methodParamModel.getDataset()));
    }

    @RequestMapping(value = "/methodMoreData", method = RequestMethod.POST)
    public ResponseData<Object> methodMoreData(@RequestBody InvokeMethodParamMoreDataDTO methodParamModel) {
        if (methodParamModel == null || StringUtils.isEmpty(methodParamModel.getMethodId())
                || StringUtils.isEmpty(methodParamModel.getPageId())) {
            return ResponseData.parameterNotFound("methodId or pageId");
        }
        return ResponseData.success(methodAdapterService.exeMethodById(methodParamModel.getPageId().toString(), methodParamModel.getMethodId(), methodParamModel.getDataset()));
    }

    @PassToken
    @RequestMapping(value = "/methodNoRight", method = RequestMethod.POST)
    public ResponseData<Object> methodNoRight(@RequestBody InvokeMethodParamDTO methodParamModel) {
        if (methodParamModel == null || StringUtils.isEmpty(methodParamModel.getMethodId())) {
            return ResponseData.parameterNotFound("methodId");
        }
        if (methodParamModel.getAppId() != null && methodParamModel.getAppId() > 0) {
            methodParamModel.getData().put("appId", methodParamModel.getAppId().toString());
        }
        return ResponseData.success(methodAdapterService.exeMethodByIdNoRight(methodParamModel.getMethodId(), methodParamModel.getData()));
    }

    @PassToken
    @RequestMapping(value = "/datasourceNoRight", method = RequestMethod.POST)
    public ResponseData<Object> datasourceNoRight(@RequestBody InvokeMethodParamDTO methodParamModel) {
        if (methodParamModel == null || StringUtils.isEmpty(methodParamModel.getMethodId())
                || StringUtils.isEmpty(methodParamModel.getAppId()) || StringUtils.isEmpty(methodParamModel.getPageId())) {
            return ResponseData.parameterNotFound("methodId or pageId");
        }
        List<String> listParams = new ArrayList<>();
        for (Integer i = 1; i <= 10; i++) {
            if (!methodParamModel.getData().containsKey("p" + i)) {
                break;
            }
            listParams.add(methodParamModel.getData().get("p" + i));
        }
        String[] queryParams = new String[listParams.size()];
        for (Integer i = 0; i < listParams.size(); i++) {
            queryParams[i] = listParams.get(i);
        }
        Object returnObj = invokeDataSourceService.queryObjectByDataSourceNoRight(methodParamModel.getAppId(), methodParamModel.getMethodId(), queryParams);
        return ResponseData.success(returnObj);
    }

    @PassToken
    @RequestMapping(value = "getOneId", method = RequestMethod.GET)
    public ResponseData<String> getOneId() {
        Long id = idGenerator.nextId();
        return ResponseData.success(id.toString());
    }
}
