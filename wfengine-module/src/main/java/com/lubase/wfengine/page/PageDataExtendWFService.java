package com.lubase.wfengine.page;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.multiDataSource.ChangeDataSourceService;
import com.lubase.orm.service.AppHolderService;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.core.extend.PageDataExtendService;
import com.lubase.core.util.ClientMacro;
import com.lubase.wfengine.model.PageDataWFExtendModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class PageDataExtendWFService implements PageDataExtendService {
    @Autowired
    DataAccess dataAccess;

    @Autowired
    AppHolderService appHolderService;

    @Autowired
    ChangeDataSourceService changeDataSourceService;

    @Override
    public String getPageId() {
        //对所有页面生效
        return "811615721816592384";
    }

    @Override
    public Boolean allPageValid() {
        return true;
    }

    @Override
    public String getDescription() {
        return "工作流引擎：主列表查询启用待办数据过滤功能";
    }

    @SneakyThrows
    @Override
    public void beforeExecuteQuery(DbEntity pageEntity, QueryOption queryOption, ClientMacro clientMacro) {
        PageDataWFExtendModel dataWFExtendModel = getExtendModel(pageEntity);
        if (dataWFExtendModel == null) {
            return;
        }
        if (StringUtils.isEmpty(dataWFExtendModel.getServiceId())) {
            throw new WarnCommonException("页面流程配置不正确");
        }
        //如果不配置状态则不过滤，为了兼容以前功能
        if (StringUtils.isEmpty(dataWFExtendModel.getProcessStatus())) {
            return;
        }
        List<String> dataIds = getDataIds(dataWFExtendModel.getServiceId(), dataWFExtendModel.getProcessStatus().toString());

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
        return dataAccess.procGetStringList("wf_app", "proc_getUserProcessIds", userId, serviceId, processStatus);
    }
}
