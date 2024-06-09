package com.lubase.core.extend.tabletrigger;

import com.lubase.model.DbEntity;
import com.lubase.model.DbTable;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.extend.ITableTrigger;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.operate.EOperateMode;
import com.lubase.orm.service.AppHolderService;
import com.lubase.orm.service.DataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DataControlForCreateByUser implements ITableTrigger {


    @Autowired
    AppHolderService appHolderService;

    @Autowired
    DataAccess dataAccess;

    @Override
    public String getTriggerTableCode() {
        return "*";
    }

    @Override
    public String getTriggerName() {
        return "根据create_by控制编辑和删除权限，只有create_by才允许对数据操作";
    }

    @Override
    public Boolean isDelete() {
        return true;
    }

    @Override
    public Boolean isEdit() {
        return true;
    }

    @Override
    public Boolean GlobalTriggerFilter(DbTable tableInfo) {
        //  配置的第3位表示是否启用此触发器逻辑
        String customConfig = tableInfo.getCustomConfig();
        if (StringUtils.isEmpty(customConfig)) {
            return false;
        }
        char[] charArray = customConfig.toCharArray();
        return charArray.length >= 2 && charArray[2] == '1';
    }

    @Override
    public Boolean beforeValidate(DbTable tableInfo, DbEntity entity, Boolean isServer) throws Exception {
        // 根据create_by判断是否允许编辑和删除
        String createBy = getCreateBy(tableInfo.getCode(), entity.getId());
        if (!StringUtils.isEmpty(createBy) && createBy.equals(appHolderService.getUser().getCode())) {
            return true;
        } else {
            throw new WarnCommonException("此表开启数据权限（create_by）控制，您无权操作当前数据");
        }
    }

    private String getCreateBy(String tableCode, Long dataId) {
        QueryOption queryOption = new QueryOption(tableCode);
        queryOption.setFixField("create_by");
        queryOption.setTableFilter(new TableFilter("id", dataId, EOperateMode.Equals));
        DbCollection collection = dataAccess.query(queryOption);
        if (collection.getData().size() == 0) {
            return null;
        }
        return collection.getData().get(0).get("create_by").toString();
    }
}
