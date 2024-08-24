package com.lubase.core.invoke;

import com.lubase.core.extend.IInvokeMethod;
import com.lubase.core.service.RenderFormService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * 获取表单子表数据
 */
public class GetFormChildTableData implements IInvokeMethod {
    @Autowired
    RenderFormService renderFormService;

    @Override
    public String getDescription() {
        return "获取表单子表的数据";
    }

    @Override
    public boolean checkRight() {
        return false;
    }


    @Override
    public String getId() {
        return "688087055355351040";
    }

    /**
     * 此方法暂时不考虑子表数据翻页，如果翻页需要再传递queryParam 参数
     *
     * @param mapParam key="dataId",value="字段值"
     *                 key="serialNum",value="随机唯一值"
     *                 key="formId",value="formId"
     * @return
     */
    @Override
    @SneakyThrows
    public Object exe(HashMap<String, String> mapParam) {
        return renderFormService.getFormChildTableData(mapParam);
    }

}
