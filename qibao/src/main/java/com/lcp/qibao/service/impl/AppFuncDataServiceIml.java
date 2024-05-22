package com.lcp.qibao.service.impl;

import com.alibaba.fastjson.JSON;
import com.lcp.core.QueryOption;
import com.lcp.core.TableFilter;
import com.lcp.core.model.DbCollection;
import com.lcp.core.operate.EOperateMode;
import com.lcp.core.service.DataAccess;
import com.lcp.core.util.TypeConverterUtils;
import com.lcp.coremodel.DbEntity;
import com.lcp.qibao.auto.entity.SsButtonEntity;
import com.lcp.qibao.auto.entity.SsPageEntity;
import com.lcp.qibao.model.PageGridInfoModel;
import com.lcp.qibao.service.AppFuncDataService;
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
