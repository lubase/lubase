package com.lubase.wfengine.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WFTaskSettingModel {
    public WFTaskSettingModel() {
        this.fieldListAccessRight = new ArrayList<>();
    }

    /**
     * 字段列表的访问权限
     */
    private List<WfTaskFieldModel> fieldListAccessRight;
}
