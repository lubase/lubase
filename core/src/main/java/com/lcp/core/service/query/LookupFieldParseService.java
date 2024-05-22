package com.lcp.core.service.query;

import com.lcp.core.model.LookupMode;
import com.lcp.core.model.QueryJoinCondition;
import com.lcp.coremodel.DbField;
import com.lcp.coremodel.DbTable;

import java.util.List;
import java.util.Map;

public interface LookupFieldParseService {
    DbField parseLookupField(String colCode, DbTable mainTableInfo, Map<String, LookupMode> refFields, List<QueryJoinCondition> joinTables);

    String SqlObj(String databaseType, String sqlObj);
}
