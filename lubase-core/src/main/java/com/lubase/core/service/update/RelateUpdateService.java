package com.lubase.core.service.update;

import com.lubase.core.model.DbCollection;
import com.lubase.core.model.RelateUpdateModel;
import com.lubase.model.DbEntity;

import java.util.List;

public interface RelateUpdateService {


    /**
     * 根据表id获取级联更新规则
     *
     * @param tableId
     * @return
     */
    List<DbEntity> getRelateRuleList(String tableId);

    /**
     * @param entity dm_relate_update entity
     * @return
     */
    List<RelateUpdateModel> getWriteRule(DbEntity entity);


    DbEntity buildTargetEntity(DbEntity targetEntity, DbEntity currentEntity, List<RelateUpdateModel> updateModelList);


    DbCollection getOldTargetData(Long tableId, Long identityColumnId, Long currentDataId);
}
