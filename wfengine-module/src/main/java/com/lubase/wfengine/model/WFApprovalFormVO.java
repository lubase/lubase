package com.lubase.wfengine.model;

import com.lubase.orm.model.DbCollection;
import com.lubase.core.model.CustomFormVO;
import lombok.Data;

import java.util.List;

/**
 * 待办详情数据对象
 */
@Data
public class WFApprovalFormVO {
    public WFApprovalFormVO() {
        this.allowSubmit = false;
        this.allowSave = false;
        this.readonly = false;
    }

    /**
     * 是否允许提交
     */
    private Boolean allowSubmit;
    /**
     * 是否允许保存
     */
    private Boolean allowSave;
    /**
     * 是否只读。如果只读 则禁用备注区域、隐藏提交、保存按钮
     */
    private Boolean readonly;
    /**
     * 审批命令列表
     */
    private List<WFCmdModel> cmdList;
    /**
     * 表单列表
     */
    private CustomFormVO customForm;
    /**
     * 流程节点字段设置信息
     */
    private List<WfTaskFieldModel> taskFieldModelList;
    /**
     * 嵌套页面配置
     */
    private CustomPageSetting customPage;
    /**
     * 审批历史
     */
    private DbCollection approvalList;
    /**
     * 评分汇总
     */
    private DbCollection ratingList;

    /**
     * 处理意见，如果此前保存过处理意见，则需要渲染到页面
     */
    private String processMemo;

    /**
     * 评分表单
     */
    private CustomFormVO ratingForm;
}
