package com.lcp.qibao.service.button.impl;

import com.alibaba.fastjson.JSON;
import com.lcp.core.QueryOption;
import com.lcp.core.TableFilter;
import com.lcp.core.exception.InvokeCommonException;
import com.lcp.core.exception.ParameterNotFoundException;
import com.lcp.core.model.DbCollection;
import com.lcp.core.service.DataAccess;
import com.lcp.core.util.TableFilterWrapper;
import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.DbField;
import com.lcp.coremodel.EDBEntityState;
import com.lcp.qibao.auto.entity.SsButtonEntity;
import com.lcp.qibao.exception.DataNotFoundException;
import com.lcp.qibao.model.ButtonServerSettingModel;
import com.lcp.qibao.service.button.MoreDataService;
import com.lcp.qibao.service.button.SpecialButtonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 删除多行数据
 *
 * @author A
 */
@Service
public class Button27DeleteImpl implements MoreDataService, SpecialButtonService {
    @Override
    public String getButtonType() {
        return "27";
    }

    @Override
    public String getDescription() {
        return "删除多行数据";
    }

    @Autowired
    DataAccess dataAccess;

    @Override
    public Object exe(SsButtonEntity button, List<HashMap<String, String>> listMapParam) throws Exception {
        ButtonServerSettingModel serverSettingModel = getMainTableCode(button);
        String mainTableCode = serverSettingModel.getMainTableCode();
        Boolean isLogicDelete = serverSettingModel.getIsLogicDelete();
        if (listMapParam == null) {
            throw new InvokeCommonException("参数不正确，请检查");
        }
        TableFilterWrapper filterWrapper = TableFilterWrapper.or();
        for (HashMap<String, String> map : listMapParam) {
            if (map.containsKey(idHandle)) {
                filterWrapper.eq(idHandle, map.get(idHandle));
            }
        }
        TableFilter filter = filterWrapper.build();
        if (filter == null) {
            throw new ParameterNotFoundException(idHandle);
        }
        QueryOption queryOption = new QueryOption(mainTableCode);
        queryOption.setFixField(isLogicDelete ? logicDeleteHandle : idHandle);
        queryOption.setTableFilter(filter);
        DbCollection collection = dataAccess.queryAllData(queryOption);
        if (collection.getTotalCount() == 0) {
            throw new DataNotFoundException(mainTableCode, Long.valueOf(JSON.toJSONString(listMapParam)));
        }
        if (isLogicDelete) {
            for (DbEntity entity : collection.getData()) {
                entity.put(logicDeleteHandle, 1);
            }
            for (DbField f : collection.getTableInfo().getFieldList()) {
                if (f.getCode().equals(logicDeleteHandle)) {
                    f.setVisible(4);
                    f.setAccessRight(4);
                    break;
                }
            }
        } else {
            for (DbEntity entity : collection.getData()) {
                entity.setState(EDBEntityState.Deleted);
            }
        }
        return dataAccess.update(collection);
    }

}
