package com.lubase.model;

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

    private String id;
    private String name;
    private List<DbField> fieldList;

    @JsonIgnore
    private Long databaseId;
    @JsonIgnore
    private String databaseType;
    @JsonIgnore
    private Long appId;
    @JsonIgnore
    private String code;
    @JsonIgnore
    private String tableTypeId;
    @JsonIgnore
    private Integer isView;
    @JsonIgnore
    private String customConfig;


    public DbTable() {
        this.fieldList = new ArrayList<>();
    }

    /**
     * 判断是否为主库，主库的databaseId 值=0
     *
     * @return
     */
    @JsonIgnore
    public boolean isMainDatabase() {
        return this.databaseId != null && this.databaseId.equals(0L);
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
