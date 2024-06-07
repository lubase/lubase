package com.lubase.core.service.button.impl;

import com.alibaba.fastjson.JSON;
import com.lubase.core.service.CustomFormDataService;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.exception.ParameterNotFoundException;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TableFilterWrapper;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.EDBEntityState;
import com.lubase.core.exception.DataNotFoundException;
import com.lubase.core.entity.SsButtonEntity;
import com.lubase.core.model.ButtonServerSettingModel;
import com.lubase.core.service.button.MoreDataService;
import com.lubase.core.service.button.SpecialButtonService;
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
    @Autowired
    CustomFormDataService customFormDataService;
    @Override
    public Object exe(SsButtonEntity button, List<HashMap<String, String>> listMapParam) throws Exception {
        ButtonServerSettingModel serverSettingModel = getMainTableCode(customFormDataService, button);
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
