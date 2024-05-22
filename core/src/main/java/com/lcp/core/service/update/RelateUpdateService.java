package com.lcp.core.service.update;

import com.lcp.core.model.DbCollection;
import com.lcp.core.model.RelateUpdateModel;
import com.lcp.coremodel.DbEntity;

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
