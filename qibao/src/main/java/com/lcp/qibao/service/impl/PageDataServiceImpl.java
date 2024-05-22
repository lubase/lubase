package com.lcp.qibao.service.impl;

import com.lcp.core.model.LoginUser;
import com.lcp.core.service.AppHolderService;
import com.lcp.coremodel.DbEntity;
import com.lcp.qibao.model.CustomFormVO;
import com.lcp.qibao.model.FormButtonVO;
import com.lcp.qibao.service.AppFuncDataService;
import com.lcp.qibao.service.CustomFormUpdateService;
import com.lcp.qibao.service.PageDataService;
import com.lcp.qibao.service.RenderFormService;
import com.lcp.qibao.service.userright.UserRightService;
import com.lcp.qibao.service.userright.model.UserRightInfo;
import com.lcp.qibao.util.ClientMacro;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取页面data数据
 *
 * @author A
 */
@Slf4j
@Service
public class PageDataServiceImpl implements PageDataService {

    @Autowired
    UserRightService userRightService;

    @Autowired
    AppHolderService appHolderService;

    @Autowired
    RenderFormService renderFormService;
    @Autowired
    CustomFormUpdateService customFormUpdateService;

    @Autowired
    AppFuncDataService appFuncDataService;

    @Override
    public CustomFormVO getAddDataByFuncCode(String pageId, String funcCode, ClientMacro clientMacro) {
        checkFormAuthority(funcCode);
        String formId = appFuncDataService.getFormIdByFuncCode(funcCode);
        CustomFormVO formVO = renderFormService.getAddDataByFormId(formId, clientMacro);
        checkFormButtonRight(formVO, pageId);
        return formVO;
    }

    @Override
    public CustomFormVO getAddDataByFormId(String pageId, String formId, ClientMacro clientMacro) {
        appFuncDataService.checkPageContainsForm(pageId, formId);
        CustomFormVO formVO = renderFormService.getAddDataByFormId(formId, clientMacro);
        checkFormButtonRight(formVO, pageId);
        return formVO;
    }

    void checkFormButtonRight(CustomFormVO formVO, String pageId) {
        List<FormButtonVO> allButtonList = formVO.getBtns();
        if (allButtonList.size() > 0) {
            //权限控制
            List<FormButtonVO> rightButtonList = new ArrayList<>();
            LoginUser user = appHolderService.getUser();
            UserRightInfo userRightInfo = userRightService.getUserRight(user.getId());
            Long funcId = 0L;
            Long lPageId = Long.parseLong(pageId);
            for (FormButtonVO button : allButtonList) {
                //表单自定义按钮的funcId 需要当前页面id+btnId
                funcId = lPageId + button.getId();
                if (userRightService.checkFuncRight(userRightInfo, funcId)) {
                    rightButtonList.add(button);
                }
            }
            formVO.setBtns(rightButtonList);
        }
    }

    @Override
    public CustomFormVO getEditDataByFuncCode(String pageId, String funcCode, String dataId, ClientMacro clientMacro) {
        checkFormAuthority(funcCode);
        String formId = appFuncDataService.getFormIdByFuncCode(funcCode);
        CustomFormVO formVO = renderFormService.getEditDataByFormId(formId, dataId, clientMacro);
        checkFormButtonRight(formVO, pageId);
        return formVO;
    }

    @Override
    public CustomFormVO getEditDataByFormId(String pageId, String formId, String dataId, ClientMacro clientMacro) {
        appFuncDataService.checkPageContainsForm(pageId, formId);
        CustomFormVO formVO = renderFormService.getEditDataByFormId(formId, dataId, clientMacro);
        checkFormButtonRight(formVO, pageId);
        return formVO;
    }

    @Override
    public CustomFormVO getCopyDataByFuncCode(String funcCode, String dataId) {
        checkFormAuthority(funcCode);
        String formId = appFuncDataService.getFormIdByFuncCode(funcCode);
        return renderFormService.getCopyDataByFormId(formId, dataId);
    }

    @Override
    public int saveFormDataByFuncCode(String funcCode, DbEntity dbEntity) {
        checkFormAuthority(funcCode);
        String formId = appFuncDataService.getFormIdByFuncCode(funcCode);
        return customFormUpdateService.saveFormData(formId, dbEntity);
    }

    @Override
    public int saveFormDataByFormId(String pageId, String formId, DbEntity dbEntity) {
        appFuncDataService.checkPageContainsForm(pageId, formId);
        return customFormUpdateService.saveFormData(formId, dbEntity);
    }

    private Boolean checkFormAuthority(String funcCode) {
        //TODO：需要先判断权限
        return true;
    }

}
