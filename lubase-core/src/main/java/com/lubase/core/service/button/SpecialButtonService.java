package com.lubase.core.service.button;

import com.alibaba.fastjson.JSON;
import com.lubase.core.entity.DmCustomFormEntity;
import com.lubase.core.entity.SsButtonEntity;
import com.lubase.core.model.ButtonServerSettingModel;
import com.lubase.core.model.customForm.ChildTableSetting;
import com.lubase.core.service.CustomFormDataService;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.util.TypeConverterUtils;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

/**
 * 此服务按说应该抽象到页面服务中
 */
public interface SpecialButtonService {
    /**
     * 获取主表code
     *
     * @param button
     * @return
     * @throws InvokeCommonException
     */
    @SneakyThrows
    default ButtonServerSettingModel getMainTableCode(CustomFormDataService customFormDataService, SsButtonEntity button) throws InvokeCommonException {
        // 通过是否有serial_num 来识别是子表按钮
        Boolean isFormButton = button.containsKey("serial_num");
        if (isFormButton) {
            //1、获取表单子表查询对象
            DmCustomFormEntity formEntity = customFormDataService.selectById(button.get("form_id").toString());
            if (formEntity == null) {
                throw new InvokeCommonException("根据按钮无法找到归属表单，请联系管理员");
            }
            ChildTableSetting childTable = customFormDataService.getChildTableFromSettingStr(formEntity.getChild_table(), button.get("serial_num").toString());
            // 子表删除按钮暂时只支持物理删除
            if (childTable == null || childTable.getQueryOption() == null || StringUtils.isEmpty(childTable.getQueryOption().getTableName())) {
                throw new WarnCommonException("子表数据源为配置TableName属性");
            }
            String mainTableCode = childTable.getQueryOption().getTableName();
            ButtonServerSettingModel serverSettingModel = new ButtonServerSettingModel();
            serverSettingModel.setMainTableCode(mainTableCode);
            serverSettingModel.setIsLogicDelete(false);
            return serverSettingModel;
        } else {
            String serverSettingStr = TypeConverterUtils.object2String(button.get("server_setting"), "");
            if (!StringUtils.isEmpty(serverSettingStr)) {
                ButtonServerSettingModel serverSettingModel = JSON.parseObject(serverSettingStr, ButtonServerSettingModel.class);
                if (serverSettingModel != null && !StringUtils.isEmpty(serverSettingModel.getMainTableCode())) {
                    return serverSettingModel;
                }
            }
            throw new InvokeCommonException("按钮未配置主表信息，请联系管理员进行配置");
        }
    }
}
