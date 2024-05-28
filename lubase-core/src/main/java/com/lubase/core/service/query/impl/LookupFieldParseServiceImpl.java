package com.lubase.core.service.query.impl;

import com.alibaba.fastjson.JSON;
import com.lubase.core.constant.CommonConst;
import com.lubase.core.model.EColumnType;
import com.lubase.core.model.EDatabaseType;
import com.lubase.core.model.LookupMode;
import com.lubase.core.model.QueryJoinCondition;
import com.lubase.core.service.RegisterColumnInfoService;
import com.lubase.core.service.query.LookupFieldParseService;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class LookupFieldParseServiceImpl implements LookupFieldParseService {
    @Qualifier("registerColumnInfoServiceApi")
    @Autowired
    RegisterColumnInfoService registerColumnInfoService;

    @Override
    public DbField parseLookupField(String colCode, DbTable mainTableInfo, Map<String, LookupMode> refFields, List<QueryJoinCondition> joinTables) {
        String[] lookColNames = colCode.split("\\.");
        String leftMainTable = "";
        String lookupAlias = "";
        for (int i = 0; i < lookColNames.length; i++) {
            String lookColName = lookColNames[i];
            if (i < lookColNames.length - 1) {
                lookupAlias = getTableAlias(lookColNames, i);
                DbField lookCol = mainTableInfo.firstOrDefault((field) -> field.getCode().equals(lookColName));
                // 手工为列设置lookup关联信息
                if (refFields != null && refFields.containsKey(lookupAlias)) {
                    lookCol.setEleType(EColumnType.Lookup.getIndex().toString());
                    lookCol.setLookup(JSON.toJSONString(refFields.get(lookColName)));
                }
                LookupMode colLookup = LookupMode.FromJsonStr(lookCol.getLookup());
                //只有lookup字段才可以进行级联查询，否则就只能退出
                if (!lookCol.getEleType().equals(EColumnType.Lookup.getIndex().toString()) || colLookup == null || StringUtils.isEmpty(colLookup.getTableCode())) {
                    //异常出口
                    return null;
                }
                //设关联的左侧主表，第一次循环的字段为主表字段
                if (i == 0) {
                    leftMainTable = lookCol.getTableCode();
                }
                DbTable lookTableInfo = registerColumnInfoService.initTableInfoByTableCode(colLookup.getTableCode());

                ////设置tablefilter中的关联的表，可以优化点：如果fixfield中已经关联表，则此处可以不关联 0711
                String joinTable = SqlObj(mainTableInfo.getDatabaseType(), colLookup.getTableCode()) + " AS " + SqlObj(mainTableInfo.getDatabaseType(), lookupAlias);
                boolean containsJoinTable = false;
                for (QueryJoinCondition queryJoinCondition : joinTables) {
                    if (queryJoinCondition.getTableAlias().equals(joinTable)) {
                        containsJoinTable = true;
                        break;
                    }
                }
                if (!containsJoinTable) {
                    // this.GenerateTableFilter(lookCol.LookupMode.TableFilter, lookTableInfo, lookupAlias);
                    // 关联字段默认tableKey为主键，且关联表不再设置条件，所以不再解析上面的tableFilter，简化设计
                    QueryJoinCondition queryJoinCondition = new QueryJoinCondition();
                    queryJoinCondition.setTableAlias(joinTable);
                    queryJoinCondition.setCondition(String.format("%s.%s = %s.%s", SqlObj(mainTableInfo.getDatabaseType(), leftMainTable), SqlObj(mainTableInfo.getDatabaseType(), lookCol.getCode()), SqlObj(mainTableInfo.getDatabaseType(), lookupAlias)
                            , SqlObj(mainTableInfo.getDatabaseType(), colLookup.getTableKey())));
                    joinTables.add(queryJoinCondition);
                }
                leftMainTable = lookupAlias;
                mainTableInfo = lookTableInfo;
            } else {
                DbField colInfo = mainTableInfo.firstOrDefault(field -> field.getCode().equals(lookColName));
                if (colInfo != null) {
                    //表名设置成别名
                    colInfo.setTableCode(lookupAlias);
                    return colInfo;
                }
            }
        }
        return null;
    }

    /**
     * 获取表的别名
     *
     * @param tmpCodes
     * @param index
     * @return
     */
    private String getTableAlias(String[] tmpCodes, int index) {
        String tableAlias = "";
        for (int i = 0; i < tmpCodes.length; i++) {
            if (i > index) {
                break;
            }
            tableAlias += CommonConst.REF_FIELD_SEPARATOR + tmpCodes[i];
        }
        if (tableAlias.startsWith(CommonConst.REF_FIELD_SEPARATOR)) {
            tableAlias = tableAlias.substring(1);
        }
        return tableAlias;
    }

    @Override
    public String SqlObj(String databaseType, String sqlObj) {
        if (EDatabaseType.Sqlserver.getType().equals(databaseType)) {
            return String.format("\"%s\"", sqlObj);
        } else if (EDatabaseType.Mysql.getType().equals(databaseType)) {
            return String.format("`%s`", sqlObj);
        } else {
            return sqlObj;
        }
    }

}
