package com.lcp.qibao.model.customForm;

import com.lcp.coremodel.DbField;
import com.lcp.coremodel.DbTable;
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
