package com.lubase.wfengine.dao;

import com.lubase.core.QueryOption;
import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DmCodeDao {

    @Autowired
    DataAccess dataAccess;

    public int getCount() {
        QueryOption queryOption = new QueryOption("dm_code");
        DbCollection coll = dataAccess.queryAllData(queryOption);
        return coll.getTotalCount();
    }
}
