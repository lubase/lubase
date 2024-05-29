package com.lubase.orm.service.query.impl;

import com.lubase.orm.extend.IColumnRemoteService;
import com.lubase.orm.extend.service.ColumnRemoteServiceAdapter;
import com.lubase.orm.service.query.ProcessCollectionService;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Order(3)
public class ProcessRefServiceDataImpl implements ProcessCollectionService {

    @Autowired
    ColumnRemoteServiceAdapter serviceAdapter;

    @Override
    public void processDataList(List<DbEntity> entityList, DbTable tableInfo) {
        for (DbField field : tableInfo.getFieldList()) {
            String serviceName = field.getServiceName();
            if (!field.getEleType().equals("10") || StringUtils.isEmpty(serviceName)) {
                continue;
            }
            IColumnRemoteService service = serviceAdapter.getServiceByName(serviceName);
            if (service == null) {
                continue;
            }
            List<String> listKey = getCollectionKeys(entityList, field);

            List<DbEntity> serviceData = new ArrayList<>();
            for (String key : listKey) {
                DbEntity entity = service.getCacheDataByKey(key);
                if (entity != null) {
                    serviceData.add(entity);
                }
            }
            for (DbEntity entity : entityList) {
                if (StringUtils.isEmpty(entity.get(field.getCode()))) {
                    continue;
                }
                String displayColValue = "";
                String[] keys = entity.get(field.getCode()).toString().split(",");
                String tableKey = service.tableKey();
                for (String key : keys) {
                    DbEntity refEntity = serviceData.stream().filter(d -> d.get(tableKey).toString().equals(key)).findFirst().orElse(null);
                    if (refEntity != null) {
                        displayColValue += "," + refEntity.get(service.displayCol()).toString();
                    }
                }
                if (displayColValue.length() > 0) {
                    entity.setRefData(field.getCode(), displayColValue.substring(1));
                }
            }
        }
    }

    List<String> getCollectionKeys(List<DbEntity> entityList, DbField field) {
        List<String> listKey = new ArrayList<>();
        for (DbEntity entity : entityList) {
            if (StringUtils.isEmpty(entity.get(field.getCode()))) {
                continue;
            }
            String[] keys = entity.get(field.getCode()).toString().split(",");
            for (String key : keys) {
                if (!listKey.contains(key)) {
                    listKey.add(key);
                }
            }
        }
        return listKey;
    }
}
