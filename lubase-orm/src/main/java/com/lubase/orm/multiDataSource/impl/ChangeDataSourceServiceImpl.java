package com.lubase.orm.multiDataSource.impl;

import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.multiDataSource.ChangeDataSourceService;
import com.lubase.orm.service.DataAccess;
import com.lubase.model.DbTable;
import com.lubase.orm.service.RegisterColumnInfoService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ChangeDataSourceServiceImpl implements ChangeDataSourceService {
    @Qualifier("registerColumnInfoServiceApi")
    @Autowired
    RegisterColumnInfoService registerColumnInfoService;

    @SneakyThrows
    @Override
    public void changeDataSourceByTableCode(String tableCode) {
        if (StringUtils.isEmpty(tableCode)) {
            throw new InvokeCommonException("tableCode is not null");
        }
        DbTable table = registerColumnInfoService.initTableInfoByTableCode(tableCode);
        if (table == null) {
            throw new InvokeCommonException(String.format("tableCode %s is not exists", tableCode));
        }
        changeDataSourceByTableCode(table);
    }
}
