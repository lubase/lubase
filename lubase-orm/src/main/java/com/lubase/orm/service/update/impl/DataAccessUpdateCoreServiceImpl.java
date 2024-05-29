package com.lubase.orm.service.update.impl;

import com.lubase.orm.exception.FieldValueException;
import com.lubase.orm.extend.ITableTrigger;
import com.lubase.orm.extend.service.TableTriggerAdapter;
import com.lubase.orm.mapper.DataAccessMapper;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.model.EColumnType;
import com.lubase.orm.model.SqlEntity;
import com.lubase.orm.service.update.DataAccessUpdateCoreService;
import com.lubase.orm.service.update.GenerateUpdateSql;
import com.lubase.model.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DataAccessUpdateCoreServiceImpl implements DataAccessUpdateCoreService {
    @Autowired
    DataAccessMapper dataAccessMapper;
    @Autowired
    GenerateUpdateSql generateUpdateSql;

    @Autowired
    TableTriggerAdapter tableTriggerAdapter;

    @SneakyThrows
    @Override
    public Integer update(DbCollection collection) {
        if (StringUtils.isEmpty(collection.getTableInfo().getCode()) || collection.getData().size() == 0) {
            return 0;
        }
        //数据检查
        List<ITableTrigger> triggerList = tableTriggerAdapter.getTableTriggerList(collection.getTableInfo());
        Integer count;
        try {
            for (DbEntity entity : collection.getData()) {
                if (entity.getDataState().equals(EDBEntityState.UnChanged)) {
                    continue;
                }
                EDBEntityState state = entity.getDataState();
                //需要触发时，再执行
                if (collection.isEnableTableTrigger()) {
                    for (ITableTrigger t : triggerList) {
                        if ((state.equals(EDBEntityState.Added) && t.isAdd())
                                || (state.equals(EDBEntityState.Modified) && t.isEdit())
                                || (state.equals(EDBEntityState.Deleted) && t.isDelete())) {
                            if (!t.beforeValidate(collection.getTableInfo(), entity, collection.isServer())) {
                                //不保存此记录
                                entity.setState(EDBEntityState.UnChanged);
                                break;
                            }
                        }
                    }
                }
                dataValidate(collection.getTableInfo(), entity, collection.isServer());
            }
            // 执行sql语句
            List<SqlEntity> sqlEntityList = generateUpdateSql(collection);
            count = 0;
            for (SqlEntity sqlEntity : sqlEntityList) {
                count += dataAccessMapper.executeUpdate(sqlEntity);
            }
            if (count > 0) {
                //事后触发器
                for (DbEntity entity : collection.getData()) {
                    EDBEntityState state = entity.getDataState();
                    if (state.equals(EDBEntityState.UnChanged)) {
                        continue;
                    }
                    //需要触发时，再执行
                    if (collection.isEnableTableTrigger()) {
                        for (ITableTrigger t : triggerList) {
                            if ((state.equals(EDBEntityState.Added) && t.isAdd())
                                    || (state.equals(EDBEntityState.Modified) && t.isEdit())
                                    || (state.equals(EDBEntityState.Deleted) && t.isDelete())) {
                                t.afterUpdate(collection.getTableInfo(), entity, collection.isServer());
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
        return count;
    }

    /**
     * 更新前 对数据进行检查
     *
     * @param table
     * @param entity
     * @throws FieldValueException
     */
    void dataValidate(DbTable table, DbEntity entity, Boolean isServer) throws FieldValueException {
        if (entity.getDataState().equals(EDBEntityState.UnChanged)) {
            return;
        }
        if (entity.getDataState().equals(EDBEntityState.Deleted)) {
            if (StringUtils.isEmpty(entity.getId())) {
                throw new FieldValueException("id", "id");
            }
            //删除时，其它值不用判断   20220720 22:54
            return;
        }
        for (DbField f : table.getFieldList()) {
            //1 如果非当前表字段则不判断
            if (!f.getTableCode().equals(table.getCode()) || f.getIsNull() == 1) {
                continue;
            }
            //2 如果是修改，且值没有发生变化无需判断
            if (entity.getDataState().equals(EDBEntityState.Modified) && !entity.isPropertyChanged(f.getCode())) {
                continue;
            }
            // 客户端模式，不可见字段和只读字段不判断必填（表单外字段）
            if (!isServer && f.getFieldAccess().getIndex() < EAccessGrade.NewToWrite.getIndex()) {
                continue;
            }
            //
            Object val = entity.get(f.getCode());
            //3 值不允许为空的需要判断
            if (StringUtils.isEmpty(val)) {
                throw new FieldValueException(f.getCode(), f.getName());
            }
            Integer actualLength = 0;
            if (!StringUtils.isEmpty(val)) {
                actualLength = val.toString().length();
            }
            //4 文本框需要判断输入长度是否越界
            if (f.getEleType().equals(EColumnType.Text.getIndex().toString()) && actualLength > f.getLength()) {
                throw new FieldValueException(f.getCode(), f.getName(), f.getLength(), actualLength);
            }
        }
    }

    List<SqlEntity> generateUpdateSql(DbCollection collection) {
        List<SqlEntity> sqlEntityList = new ArrayList<>();
        SqlEntity sqlEntity = null;
        EDBEntityState lastState = EDBEntityState.UnChanged;
        for (DbEntity entity : collection.getData()) {
            if (entity.getDataState().equals(EDBEntityState.UnChanged)) {
                continue;
            }
            //参数数量大于2000后，新的sql语句
            if (sqlEntity == null || !lastState.equals(entity.getDataState()) || sqlEntity.getParamIndex() > 2000) {
                sqlEntity = new SqlEntity();
                sqlEntityList.add(sqlEntity);
            }
            lastState = entity.getDataState();
            if (entity.getDataState().equals(EDBEntityState.Added)) {
                generateUpdateSql.addSql(entity, collection.getTableInfo(), sqlEntity, collection.isServer());
            } else if (entity.getDataState().equals(EDBEntityState.Modified)) {
                generateUpdateSql.updateSql(entity, collection.getTableInfo(), collection.isServer(), sqlEntity);
            } else if (entity.getDataState().equals(EDBEntityState.Deleted)) {
                generateUpdateSql.deleteSql(entity, collection.getTableInfo(), sqlEntity);
            }
        }
        return sqlEntityList;
    }
}
