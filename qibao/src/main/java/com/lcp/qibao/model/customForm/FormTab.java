package com.lcp.qibao.model.customForm;

import lombok.Data;

import java.util.List;

/**
 * 表单 Tab 数据对象
 *
 * @author A
 */
@Data
public class FormTab {
    private String title;
    private List<FormGroup> groups;
}
