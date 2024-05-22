package com.lcp.core.service.query.impl;

import com.lcp.core.QueryOption;
import com.lcp.core.model.DbCollection;
import com.lcp.core.model.LookupMode;
import com.lcp.core.service.query.DataAccessQueryCoreService;
import com.lcp.core.service.query.ProcessCollectionService;
import com.lcp.core.util.QueryOptionWrapper;
import com.lcp.core.util.TableFilterWrapper;
import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.DbField;
import com.lcp.coremodel.DbTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

@Service
@Order(2)
public class ProcessRefLookupDataImpl implements ProcessCollectionService {

    @Autowired
    DataAccessQueryCoreService dataAccessQueryCoreService;

    @Override
    public void processDataList(List<DbEntity> entityList, DbTable tableInfo) {
        for (DbField colInfo : tableInfo.getFieldList()) {
            LookupMode colLookup = LookupMode.FromJsonStr(colInfo.getLookup());
            if (!colInfo.getEleType().equals("7") ||
                    colLookup == null ||
                    colLookup.getTableKey() == colLookup.getDisplayCol()) //关联字段与显示字段一致
            {
                continue;
            }
            if (colInfo.getIsMultivalued() == 1) {
                processMultivaluedRefLookupData(entityList, colInfo, colLookup);
            } else {
                for (DbEntity entity : entityList) {
                    String displayCol = colInfo.getCode() + "name";
                    String displayCol2 = colInfo.getCode() + "name2";
                    String displayColValue = "";
                    //TODO 关联字段 as的xxNAME字段，检索出来后变成小写name。暂未定位问题  20220710
                    if (entity.containsKey(colInfo.getCode()) && entity.containsKey(displayCol)) {
                        if (entity.get(displayCol) != null) {
                            displayColValue = entity.get(displayCol).toString();
                            if (entity.get(displayCol2) != null) {
                                displayColValue = String.format("%s(%s)", displayColValue, entity.get(displayCol2).toString());
                            }
                        }
                        //判断是否有扩展字段
                        entity.setRefData(colInfo.getCode(), displayColValue);
                        entity.remove(displayCol);
                        entity.remove(displayCol2);
                    }
                }
            }
        }
    }

    private void processMultivaluedRefLookupData(List<DbEntity> entityList, DbField colInfo, LookupMode colLookup) {
        String tableKey = colLookup.getTableKey();
        String displayCol = colLookup.getDisplayCol();
        String extendCol = colLookup.getExtendCol();
        String queryFields = String.format("%s,%s", tableKey, displayCol);
        if (!StringUtils.isEmpty(extendCol)) {
            queryFields = String.format("%s,%s", queryFields, extendCol);
        }
        TableFilterWrapper filterWrapper = TableFilterWrapper.or();
        boolean hasValue = false;
        for (DbEntity entity : entityList) {
            Object val = entity.get(colInfo.getCode());
            if (StringUtils.isEmpty(val)) {
                continue;
            }
            for (String tmpChildVal : val.toString().split("\\,")) {
                filterWrapper.eq(tableKey, tmpChildVal);
                hasValue = true;
            }
        }
        if (!hasValue) {
            return;
        }
        QueryOption queryOption = QueryOptionWrapper.select(queryFields).from(colLookup.getTableCode()).where(filterWrapper.build()).build();
        queryOption.setQueryMode(2);
        queryOption.setBuildLookupField(false);
        DbCollection refCollection = dataAccessQueryCoreService.query(queryOption, false);
        HashMap<String, String> valueMap = new HashMap<>();
        for (DbEntity entity : refCollection.getData()) {
            String val = "";
            String key = entity.get(tableKey).toString();
            if (!valueMap.containsKey(key)) {
                if (entity.get(displayCol) != null) {
                    val = entity.get(displayCol).toString();
                }
                if (entity.containsKey(extendCol) && entity.get(extendCol) != null) {
                    val = String.format("%s(%s)", val, entity.get(extendCol).toString());
                }
                valueMap.put(key, val);
            }
        }
        //赋值
        for (DbEntity entity : entityList) {
            Object val = entity.get(colInfo.getCode());
            if (StringUtils.isEmpty(val)) {
                continue;
            }
            String refVale = "";
            for (String tmpChildVal : val.toString().split("\\,")) {
                if (StringUtils.isEmpty(refVale)) {
                    refVale = valueMap.get(tmpChildVal);
                } else {
                    refVale += "," + valueMap.get(tmpChildVal);
                }
            }
            entity.setRefData(colInfo.getCode(), refVale);
        }
    }
}
