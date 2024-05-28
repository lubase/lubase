package com.lubase.core.service.query;

import com.lubase.core.model.LookupMode;
import com.lubase.core.model.QueryJoinCondition;
import com.lubase.model.DbField;
import com.lubase.model.DbTable;

import java.util.List;
import java.util.Map;

public interface LookupFieldParseService {
    DbField parseLookupField(String colCode, DbTable mainTableInfo, Map<String, LookupMode> refFields, List<QueryJoinCondition> joinTables);

    String SqlObj(String databaseType, String sqlObj);
}
