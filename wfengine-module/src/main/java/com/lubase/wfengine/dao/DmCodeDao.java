package com.lubase.wfengine.dao;

import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.service.DataAccess;
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
