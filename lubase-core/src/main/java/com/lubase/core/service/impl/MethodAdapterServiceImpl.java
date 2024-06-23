package com.lubase.core.service.impl;

import com.lubase.core.entity.SsButtonEntity;
import com.lubase.core.entity.SsPageEntity;
import com.lubase.core.extend.IInvokeMethod;
import com.lubase.core.extend.service.InvokeMethodAdapter;
import com.lubase.core.service.AppFuncDataService;
import com.lubase.core.service.MethodAdapterService;
import com.lubase.core.service.button.MoreDataService;
import com.lubase.core.service.button.OndDataService;
import com.lubase.core.service.userright.UserRightService;
import com.lubase.core.service.userright.model.UserRightInfo;
import com.lubase.core.util.InvokeDataSourceService;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.extend.ExtendAppLoadCompleteService;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.LoginUser;
import com.lubase.orm.service.AppHolderService;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TypeConverterUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author A
 */
@Slf4j
@Service
public class MethodAdapterServiceImpl implements ExtendAppLoadCompleteService, MethodAdapterService {

    @Autowired
    DataAccess dataAccess;

    @Autowired
    UserRightService userRightService;

    @Autowired
    AppHolderService appHolderService;
    @Autowired
    InvokeMethodAdapter invokeMethodAdapter;

    @Autowired
    AppFuncDataService appFuncDataService;

    List<OndDataService> ondDataServices;
    List<MoreDataService> moreDataServices;
    @Autowired
    InvokeDataSourceService invokeDataSourceService;

    @Override
    public void clearData() {
        ondDataServices = null;
        moreDataServices = null;
    }

    @Override
    public void LoadCompleted(ApplicationContext applicationContext) {
        if (ondDataServices == null) {
            ondDataServices = new ArrayList<>(applicationContext.getBeansOfType(OndDataService.class).values());
        }
        if (moreDataServices == null) {
            moreDataServices = new ArrayList<>(applicationContext.getBeansOfType(MoreDataService.class).values());
        }
    }

    @SneakyThrows
    @Override
    public Object exeMethodById(String pageId, Long methodId, HashMap<String, String> mapParam) {
        IInvokeMethod service = getMethodById(Long.parseLong(pageId), methodId);
        return service.exe(mapParam);
    }

    @SneakyThrows
    @Override
    public Object exeDataSourceById(Long appId, Long pageId, Long dataSourceId, String... queryParamList) {
        if (StringUtils.isEmpty(pageId) || StringUtils.isEmpty(dataSourceId)) {
            throw new WarnCommonException("pageId和dataSourceId不能为空");
        }
        String pageMethods = getCanAccessMethodId(pageId);
        if (StringUtils.isEmpty(pageMethods) || !pageMethods.contains(dataSourceId.toString())) {
            throw new WarnCommonException(String.format("当前页面无权访问此方法：%s", dataSourceId));
        }
        LoginUser user = appHolderService.getUser();
        UserRightInfo userRightInfo = userRightService.getUserRight(user.getId());
        if (!userRightService.checkFuncRight(userRightInfo, pageId)) {
            throw new WarnCommonException("您没有页面的访问权限，请联系管理员");
        }
        return invokeDataSourceService.queryObjectByDataSource(appId, dataSourceId, queryParamList);
    }

    @SneakyThrows
    @Override
    public Object exeMethodByIdNoRight(Long methodId, HashMap<String, String> mapParam) {
        IInvokeMethod service = getMethod(methodId);
        if (service.checkRight()) {
            throw new WarnCommonException("此方法不支持无权限访问，请联系开发人员");
        }
        return service.exe(mapParam);
    }

    @SneakyThrows
    private IInvokeMethod getMethod(Long methodId) {
        IInvokeMethod service = invokeMethodAdapter.getInvokeMethodById(methodId);
        if (service == null) {
            throw new WarnCommonException(String.format("未找到invoke方法：%s", methodId));
        }
        return service;
    }

    @SneakyThrows
    @Override
    public Object exeMethodById(String pageId, Long methodId, List<HashMap<String, String>> listMapParam) {
        IInvokeMethod service = getMethodById(Long.parseLong(pageId), methodId);
        return service.exe(listMapParam);
    }

    @SneakyThrows
    private IInvokeMethod getMethodById(Long pageId, Long methodId) {
        String pageMethods = getCanAccessMethodId(pageId);
        IInvokeMethod service = getMethod(methodId);
        if (service.checkRight()) {
            if (StringUtils.isEmpty(pageMethods) || !pageMethods.contains(methodId.toString())) {
                throw new WarnCommonException(String.format("当前页面无权访问此方法：%s", methodId));
            }
            LoginUser user = appHolderService.getUser();
            UserRightInfo userRightInfo = userRightService.getUserRight(user.getId());
            if (!userRightService.checkFuncRight(userRightInfo, pageId)) {
                throw new WarnCommonException("您没有页面的访问权限，请联系管理员");
            }
        }
        return service;
    }

    private String getCanAccessMethodId(Long pageId) {
        DbCollection collection = dataAccess.queryById(SsPageEntity.TABLE_CODE, pageId);
        String pageMethods = "";
        if (collection.getData().size() == 1) {
            pageMethods = TypeConverterUtils.object2String(collection.getData().get(0).get(SsPageEntity.COL_METHODS));
        }
        return pageMethods;
    }

    @SneakyThrows
    @Override
    public Object exeMethodByFuncCode(Long pageId, String btnCode, List<HashMap<String, String>> listMapParam) {
        Object obj = null;
        SsButtonEntity btn = getFuncInfoByFuncCode(pageId, btnCode);

        MoreDataService service = null;
        for (MoreDataService moreDataService : moreDataServices) {
            if (moreDataService.getButtonType().equals(btn.getButton_type())) {
                service = moreDataService;
            }
        }
        if (service != null) {
            obj = service.exe(btn, listMapParam);
        } else if (!StringUtils.isEmpty(btn.getMethod_id())) {
            obj = getMethod(btn.getMethod_id()).exe(listMapParam);
        } else {
            throw new WarnCommonException("methodId is not found");
        }
        return obj;
    }

    @SneakyThrows
    @Override
    public Object exeMethodByFuncCode(Long pageId, String btnId, HashMap<String, String> mapParam) {
        Object obj = null;
        SsButtonEntity btn = getFuncInfoByFuncCode(pageId, btnId);

        OndDataService service = null;
        for (OndDataService ondDataService : ondDataServices) {
            if (ondDataService.getButtonType().equals(btn.getButton_type())) {
                service = ondDataService;
            }
        }
        if (service != null) {
            obj = service.exe(btn, mapParam);
        } else if (!StringUtils.isEmpty(btn.getMethod_id())) {
            obj = getMethod(btn.getMethod_id()).exe(mapParam);
        } else {
            throw new WarnCommonException("methodId is not found");
        }
        return obj;
    }


    @SneakyThrows
    private SsButtonEntity getFuncInfoByFuncCode(Long pageId, String btnId) {
        SsButtonEntity button = appFuncDataService.getFuncInfoByFuncCode(btnId);
        Long lBtnId = Long.parseLong(btnId);
        //权限控制
        LoginUser user = appHolderService.getUser();
        UserRightInfo userRightInfo = userRightService.getUserRight(user.getId());
        Long funcId = lBtnId;
        //表单自定义按钮的funcId 需要当前页面id+btnId
        if (button != null && button.containsKey("ref_form_id")) {
            funcId = pageId + lBtnId;
        }
        if (userRightService.checkFuncRight(userRightInfo, funcId)) {
            return button;
        } else {
            throw new WarnCommonException("func is not found or no right access");
        }
    }

}
