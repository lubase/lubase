package com.lcp.core.service.query.impl;

import com.lcp.core.model.EColumnType;
import com.lcp.core.service.query.ProcessCollectionService;
import com.lcp.core.util.TypeConverterUtils;
import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.DbField;
import com.lcp.coremodel.DbTable;
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
