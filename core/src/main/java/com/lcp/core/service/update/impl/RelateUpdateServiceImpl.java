package com.lcp.core.service.update.impl;

import com.alibaba.fastjson.JSONArray;
import com.lcp.core.QueryOption;
import com.lcp.core.model.DbCollection;
import com.lcp.core.model.RelateUpdateModel;
import com.lcp.core.TableFilter;
import com.lcp.core.operate.EOperateMode;
import com.lcp.core.service.RegisterColumnInfoService;
import com.lcp.core.service.TableUpdateSettingCacheDataService;
import com.lcp.core.service.query.DataAccessQueryCoreService;
import com.lcp.core.service.update.RelateUpdateService;
import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.DbTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RelateUpdateServiceImpl implements RelateUpdateService {

    @Qualifier("registerColumnInfoServiceApi")
    @Autowired
    RegisterColumnInfoService registerColumnInfoService;
    @Autowired
    DataAccessQueryCoreService dataAccessQueryCoreService;

    @Autowired
    TableUpdateSettingCacheDataService cacheDataService;

    /**
     * 根据表id获取级联更新规则
     *
     * @param tableId
     * @return
     */
    public List<DbEntity> getRelateRuleList(String tableId) {
        return cacheDataService.getTableRelateSettingList()
                .stream().filter(c -> c.get("current_table_id").toString().equals(tableId)).collect(Collectors.toList());
    }

    /**
     * @param entity dm_relate_update entity
     * @return
     */
    public List<RelateUpdateModel> getWriteRule(DbEntity entity) {
        List<RelateUpdateModel> list = new ArrayList<>();
        if (!StringUtils.isEmpty(entity.get("write_rule"))) {
            try {
                list = JSONArray.parseArray(entity.get("write_rule").toString(), RelateUpdateModel.class);
            } catch (Exception ex) {
                log.error("级联更新规则配置错误", ex);
            }
        }
        return list;
    }

    public DbEntity buildTargetEntity(DbEntity targetEntity, DbEntity currentEntity, List<RelateUpdateModel> updateModelList) {
        for (RelateUpdateModel model : updateModelList) {
            if (model.getValueType() == 1) {
                targetEntity.put(model.getTargetColCode(), currentEntity.get(model.getUpdateValue()));
            } else if (model.getValueType() == 2) {
                targetEntity.put(model.getTargetColCode(), model.getUpdateValue());
            }
        }
        return targetEntity;
    }

    public DbCollection getOldTargetData(Long tableId, Long identityColumnId, Long currentDataId) {
        DbTable targetTable = registerColumnInfoService.initTableInfoByTableId(tableId);
        String identity_column_code = registerColumnInfoService.getColumnInfoByColumnId(identityColumnId).getCode();
        QueryOption queryOption = new QueryOption(targetTable.getCode());
        queryOption.setTableFilter(new TableFilter(identity_column_code, currentDataId, EOperateMode.Equals));
        queryOption.setQueryMode(2);
        DbCollection oldData = dataAccessQueryCoreService.query(queryOption, false);
        return oldData;
    }
}
