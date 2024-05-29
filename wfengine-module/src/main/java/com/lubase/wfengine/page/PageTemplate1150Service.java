package com.lubase.wfengine.page;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.multiDataSource.ChangeDataSourceService;
import com.lubase.orm.service.AppHolderService;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;
import com.lubase.core.extend.PageTemplateExtendService;
import com.lubase.core.util.ClientMacro;
import com.lubase.wfengine.model.PageDataWFExtendModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 待办和已办页面获取数据
 */
@Slf4j
@Service
public class PageTemplate1150Service implements PageTemplateExtendService {

    @Autowired
    DataAccess dataAccess;

    @Autowired
    AppHolderService appHolderService;

    @Autowired
    ChangeDataSourceService changeDataSourceService;
    String processStatusKey = "@@C.processStatus";

    @SneakyThrows
    @Override
    public void beforeExecuteQuery(DbEntity pageEntity, QueryOption queryOption, ClientMacro clientMacro) {

        if (clientMacro == null || !clientMacro.containsKey(processStatusKey)) {
            throw new WarnCommonException("客户端宏变量未包含processStatus，请联系管理员");
        }
        String processStatus = clientMacro.get(processStatusKey);
        if (!processStatus.equals("0") && !processStatus.equals("1")) {
            throw new WarnCommonException("客户端宏变量processStatus值设置不正确，请联系管理员");
        }
        PageDataWFExtendModel dataWFExtendModel = getExtendModel(pageEntity);
        if (dataWFExtendModel == null || StringUtils.isEmpty(dataWFExtendModel.getServiceId())) {
            throw new WarnCommonException("页面流程配置不正确");
        }
        List<String> dataIds = getDataIds(dataWFExtendModel.getServiceId(), processStatus);

        TableFilterWrapper filterWrapper = TableFilterWrapper.and();
        if (dataIds.size() == 0) {
            filterWrapper.eq("id", 0);
        } else {
            filterWrapper.in("id", String.join(",", dataIds));
            TableFilter originalFilter = queryOption.getTableFilter();
            if (originalFilter != null) {
                filterWrapper.addFilter(originalFilter);
            }
        }
        queryOption.setTableFilter(filterWrapper.build());
    }

    @Override
    public void beforeReturnMainData(DbEntity dbEntity, DbCollection dbCollection, ClientMacro clientMacro) {
        DbTable table = dbCollection.getTableInfo();
        String colCode = "_process_status";
        DbField field = new DbField();
        field.setId(colCode);
        field.setCode(field.getId());
        field.setName("页签");
        field.setVisible(0);
        field.setEleType("1");
        table.getFieldList().add(field);
        String processStatus = clientMacro.get(processStatusKey);
        for (DbEntity entity : dbCollection.getData()) {
            entity.put(colCode, processStatus);
        }
    }

    PageDataWFExtendModel getExtendModel(DbEntity pageEntity) {
        String settingStr = "";
        if (pageEntity.containsKey("workflow_setting")) {
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

    /**
     * 根据业务场景标识获取待办数据id列表
     *
     * @param serviceId 流程引擎中业务场景id
     * @return
     */
    List<String> getDataIds(String serviceId, String processStatus) {
        String userId = appHolderService.getUser().getId().toString();
        changeDataSourceService.changeDataSourceByTableCode("wf_app");
        return dataAccess.procGetStringList("proc_getUserProcessIds", userId, serviceId, processStatus);
    }

    @Override
    public String getTemplateCode() {
        return "1150";
    }
}
