package com.lcp.core;

import com.lcp.core.model.LookupMode;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询对象
 *
 * @author A
 */
@Data
public class QueryOption {
    public QueryOption() {
        this.queryMode = 1;
        this.queryType = 1;
        this.pageSize = 10;
        this.pageIndex = 1;
        this.fixField = "*";
        this.tableFilter = new TableFilter();
        this.sortFieldsList = new ArrayList<>();
        this.buildLookupField = true;
        this.enableColAccessControl = false;
    }

    public QueryOption(String tableName) {
        this();
        this.tableName = tableName;
        this.refFields = new HashMap<>();
    }

    public QueryOption(String tableName, int pageIndex, int pageSize) {
        this(tableName);
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    /**
     * dm_table 表中主键id
     */
    private Long tableId;
    /**
     * 数控中表代码，其实应该叫TableCode，名字起的不规范
     */
    private String tableName;
    /**
     * dataSource ID 或者 invokeMethodid
     */
    private Long serverId;
    /**
     * 1:基本查询 2：DataSource查询 3:invokeMethod查询
     */
    private int queryType;
    /**
     * 1:分页查询  2：全部数据
     */
    private int queryMode;
    private int pageSize;
    private int pageIndex;
    private TableFilter tableFilter;
    private String fixField;
    /**
     * 设置排序，例如：  orderid asc|  suuser.orderid asc,suuser.age desc
     */
    private String sortField;
    private Map<String, LookupMode> refFields;
    private List<String> sortFieldsList;
    private boolean buildLookupField;

    /**
     * 是否开启列权限控制。默认值：false
     */
    private boolean enableColAccessControl;

    /**
     * 区分查询场景，用于场景下自定义字段方案设置
     */
    private String queryScene;

    /**
     * 此方法的优先级比 sortField高
     *
     * @param field
     * @return
     */
    public List<String> addAscSort(String field) {
        return addSort(field, "asc");
    }

    /**
     * 此方法的优先级比 sortField高
     *
     * @param field
     * @return
     */
    public List<String> addDescSort(String field) {
        return addSort(field, "desc");
    }

    private List<String> addSort(String field, String sortTypeStr) {
        if (StringUtils.isEmpty(field) || StringUtils.isEmpty(tableName)) {
            throw new NullPointerException("field or tableName is not null");
        }
        if (field.indexOf(".") == -1) {
            sortFieldsList.add(String.format("%s.%s %s", this.tableName, field, sortTypeStr));
        } else {
            sortFieldsList.add(String.format("%s %s", field, sortTypeStr));
        }
        return sortFieldsList;
    }
}
