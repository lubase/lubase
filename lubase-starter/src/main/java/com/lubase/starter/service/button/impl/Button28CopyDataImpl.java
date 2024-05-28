package com.lubase.starter.service.button.impl;

import com.lubase.core.QueryOption;
import com.lubase.core.exception.ParameterNotFoundException;
import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
import com.lubase.core.service.IDGenerator;
import com.lubase.core.util.TableFilterWrapper;
import com.lubase.model.DbEntity;
import com.lubase.model.DbField;
import com.lubase.model.EDBEntityState;
import com.lubase.starter.exception.DataNotFoundException;
import com.lubase.starter.auto.entity.SsButtonEntity;
import com.lubase.starter.service.button.OndDataService;
import com.lubase.starter.service.button.SpecialButtonService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;

@Service
public class Button28CopyDataImpl implements OndDataService, SpecialButtonService {

    @Autowired
    DataAccess dataAccess;
    @Autowired
    IDGenerator idGenerator;

    @Override
    public String getButtonType() {
        return "28";
    }

    @Override
    public String getDescription() {
        return "复制";
    }

    @SneakyThrows
    @Override
    public Object exe(SsButtonEntity button, HashMap<String, String> mapParam) {
        String mainTableCode = getMainTableCode(button).getMainTableCode();
        Long id = 0L;
        if (mapParam.containsKey(idHandle)) {
            id = Long.valueOf(mapParam.get(idHandle));
        }
        if (StringUtils.isEmpty(id)) {
            throw new ParameterNotFoundException(idHandle);
        }
        DbCollection collection = dataAccess.queryById(mainTableCode, id, "*");
        if (collection.getTotalCount() == 0) {
            throw new DataNotFoundException(mainTableCode, id);
        }
        Long nextId = idGenerator.nextId();
        for (DbField dbfield : collection.tableInfo.getFieldList()) {
            if (dbfield.getEleType().equals("8") || dbfield.getEleType().equals("9")) {
                //复制是否需要复制图片
                QueryOption queryOption = new QueryOption("sd_file_relation", 0, 0);
                TableFilterWrapper wrapper = new TableFilterWrapper(true);
                wrapper.eq("data_column_tag", collection.getData().get(0).get(dbfield.getCode())).eq("data_id", id);
                queryOption.setTableFilter(wrapper.build());
                queryOption.setFixField("*");
                DbCollection codeCollection = dataAccess.query(queryOption);
                if (codeCollection.getTotalCount() > 0) {
                    for (DbEntity entity:codeCollection.getData()) {
                        //新关联关系表主键
                        Long nextIdTmp = idGenerator.nextId();
                        entity.setId(nextIdTmp);
                        //对应关系发生改变
                        entity.put("data_id", nextId);
                        //data_column_tag，file_info_id不变
                        //codeCollection.getData().get(0).put("file_info_id", nextIdTmp);
                        entity.setState(EDBEntityState.Added);
                    }
                    dataAccess.update(codeCollection);
                }
            } else if (dbfield.getEleType().equals("1") && dbfield.getEleDisType().equals("10")) {
                collection.getData().get(0).put(dbfield.getCode(), collection.getData().get(0).get(dbfield.getCode()) + "复制");
            }
        }

        collection.getData().get(0).setId(nextId);
        collection.getData().get(0).setState(EDBEntityState.Added);
        dataAccess.update(collection);
        return nextId;
    }

}
