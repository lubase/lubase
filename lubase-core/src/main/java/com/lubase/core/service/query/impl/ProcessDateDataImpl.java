package com.lubase.core.service.query.impl;

import com.lubase.core.model.EColumnType;
import com.lubase.core.service.query.ProcessCollectionService;
import com.lubase.core.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Order(5)
public class ProcessDateDataImpl implements ProcessCollectionService {
    @Override
    public void processDataList(List<DbEntity> entityList, DbTable tableInfo) {
        for (DbField field : tableInfo.getFieldList()) {
            if (!field.getEleType().equals(EColumnType.Date.getIndex().toString())) {
                continue;
            }
            String dataFormat = field.getDataFormat();
            if (StringUtils.isEmpty(dataFormat)) {
                dataFormat = "yyyy-MM-dd";
            }
            for (DbEntity entity : entityList) {
                if (entity.containsKey(field.getCode())) {
                    entity.put(field.getCode(), TypeConverterUtils.object2LocalDateTime2String(entity.get(field.getCode()), dataFormat));
                }
            }
        }
    }
}
