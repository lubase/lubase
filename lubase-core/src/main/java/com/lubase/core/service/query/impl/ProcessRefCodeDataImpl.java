package com.lubase.core.service.query.impl;

import com.lubase.core.service.RegisterColumnInfoService;
import com.lubase.core.service.query.ProcessCollectionService;
import com.lubase.model.DbCode;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@Order(1)
public class ProcessRefCodeDataImpl implements ProcessCollectionService {

    @Qualifier("registerColumnInfoServiceApi")
    @Autowired
    RegisterColumnInfoService registerColumnInfoService;

    @Override
    public void processDataList(List<DbEntity> entityList, DbTable tableInfo) {
        for (DbField field : tableInfo.getFieldList()) {
            String typeId = field.getCodeTypeId();
            try {
                //TODO:  eletype  应该是枚举类型
                if (!field.getEleType().equals("6") || StringUtils.isEmpty(typeId)) {
                    continue;
                }
                List<DbCode> sscodeList = registerColumnInfoService.getCodeListByTypeId(typeId);
                if (sscodeList == null) {
                    log.warn("获取代码表数据为空：" + field.getId() + field.getCode());
                    continue;
                }
                for (DbEntity entity : entityList) {
                    if (null == entity.get(field.getCode())) {
                        continue;
                    }
                    String codeValue = entity.get(field.getCode()).toString();
                    if (field.getIsMultivalued() == 1) {
                        String disName = "";
                        for (String tmpValue : codeValue.split("\\,")) {
                            for (DbCode codeEntity : sscodeList) {
                                if (tmpValue.equals(codeEntity.getCode())) {
                                    if (disName.equals("")) {
                                        disName += codeEntity.getName();
                                    } else {
                                        disName += "," + codeEntity.getName();
                                    }
                                }
                            }
                        }
                        entity.setRefData(field.getCode(), disName);
                    } else {
                        for (DbCode codeEntity : sscodeList) {
                            if (codeValue.equals(codeEntity.getCode())) {
                                entity.setRefData(field.getCode(), codeEntity.getName());
                                break;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                log.error("代码表转换失败" + field.getId() + field.getCode(), ex);
                log.error(ex.getMessage());
            }
        }
    }
}
