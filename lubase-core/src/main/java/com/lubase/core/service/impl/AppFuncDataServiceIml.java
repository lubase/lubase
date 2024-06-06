package com.lubase.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.lubase.core.model.ButtonRefFormInfo;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.operate.EOperateMode;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbEntity;
import com.lubase.core.entity.SsButtonEntity;
import com.lubase.core.entity.SsPageEntity;
import com.lubase.core.model.PageGridInfoModel;
import com.lubase.core.service.AppFuncDataService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AppFuncDataServiceIml implements AppFuncDataService {
    @Autowired
    DataAccess dataAccess;

    @Override
    public SsPageEntity getPageById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        Long longId = Long.parseLong(id);
        List<SsPageEntity> list = dataAccess.queryById(SsPageEntity.TABLE_CODE, longId)
                .getGenericData(SsPageEntity.class);
        return list.stream().findFirst().orElse(null);
    }

    @Override
    public String getPageRefTable(SsPageEntity pageEntity) {
        if (pageEntity == null || StringUtils.isEmpty(pageEntity.getGrid_query())) {
            return null;
        }
        QueryOption queryOption;
        try {
            queryOption = JSON.parseObject(pageEntity.getGrid_query(), QueryOption.class);
        } catch (Exception ex) {
            log.warn("页面配置错误，无法获取主表：" + pageEntity.getId());
            return null;
        }
        if (queryOption == null) {
            return null;
        }
        return queryOption.getTableName();
    }

    @Override
    public String getPageCanEditColumn(SsPageEntity pageEntity) {
        if (pageEntity == null || StringUtils.isEmpty(pageEntity.getGrid_info())) {
            return null;
        }
        PageGridInfoModel infoModel;
        try {
            infoModel = JSON.parseObject(pageEntity.getGrid_info(), PageGridInfoModel.class);
        } catch (Exception ex) {
            log.warn("页面配置错误，无法获取可编辑列：" + pageEntity.getId());
            return null;
        }
        if (infoModel == null) {
            return null;
        }
        return infoModel.getEditColumns();
    }

    @Override
    public List<DbEntity> getButtonListByPageId(Long pageId) {
        if (pageId == null) {
            return new ArrayList<>();
        }
        QueryOption queryOption = new QueryOption(SsButtonEntity.TABLE_CODE);
        queryOption.setTableFilter(new TableFilter(SsButtonEntity.COL_PAGE_ID, pageId, EOperateMode.Equals));
        queryOption.setBuildLookupField(false);
        return dataAccess.queryAllData(queryOption).getData();
    }

    @Override
    public String getFormIdByFuncCode(String funcCode) {
        Long btnId = 0L;
        try {
            btnId = Long.parseLong(funcCode);
        } catch (Exception ex) {
            return "";
        }
        DbCollection coll = dataAccess.queryById("ss_button", btnId, "form_id");
        String method = null;
        if (coll.getData().size() == 1) {
            method = TypeConverterUtils.object2String(coll.getData().get(0).get("form_id"), "");
        } else {
            coll = dataAccess.queryById("dm_form_button", btnId, "ref_form_id");
            if (coll.getData().size() == 1) {
                method = TypeConverterUtils.object2String(coll.getData().get(0).get("ref_form_id"), "");
            }
        }
        if (StringUtils.isEmpty(method)) {
            method = "";
        }
        return method;
    }

    @SneakyThrows
    @Override
    public ButtonRefFormInfo getRefFormInfoByFuncCode(String funcCode) {
        Long btnId = 0L;
        try {
            btnId = Long.parseLong(funcCode);
        } catch (Exception ex) {
            return null;
        }
        DbCollection coll = dataAccess.queryById("ss_button", btnId, "form_id");
        String refFormId = null, buttonType = null;
        ButtonRefFormInfo info = new ButtonRefFormInfo();
        if (coll.getData().size() == 1) {
            refFormId = TypeConverterUtils.object2String(coll.getData().get(0).get("form_id"), "");
            buttonType = TypeConverterUtils.object2String(coll.getData().get(0).get("button_type"), "");
            info.setIsFormChildTable(false);
        } else {
            coll = dataAccess.queryById("dm_form_button", btnId, "ref_form_id");
            if (coll.getData().size() == 1) {
                refFormId = TypeConverterUtils.object2String(coll.getData().get(0).get("ref_form_id"), "");
                buttonType = TypeConverterUtils.object2String(coll.getData().get(0).get("button_type"), "");
                info.setIsFormChildTable(true);
            }
        }
        if (StringUtils.isEmpty(refFormId)) {
            throw new WarnCommonException("按钮未找到关联的表单，请联系管理员");
        }
        info.setRefFormId(refFormId);
        info.setButtonType(buttonType);
        return info;
    }

    @Override
    public Boolean checkPageContainsForm(String pageId, String formId) {
        return true;
    }

    @Override
    public SsButtonEntity getFuncInfoByFuncCode(String btnId) {
        DbCollection coll = dataAccess.queryById("ss_button", Long.parseLong(btnId));
        List<SsButtonEntity> btnList = coll.getGenericData(SsButtonEntity.class);
        if (coll.getTotalCount() == 1) {
            return btnList.get(0);
        } else {
            coll = dataAccess.queryById("dm_form_button", Long.parseLong(btnId));
            btnList = coll.getGenericData(SsButtonEntity.class);
            if (coll.getTotalCount() == 1) {
                return btnList.get(0);
            }
        }
        return null;
    }
}
