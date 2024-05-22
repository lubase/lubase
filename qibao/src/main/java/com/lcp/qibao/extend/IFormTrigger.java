package com.lcp.qibao.extend;

import com.lcp.coremodel.DbEntity;
import com.lcp.qibao.model.CustomFormVO;
import com.lcp.qibao.util.ClientMacro;

/**
 * 表单触发器
 *
 * @author A
 */
public interface IFormTrigger {

    /**
     * 表代码
     *
     * @return
     */
    String getTriggerTableCode();

    String getDescription();


    /**
     * 表单新增时，设置表单的默认值
     *
     * @param formDataEntity 表单数据对象
     * @param clientMacro    客户端宏变量
     */
    void initDefaultValue(DbEntity formDataEntity, ClientMacro clientMacro);

    /**
     * 加载表单之前处理
     *
     * @param customFormVO 表单对象
     * @param clientMacro  客户端宏变量
     */
    void beforeLoadForm(CustomFormVO customFormVO, ClientMacro clientMacro);
}
