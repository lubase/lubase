package com.lubase.starter.model.customForm;

import com.lubase.model.DbField;
import com.lubase.model.DbTable;
import lombok.Data;

import java.util.List;

@Data
public class FormAvailableColumnModel {
    /**
     * 当前主表字段
     */
    private List<DbField> mainTableFieldList;
    /**
     * 父表字段
     */
    private List<DbTable> parentTable;
}
