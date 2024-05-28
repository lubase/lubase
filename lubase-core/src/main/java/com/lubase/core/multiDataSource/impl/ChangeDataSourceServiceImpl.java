package com.lubase.core.multiDataSource.impl;

import com.lubase.core.exception.InvokeCommonException;
import com.lubase.core.multiDataSource.ChangeDataSourceService;
import com.lubase.core.service.DataAccess;
import com.lubase.model.DbTable;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ChangeDataSourceServiceImpl implements ChangeDataSourceService {
    @Autowired
    DataAccess dataAccess;

    @SneakyThrows
    @Override
    public void changeDataSourceByTableCode(String tableCode) {
        if (StringUtils.isEmpty(tableCode)) {
            throw new InvokeCommonException("tableCode is not null");
        }
        DbTable table = dataAccess.initTableInfoByTableCode(tableCode);
        if (table == null) {
            throw new InvokeCommonException(String.format("tableCode %s is not exists", tableCode));
        }
        changeDataSourceByTableCode(table);
    }
}
