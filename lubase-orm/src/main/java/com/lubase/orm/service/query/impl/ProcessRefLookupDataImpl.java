package com.lubase.orm.service.query.impl;

import com.lubase.orm.QueryOption;
import com.lubase.orm.extend.IColumnRemoteService;
import com.lubase.orm.extend.service.ColumnRemoteServiceAdapter;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.EColumnType;
import com.lubase.orm.model.LookupMode;
import com.lubase.orm.service.RegisterColumnInfoService;
import com.lubase.orm.service.query.DataAccessQueryCoreService;
import com.lubase.orm.service.query.ProcessCollectionService;
import com.lubase.orm.util.QueryOptionWrapper;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@Order(2)
public class ProcessRefLookupDataImpl implements ProcessCollectionService {

    @Autowired
    DataAccessQueryCoreService dataAccessQueryCoreService;
    @Qualifier("registerColumnInfoServiceApi")
    @Autowired
    RegisterColumnInfoService registerColumnInfoService;

    @Autowired
    ColumnRemoteServiceAdapter serviceAdapter;

    @Override
    public void processDataList(List<DbEntity> entityList, DbTable tableInfo) {
        for (DbField colInfo : tableInfo.getFieldList()) {
            LookupMode colLookup = LookupMode.FromJsonStr(colInfo.getLookup());
            if (!colInfo.getEleType().equals("7") ||
                    colLookup == null || colLookup.getTableCode() == null ||
                    Objects.equals(colLookup.getTableKey(), colLookup.getDisplayCol())) //关联字段与显示字段一致
            {
                continue;
            }

            if (colInfo.getIsMultivalued() == 1) {
                processMultivaluedRefLookupData(entityList, colInfo, colLookup);
            } else {
                DbTable refTable = registerColumnInfoService.initTableInfoByTableCode(colLookup.getTableCode());
                DbField refDisplayCol = refTable.getFieldList().stream().filter(f -> f.getCode().equals(colLookup.getDisplayCol())).findFirst().orElse(null);
                IColumnRemoteService service = null;
                if (refDisplayCol != null && !StringUtils.isEmpty(refDisplayCol.getServiceName())) {
                    service = serviceAdapter.getServiceByName(refDisplayCol.getServiceName());
                }
                boolean isRefService = refDisplayCol != null && service != null && refDisplayCol.getEleType().equals(EColumnType.RemoteServiceColumn.getStringValue());
                for (DbEntity entity : entityList) {
                    String displayCol = colInfo.getCode() + "name";
                    // 如果关联数据表的显示列是关联数据表，则取name2的属性
                    String displayCol2 = colInfo.getCode() + "name2";
                    String displayColValue = "";
                    //TODO 关联字段 as的xxNAME字段，检索出来后变成小写name。暂未定位问题  20220710
                    if (entity.containsKey(colInfo.getCode()) && entity.containsKey(displayCol)) {
                        if (entity.get(displayCol) != null) {
                            // 如果关联字段是关联服务列，则自动获取服务列显示名称；否则如果是关联数据表列，则直接赋值；否则直接赋值；
                            // 判断关联显示列是否是关联服务列或者关联数据表列
                            displayColValue = entity.get(displayCol).toString();
                            if (isRefService) {
                                DbEntity refEntity = service.getCacheDataByKey(displayColValue);
                                if (refEntity != null && refEntity.containsKey(service.displayCol())) {
                                    displayColValue = refEntity.get(service.displayCol()).toString();
                                }
                            } else if (entity.get(displayCol2) != null) {
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
