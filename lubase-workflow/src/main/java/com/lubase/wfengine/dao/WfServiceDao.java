package com.lubase.wfengine.dao;

import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
import com.lubase.wfengine.auto.entity.WfServiceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WfServiceDao {
    @Autowired
    DataAccess dataAccess;

    public WfServiceEntity getWfServiceById(Long id) {
        DbCollection collection = dataAccess.queryById(WfServiceEntity.TABLE_CODE, id);
        List<WfServiceEntity> list = collection.getGenericData(WfServiceEntity.class);
        return list.stream().findFirst().orElse(null);
    }
}
