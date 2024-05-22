package com.lcp.coremodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author A
 */
@Data
public class DbTable implements Serializable {
    private static final long serialVersionUID = 5584605329383028458L;
    /**
     * 主库的ID
     */
    @JsonIgnore
    private final Long mainDatabaseId = 0L;
    private String id;
    /**
     * 表所属的数据库Id
     */
    @JsonIgnore
    private Long databaseId;
    @JsonIgnore
    private Long appId;
    @JsonIgnore
    private String code;
    @JsonIgnore
    private String databaseType;
    /**
     * 表中文名字
     */
    private String name;
    private List<DbField> fieldList;

    /**
     * 判断是否为主库，主库的databaseId 值=0
     *
     * @return
     */
    @JsonIgnore
    public boolean isMainDatabase() {
        return this.databaseId != null && this.databaseId.equals(mainDatabaseId);
    }

    @JsonIgnore
    private String customConfig;

    public DbTable() {
        this.fieldList = new ArrayList<>();
    }

    public DbField firstOrDefault(IFilterField iFilterField) {
        for (DbField field : fieldList) {
            if (iFilterField.filter((field))) {
                return field;
            }
        }
        return null;
    }
}
