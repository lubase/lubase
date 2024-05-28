package com.lubase.starter.service.button.impl;

import com.lubase.core.exception.InvokeCommonException;
import com.lubase.core.exception.ParameterNotFoundException;
import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.EDBEntityState;
import com.lubase.starter.exception.DataNotFoundException;
import com.lubase.starter.auto.entity.SsButtonEntity;
import com.lubase.starter.model.ButtonServerSettingModel;
import com.lubase.starter.service.button.OndDataService;
import com.lubase.starter.service.button.SpecialButtonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * 删除单行数据
 *
 * @author A
 */
@Service
public class Button26DeleteImpl implements OndDataService, SpecialButtonService {
    @Override
    public String getButtonType() {
        return "26";
    }

    @Override
    public String getDescription() {
        return "删除单行数据";
    }

    @Autowired
    DataAccess dataAccess;


    @Override
    public Object exe(SsButtonEntity button, HashMap<String, String> mapParam) throws Exception {
        ButtonServerSettingModel serverSettingModel = getMainTableCode(button);
        String mainTableCode = serverSettingModel.getMainTableCode();
        Boolean isLogicDelete = serverSettingModel.getIsLogicDelete();

        if (mapParam == null) {
            throw new InvokeCommonException("参数不正确，请检查");
        }
        Long id = 0L;
        if (mapParam.containsKey(idHandle)) {
            id = Long.valueOf(mapParam.get(idHandle));
        }
        if (StringUtils.isEmpty(id)) {
            throw new ParameterNotFoundException(idHandle);
        }
        DbCollection collection = dataAccess.queryById(mainTableCode, id, isLogicDelete ? logicDeleteHandle : idHandle);
        if (collection.getTotalCount() == 0) {
            throw new DataNotFoundException(mainTableCode, id);
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
