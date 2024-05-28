package com.lubase.wfengine.service.impl;

import com.lubase.core.QueryOption;
import com.lubase.core.TableFilter;
import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
import com.lubase.core.util.TableFilterWrapper;
import com.lubase.model.DbEntity;
import com.lubase.wfengine.model.OperatorUserModel;
import com.lubase.wfengine.service.WFUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class WFUserInfoServiceImpl implements WFUserInfoService {
    @Autowired
    DataAccess dataAccess;

    @Override
    public OperatorUserModel getOperatorUserByUserIdOrCode(String idOrCode) {
        if (StringUtils.isEmpty(idOrCode)) {
            return null;
        }
        QueryOption queryOption = new QueryOption("sa_account");
        TableFilter filter = null;
        if (idOrCode.matches("\\d*")) {
            filter = TableFilterWrapper.or().eq("id", idOrCode).eq("user_code", idOrCode).build();
        } else {
            filter = TableFilterWrapper.or().eq("user_code", idOrCode).build();
        }
        queryOption.setTableFilter(filter);
        queryOption.setFixField("id,user_code,user_name");
        DbCollection collection = dataAccess.queryAllData(queryOption);
        if (collection.getData().size() == 1) {
            OperatorUserModel userModel = new OperatorUserModel();
            DbEntity entity = collection.getData().get(0);
            userModel.setUserId(entity.getId().toString());
            String display = String.format("%s(%s)", entity.get("user_name"), entity.get("user_code"));
            userModel.setUserName(display);
            return userModel;
        }
        return null;
    }
}
