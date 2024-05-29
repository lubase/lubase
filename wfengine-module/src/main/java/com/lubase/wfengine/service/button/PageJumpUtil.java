package com.lubase.wfengine.service.button;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.wfengine.model.PageDataWFExtendModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class PageJumpUtil {

    public PageDataWFExtendModel getServiceId(DbEntity button, DbEntity pageEntity) {
        String settingStr = "";
        if (button.containsKey("server_setting") && !StringUtils.isEmpty(button.get("server_setting"))) {
            settingStr = TypeConverterUtils.object2String(button.get("server_setting"));
        } else if (pageEntity.containsKey("workflow_setting")) {
            settingStr = TypeConverterUtils.object2String(pageEntity.get("workflow_setting"));
        }
        PageDataWFExtendModel dataWFExtendModel = null;
        if (!StringUtils.isEmpty(settingStr)) {
            try {
                dataWFExtendModel = JSON.parseObject(settingStr, PageDataWFExtendModel.class);
            } catch (Exception exception) {
                log.error("workflow_setting配置格式不正确:" + pageEntity.getId() + settingStr, exception);
            }
        }
        return dataWFExtendModel;
    }

}
