package com.lcp.qibao.controller;

import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.EDBEntityState;
import com.lcp.qibao.model.ColumnRefPageVO;
import com.lcp.qibao.model.CustomFormVO;
import com.lcp.qibao.model.GetFormDataParamDTO;
import com.lcp.qibao.model.SaveFormParamDTO;
import com.lcp.qibao.model.customForm.ColumnLookupInfoVO;
import com.lcp.qibao.model.customForm.ColumnLookupParamModel;
import com.lcp.qibao.model.customForm.ColumnUniqueValueVO;
import com.lcp.qibao.response.ResponseData;
import com.lcp.qibao.service.PageDataService;
import com.lcp.qibao.service.RenderFormService;
import com.lcp.qibao.util.ClientMacro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 详情数据渲染
 * </p>
 *
 * @author zhulz
 * @jdk 1.8
 */
@RestController
@RequestMapping("/form")
public class FormDataController {

    @Autowired
    RenderFormService renderFormService;

    @Autowired
    PageDataService pageDataService;

    /**
     * 获取表单数据
     *
     * @param paramDTO
     * @return
     */
    @RequestMapping(value = "/getFormData", method = RequestMethod.POST)
    public ResponseData<CustomFormVO> getFormDataByFuncCode(@RequestBody GetFormDataParamDTO paramDTO) {
        if (StringUtils.isEmpty(paramDTO.getFuncCode())) {
            return ResponseData.parameterNotFound("funcCode");
        }
        if (StringUtils.isEmpty(paramDTO.getClientMacro())) {
            return ResponseData.parameterNotFound("clientMacro");
        }
        ClientMacro clientMacro = ClientMacro.init(paramDTO.getClientMacro());
        CustomFormVO customFormVO = null;
        if (StringUtils.isEmpty(paramDTO.getId())) {
            customFormVO = pageDataService.getAddDataByFuncCode(paramDTO.getPageId(), paramDTO.getFuncCode(), clientMacro);
        } else {
            customFormVO = pageDataService.getEditDataByFuncCode(paramDTO.getPageId(), paramDTO.getFuncCode(), paramDTO.getId(), clientMacro);
        }
        return ResponseData.success(customFormVO);
    }

    @RequestMapping(value = "/getFormDataByFormId", method = RequestMethod.POST)
    public ResponseData<CustomFormVO> getFormDataByFormId(@RequestBody GetFormDataParamDTO paramDTO) {
        if (StringUtils.isEmpty(paramDTO.getPageId()) || StringUtils.isEmpty(paramDTO.getFormId())) {
            return ResponseData.parameterNotFound("pageId and formId");
        }
        if (StringUtils.isEmpty(paramDTO.getClientMacro())) {
            return ResponseData.parameterNotFound("clientMacro");
        }
        ClientMacro clientMacro = ClientMacro.init(paramDTO.getClientMacro());
        CustomFormVO customFormVO = null;
        if (StringUtils.isEmpty(paramDTO.getId())) {
            customFormVO = pageDataService.getAddDataByFormId(paramDTO.getPageId(), paramDTO.getFormId(), clientMacro);
        } else {
            customFormVO = pageDataService.getEditDataByFormId(paramDTO.getPageId(), paramDTO.getFormId(), paramDTO.getId(), clientMacro);
        }
        return ResponseData.success(customFormVO);
    }

    @RequestMapping(value = "/getCopyData", method = RequestMethod.POST)
    public ResponseData<CustomFormVO> getCopyData(@RequestBody GetFormDataParamDTO paramDTO) {
        if (StringUtils.isEmpty(paramDTO.getFuncCode()) || StringUtils.isEmpty(paramDTO.getId())) {
            return ResponseData.parameterNotFound("funcCode or id");
        }
        CustomFormVO customFormVO = pageDataService.getCopyDataByFuncCode(paramDTO.getFuncCode(), paramDTO.getId());
        return ResponseData.success(customFormVO);
    }

    /**
     * 表单保存
     *
     * @param paramModel
     * @return
     */
    @RequestMapping(value = "/saveFormData", method = RequestMethod.POST)
    public ResponseData<Integer> saveFormDataByFuncCode(@RequestBody SaveFormParamDTO paramModel) {
        String funcCode;
        DbEntity entity;
        funcCode = paramModel.getFuncCode();
        entity = paramModel.getData();
        if (StringUtils.isEmpty(funcCode) || null == entity) {
            return ResponseData.parameterNotFound("funcCode");
        }
        if (entity.getDataState().equals(EDBEntityState.Modified) && StringUtils.isEmpty(entity.getId())) {
            return ResponseData.error("id is not null");
        }
        return ResponseData.success(pageDataService.saveFormDataByFuncCode(funcCode, entity));
    }

    @RequestMapping(value = "/saveFormDataByFormId", method = RequestMethod.POST)
    public ResponseData<Integer> saveFormDataByFormId(@RequestBody SaveFormParamDTO paramModel) {
        String formId = paramModel.getFormId();
        DbEntity entity = paramModel.getData();
        if (StringUtils.isEmpty(formId) || null == entity) {
            return ResponseData.parameterNotFound("formId");
        }
        if (StringUtils.isEmpty(paramModel.getPageId())) {
            return ResponseData.parameterNotFound("pageId");
        }
        if (entity.getDataState().equals(EDBEntityState.Modified) && StringUtils.isEmpty(entity.getId())) {
            return ResponseData.error("id is not null");
        }
        return ResponseData.success(pageDataService.saveFormDataByFormId(paramModel.getPageId(), formId, entity));
    }

    /**
     * 获取字段的关联信息
     *
     * @param columnLookupParam
     * @return
     */
    @RequestMapping(value = "/getColLookupData", method = RequestMethod.POST)
    public ResponseData<ColumnLookupInfoVO> getColLookupData(@RequestBody ColumnLookupParamModel columnLookupParam) {
        if (StringUtils.isEmpty(columnLookupParam.getFuncCode())) {
            return ResponseData.parameterNotFound("funcCode");
        }
        if (StringUtils.isEmpty(columnLookupParam.getColumnId())) {
            return ResponseData.parameterNotFound("columnId");
        }
        if (StringUtils.isEmpty(columnLookupParam.getFormId())) {
            return ResponseData.parameterNotFound("formId");
        }
        return ResponseData.success(renderFormService.getColLookupData(columnLookupParam));
    }

    @RequestMapping(value = "/getColServiceData", method = RequestMethod.POST)
    public ResponseData<ColumnLookupInfoVO> getColServiceData(@RequestBody ColumnLookupParamModel columnServiceParamModel) {
        if (StringUtils.isEmpty(columnServiceParamModel.getFuncCode())) {
            return ResponseData.parameterNotFound("funcCode");
        }
        if (StringUtils.isEmpty(columnServiceParamModel.getColumnId())) {
            return ResponseData.parameterNotFound("columnId");
        }
        if (StringUtils.isEmpty(columnServiceParamModel.getFormId())) {
            return ResponseData.parameterNotFound("formId");
        }
        return ResponseData.success(renderFormService.getColServiceData(columnServiceParamModel));
    }

    @RequestMapping(value = "/getColRefPageInfo", method = RequestMethod.GET)
    public ResponseData<ColumnRefPageVO> getColRefPageInfo(@RequestParam String columnId) {
        if (StringUtils.isEmpty(columnId)) {
            return ResponseData.parameterNotFound("columnId");
        }
        return ResponseData.success(renderFormService.getColRefPageInfo(columnId));
    }

    @RequestMapping(value = "/checkColUnique", method = RequestMethod.POST)
    public ResponseData<Boolean> checkColUnique(@RequestBody ColumnUniqueValueVO uniqueValueVO) {
        if (StringUtils.isEmpty(uniqueValueVO.getColumnId()) || StringUtils.isEmpty(uniqueValueVO.getColumnValue())) {
            return ResponseData.parameterNotFound("columnId or columnValue");
        }
        Boolean checkResult = renderFormService.checkFieldUniqueValue(uniqueValueVO.getColumnId(), uniqueValueVO.getColumnValue(), uniqueValueVO.getDataId());
        return ResponseData.success(checkResult);
    }
}
