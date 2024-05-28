package com.lubase.core.service.query.impl;

import com.lubase.core.service.RegisterColumnInfoService;
import com.lubase.core.service.query.DataAccessQueryCoreService;
import com.lubase.core.service.query.ProcessCollectionService;
import com.lubase.core.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Order(4)
public class ProcessRefFileDataImpl implements ProcessCollectionService {
    @Autowired
    DataAccessQueryCoreService dataAccessQueryCoreService;

    @Qualifier("registerColumnInfoServiceApi")
    @Autowired
    RegisterColumnInfoService registerColumnInfoService;

    @Override
    public void processDataList(List<DbEntity> entityList, DbTable tableInfo) {
        for (DbField colInfo : tableInfo.getFieldList()) {
            if (!colInfo.getEleType().equals("8") && !colInfo.getEleType().equals("9")) {
                continue;
            }
            for (DbEntity entity : entityList) {
                if (entity.containsKey(colInfo.getCode())) {
                    String fileKey = String.format("%s,%s", tableInfo.getId(), colInfo.getCode());
                    String dataId = entity.getId().toString();
                    Boolean isParentTable = !colInfo.getTableId().equals(tableInfo.getId());
                    // 如果是视图 则 需要根据表内的值来获取附件数据
                    if (tableInfo.getCode().startsWith("v_")) {
                        fileKey = TypeConverterUtils.object2String(entity.get(colInfo.getCode()), "");
                    } else if (isParentTable) {
                        fileKey = String.format("%s,%s", colInfo.getTableId(), colInfo.getCode().split("#")[1]);
                        String lookupFieldCode = colInfo.getCode().split("#")[0];
                        if (entity.containsKey(lookupFieldCode)) {
                            dataId = entity.get(lookupFieldCode).toString();
                        } else {
                            //如果是关联表的情况下，没有查询关联字段，则无法显示附件信息
                            continue;
                        }
                    }
                    if (fileKey.isBlank()) {
                        continue;
                    }
                    //文件关联字段，从sd_file_relation表检索文件记录
                    String key = String.format("%s_%s", dataId, fileKey);
                    List<DbEntity> files = registerColumnInfoService.getFileDisplayNameByFileKey2(key);
                    if (files.size() > 0) {
                        String fileDisplayName = "";
                        // 暂时用把文件名字的逗号进行替换，兼容以前的数据 20230914
                        for (DbEntity file : files) {
                            fileDisplayName += String.format(",%s|%s", file.get("file_name").toString().replace(",",""), file.getId().toString());
                        }
                        if (fileDisplayName.startsWith(",")) {
                            fileDisplayName = fileDisplayName.substring(1);
                        }
                        entity.setRefData(colInfo.getCode(), fileDisplayName);
                    }
                }
            }
        }
    }
}
