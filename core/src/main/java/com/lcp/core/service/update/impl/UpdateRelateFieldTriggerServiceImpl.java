package com.lcp.core.service.update.impl;

import com.lcp.core.model.DbCollection;
import com.lcp.core.model.RelateUpdateModel;
import com.lcp.core.service.RegisterColumnInfoService;
import com.lcp.core.service.update.DataAccessUpdateCoreService;
import com.lcp.core.service.update.RelateUpdateService;
import com.lcp.core.service.update.UpdateTriggerService;
import com.lcp.core.util.TypeConverterUtils;
import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.DbTable;
import com.lcp.coremodel.EDBEntityState;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 级联字段处理
 */
@Slf4j
@Component
@Order(2)
public class UpdateRelateFieldTriggerServiceImpl implements UpdateTriggerService {
    @Autowired
    RelateUpdateService relateUpdateService;
    @Qualifier("registerColumnInfoServiceApi")
    @Autowired
    RegisterColumnInfoService registerColumnInfoService;
    @Autowired
    DataAccessUpdateCoreService updateCoreService;

    @SneakyThrows
    @Override
    public void afterUpdate(DbCollection collection, Integer updateRowCount) {
        DbTable table = collection.getTableInfo();
        List<DbEntity> listRelate = relateUpdateService.getRelateRuleList(table.getId());
        for (DbEntity entity : collection.getData()) {
            afterUpdate(table, entity, collection.isServer(), listRelate);
        }
    }

    void afterUpdate(DbTable tableInfo, DbEntity entity, Boolean isServer, List<DbEntity> listRelate) throws Exception {
        for (DbEntity relateEntity : listRelate) {
            int settingState = TypeConverterUtils.object2Integer(relateEntity.get("data_state"));
            if ((entity.getDataState().getIndex() | settingState) != settingState) {
                continue;
            }
            List<RelateUpdateModel> updateModelList = relateUpdateService.getWriteRule(relateEntity);
            if (updateModelList.size() == 0) {
                continue;
            }
            Long targetTableId = Long.parseLong(relateEntity.get("target_table_id").toString());
            if (entity.getDataState().equals(EDBEntityState.Added)) {
                DbCollection targetCollection = getEmptyDataByTableId(targetTableId);
                DbEntity newEntity = targetCollection.newEntity();
                relateUpdateService.buildTargetEntity(newEntity, entity, updateModelList);
                newEntity.setState(EDBEntityState.Added);
                targetCollection.getData().add(newEntity);
                updateCoreService.update(targetCollection);
            } else if (entity.getDataState().equals(EDBEntityState.Modified) || entity.getDataState().equals(EDBEntityState.Deleted)) {
                DbCollection oldData = relateUpdateService.getOldTargetData(targetTableId,
                        Long.parseLong(relateEntity.get("identify_column_id").toString()), entity.getId());
                if (oldData.getData().size() == 1 && entity.getDataState().equals(EDBEntityState.Modified)) {
                    DbEntity oldEntity = oldData.getData().get(0);
                    relateUpdateService.buildTargetEntity(oldEntity, entity, updateModelList);
                    oldEntity.setState(EDBEntityState.Modified);
                    updateCoreService.update(oldData);
                } else if (oldData.getData().size() == 1 && entity.getDataState().equals(EDBEntityState.Deleted)) {
                    oldData.getData().get(0).setState(EDBEntityState.Deleted);
                    updateCoreService.update(oldData);
                } else {
                    log.warn("找到级联数据，更新target表时 未找到原数据,data id is {} ,relate id is {}", entity.getId().toString(), relateEntity.getId());
                }
            }
        }
    }

    DbCollection getEmptyDataByTableId(Long tableId) {
        try {
            DbCollection data = new DbCollection();
            DbTable dbTable = registerColumnInfoService.initTableInfoByTableId(tableId);
            data.setData(new ArrayList<DbEntity>());
            data.setTableInfo(dbTable);
            return data;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
