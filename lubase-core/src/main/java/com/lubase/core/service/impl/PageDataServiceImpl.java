package com.lubase.core.service.impl;

import com.lubase.core.model.ButtonRefFormInfo;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.EButtonType;
import com.lubase.orm.model.LoginUser;
import com.lubase.orm.service.AppHolderService;
import com.lubase.model.DbEntity;
import com.lubase.core.model.CustomFormVO;
import com.lubase.core.model.FormButtonVO;
import com.lubase.core.service.AppFuncDataService;
import com.lubase.core.service.CustomFormUpdateService;
import com.lubase.core.service.PageDataService;
import com.lubase.core.service.RenderFormService;
import com.lubase.core.service.userright.UserRightService;
import com.lubase.core.service.userright.model.UserRightInfo;
import com.lubase.core.util.ClientMacro;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
                if (button.getBtnType().equals(EButtonType.ViewInfo.getStringValue()) || userRightService.checkFuncRight(userRightInfo, funcId)) {
                    rightButtonList.add(button);
                }
            }
            formVO.setBtns(rightButtonList);
        }
    }

    @Override
    public CustomFormVO getEditDataByFuncCode(String pageId, String funcCode, String dataId, ClientMacro clientMacro) {
        ButtonRefFormInfo refFormInfo = appFuncDataService.getRefFormInfoByFuncCode(funcCode);
        // 查询类型按钮无需控制权限
        if (!refFormInfo.getButtonType().equals(EButtonType.ViewInfo.getStringValue())) {
            checkFormAuthority(funcCode);
        }
        CustomFormVO formVO = renderFormService.getEditDataByFormId(refFormInfo.getRefFormId(), dataId, clientMacro);
        // 如果是查询详情按钮，则表单只读
        if (refFormInfo.getButtonType().equals(EButtonType.ViewInfo.getStringValue())) {
            formVO.setReadonly(true);
        }
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

    @SneakyThrows
    @Override
    public int saveFormDataByFuncCode(String funcCode, DbEntity dbEntity) {
        checkFormAuthority(funcCode);
        ButtonRefFormInfo refFormInfo = appFuncDataService.getRefFormInfoByFuncCode(funcCode);
        // 判断是否保存
        String buttonType = refFormInfo.getButtonType();
        if (buttonType.equals(EButtonType.Add.getStringValue()) || buttonType.equals(EButtonType.TreeEdit.getStringValue()) || buttonType.equals(EButtonType.Edit.getStringValue())) {
            if (refFormInfo.getIsFormChildTable()) {
                return customFormUpdateService.saveChildTableFormData(refFormInfo.getRefFormId(), dbEntity);
            } else {
                return customFormUpdateService.saveFormData(refFormInfo.getRefFormId(), dbEntity);
            }
        } else {
            throw new WarnCommonException("配置错误，此类型按钮无法保存表单");
        }
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
