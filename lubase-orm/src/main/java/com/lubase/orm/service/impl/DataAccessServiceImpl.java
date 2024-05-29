package com.lubase.orm.service.impl;

import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.exception.WarnCommonException;
import com.lubase.orm.extend.ITableTrigger;
import com.lubase.orm.extend.service.TableTriggerAdapter;
import com.lubase.orm.mapper.ProcMSSqlMapper;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.multiDataSource.DBContextHolder;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.service.RegisterColumnInfoService;
import com.lubase.orm.service.query.DataAccessQueryCoreService;
import com.lubase.orm.service.query.ProcessCollectionService;
import com.lubase.orm.service.update.DataAccessUpdateCoreService;
import com.lubase.orm.service.update.UpdateTriggerService;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author A
 */
@Component
@Slf4j
public class DataAccessServiceImpl implements DataAccess {
    @Qualifier("registerColumnInfoServiceApi")
    @Autowired
    RegisterColumnInfoService registerColumnInfoService;

    @Autowired
    DataAccessQueryCoreService dataAccessQueryCoreService;

    @Autowired
    DataAccessUpdateCoreService dataAccessUpdateCoreService;

    @Autowired
    List<ProcessCollectionService> processCollectionServices;
    @Autowired
    List<UpdateTriggerService> updateTriggerServices;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    ProcMSSqlMapper procMSSqlMapper;
    @Autowired
    TableTriggerAdapter tableTriggerAdapter;


    /**
     * 标砖的query查询，默认分页
     *
     * @param queryOption 返回数据和表结构信息
     * @return
     */
    @Override
    public DbCollection query(QueryOption queryOption) {
        return query(queryOption, false);
    }

    /**
     * 不分页查询所有数据
     *
     * @param queryOption
     * @return
     */
    @Override
    public DbCollection queryAllData(QueryOption queryOption) {
        queryOption.setQueryMode(2);
        return query(queryOption, false);
    }

    /**
     * 只查询表结构
     *
     * @param queryOption
     * @return
     */
    @Override
    public DbCollection queryFieldList(QueryOption queryOption) {
        return query(queryOption, true);
    }

    /**
     * @param queryOption
     * @param onlyQueryFieldList 是否只查询表结构信息
     * @return
     */
    @SneakyThrows
    private DbCollection query(QueryOption queryOption, Boolean onlyQueryFieldList) {
        DbCollection collection = dataAccessQueryCoreService.query(queryOption, onlyQueryFieldList);
        if (onlyQueryFieldList) {
            //如果只查询表结构直接返回，无需处理数据
            return collection;
        }
        //处理特殊的数据。例如：代码字段翻译、多选下拉、服务字段等
        List<DbEntity> dataList = collection.getData();
        for (ProcessCollectionService service : processCollectionServices) {
            service.processDataList(dataList, collection.getTableInfo());
        }
        for (DbEntity entity : dataList) {
            entity.acceptChange();
        }
        collection.setData(dataList);
        return collection;
    }

    /**
     * <p>
     * 根据表名称，ID获取记录
     * </p>
     *
     * @param tableName 表名称
     * @param id        记录id
     * @return DbCollection  数据状态是Unchanged
     * @since 2022/4/3 0003
     */
    @Override
    @SneakyThrows
    public DbCollection queryById(String tableName, Long id) {
        return queryById(tableName, id, "*");
    }

    @Override
    @SneakyThrows
    public DbCollection queryById(String tableName, Long id, String fixField) {
        if (StringUtils.isEmpty(tableName) || StringUtils.isEmpty(id)) {
            throw new InvokeCommonException("tableName and id is not null ");
        }
        QueryOption queryOption = new QueryOption(tableName, 0, 0);
        queryOption.setFixField(fixField);
        queryOption.setTableFilter(new TableFilter("ID", id.toString()));
        return query(queryOption);
    }


    @Override
    public DbCollection getEmptyData(String tableName) {
        //memo: 此方法无需从切换数据源，因为获取表结构方法已经走了缓存  221010 ss
        try {
            DbCollection data = new DbCollection();
            DbTable dbTable = this.initTableInfoByTableCode(tableName);
            data.setData(new ArrayList<DbEntity>());
            data.setTableInfo(dbTable);
            return data;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public DbCollection getEmptyDataByTableId(Long tableId) {
        try {
            DbCollection data = new DbCollection();
            //memo: 此方法无需从切换数据源，因为获取表结构方法已经走了缓存  221010 ss
            DbTable dbTable = registerColumnInfoService.initTableInfoByTableId(tableId);
            data.setData(new ArrayList<DbEntity>());
            data.setTableInfo(dbTable);
            return data;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public DbTable initTableInfoByTableCode(String tableCode) {
        return registerColumnInfoService.initTableInfoByTableCode(tableCode);
    }

    @Override
    public DbTable initTableInfoByTableId(Long tableId) {
        return registerColumnInfoService.initTableInfoByTableId(tableId);
    }

    @Override
    public DbField getDbFieldByColumnId(Long columnId) {
        return registerColumnInfoService.getColumnInfoByColumnId(columnId);
    }

    /**
     * 数据更新
     *
     * @param collection
     * @return
     */
    @Override
    @SneakyThrows
    public Integer update(DbCollection collection) {
        if (StringUtils.isEmpty(collection.getTableInfo().getCode()) || collection.getData().size() == 0) {
            return 0;
        }
        // 如果是新增数据则补齐tableInfo 信息。修改和删除是否需要补齐字段后续再议  0828 ss
        if (collection.getData().stream().filter(d -> d.getDataState().equals(EDBEntityState.Added)).count() > 0) {
            List<DbField> allFieldList = registerColumnInfoService.getColumnsByTableId(TypeConverterUtils.object2Long(collection.getTableInfo().getId()));
            for (DbField field : allFieldList) {
                if (collection.getTableInfo().getFieldList().stream().filter(f -> f.getCode().equals(field.getCode())).count() == 0) {
                    field.setVisible(0);
                    collection.getTableInfo().getFieldList().add(field);
                }
            }
        } else if (collection.getData().stream().filter(d -> d.getDataState().equals(EDBEntityState.Modified)).count() > 0) {
            //如果是更新，则补全update_by和update_time 字段，确保记录更新信息
            List<DbField> allFieldList = registerColumnInfoService.getColumnsByTableId(TypeConverterUtils.object2Long(collection.getTableInfo().getId()));
            for (DbField field : allFieldList) {
                if (field.getCode().equals("update_by") || field.getCode().equals("update_time")) {
                    if (collection.getTableInfo().getFieldList().stream().filter(f -> f.getCode().equals(field.getCode())).count() == 0) {
                        field.setVisible(0);
                        collection.getTableInfo().getFieldList().add(field);
                    }
                }
            }
        }
        Integer updateRowCount = 0;
        //事务开启前->校验
        beforeTransactionValidate(collection);
        // 重要：在启动事务之前确定好数据源
        if (collection.getTableInfo().isMainDatabase()) {
            if (DBContextHolder.getDataSourceCode() != null) {
                DBContextHolder.setMainDataSourceCode();
            }
        } else {
            if (!collection.getTableInfo().getDatabaseId().toString().equals(DBContextHolder.getDataSourceCode())) {
                DBContextHolder.setDataSourceCode(collection.getTableInfo().getDatabaseId().toString());
            }
        }
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(definition);
        try {

            for (UpdateTriggerService updateTriggerService : updateTriggerServices) {
                try {
                    updateTriggerService.beforeUpdate(collection);
                } catch (Exception ex) {
                    log.error(String.format("UpdateTriggerService执行错误：%s", updateTriggerService.getClass()), ex);
                    throw ex;
                }
            }
            updateRowCount = dataAccessUpdateCoreService.update(collection);
            for (UpdateTriggerService updateTriggerService : updateTriggerServices) {
                try {
                    updateTriggerService.afterUpdate(collection, updateRowCount);
                } catch (Exception ex) {
                    log.error(String.format("UpdateTriggerService执行错误：%s", updateTriggerService.getClass()), ex);
                    throw ex;
                }
            }
            transactionManager.commit(transactionStatus);
            //事务后数据处理
            afterTransactionValidate(collection);
        } catch (Exception ex) {
            log.error("udpate方法执行失败：", ex);
            if (!transactionStatus.isCompleted()) {
                transactionManager.rollback(transactionStatus);
            }
            // 此处异常抛出不能省略，否则多个 update 手动封装到一个事务不会在异常时回滚
            throw ex;
        }
        return updateRowCount;
    }

    @Override
    public List<String> getControlledTableList() {
        return registerColumnInfoService.getControlledTableList();
    }

    @Override
    public List<DbEntity> procGetDbEntityList(String procName, String... p1) {
        return procMSSqlMapper.getDbEntityList(procName, p1);
    }

    @Override
    public List<String> procGetStringList(String procName, String... p1) {
        return procMSSqlMapper.getStringList(procName, p1);
    }

    @Override
    public List<DbCode> getCodeListByTypeId(String codeTypeId) {
        if(StringUtils.isEmpty(codeTypeId)){
            return new ArrayList<>();
        }
        return registerColumnInfoService.getCodeListByTypeId(codeTypeId);
    }

    private void beforeTransactionValidate(DbCollection collection) throws Exception {
        for (DbEntity entity : collection.getData()) {
            if (entity.getDataState().equals(EDBEntityState.UnChanged)) {
                continue;
            }
            if (collection.isEnableTableTrigger()) {
                EDBEntityState state = entity.getDataState();
                List<ITableTrigger> triggerList = tableTriggerAdapter.getTableTriggerList(collection.getTableInfo());
                try {
                    for (ITableTrigger t : triggerList) {
                        if ((state.equals(EDBEntityState.Added) && t.isAdd())
                                || (state.equals(EDBEntityState.Modified) && t.isEdit())
                                || (state.equals(EDBEntityState.Deleted) && t.isDelete())) {
                            if (!t.beforeTransactionValidate(collection.getTableInfo(), entity, collection.isServer())) {
                                //返回false,抛出异常
                                throw new WarnCommonException("事务前校验失败");
                            }
                        }
                    }
                } catch (Exception ex) {
                    log.error("udpate方法执行失败：", ex);
                    throw ex;
                }
            }
        }
    }

    private void afterTransactionValidate(DbCollection collection) throws Exception {
        for (DbEntity entity : collection.getData()) {
            if (entity.getDataState().equals(EDBEntityState.UnChanged)) {
                continue;
            }
            if (collection.isEnableTableTrigger()) {
                EDBEntityState state = entity.getDataState();
                List<ITableTrigger> triggerList = tableTriggerAdapter.getTableTriggerList(collection.getTableInfo());
                try {
                    for (ITableTrigger t : triggerList) {
                        if ((state.equals(EDBEntityState.Added) && t.isAdd())
                                || (state.equals(EDBEntityState.Modified) && t.isEdit())
                                || (state.equals(EDBEntityState.Deleted) && t.isDelete())) {
                            t.afterTransactionUpdate(collection.getTableInfo(), entity, collection.isServer());
                        }
                    }
                } catch (Exception ex) {
                    log.error("事务后udpate方法执行失败：", ex);
                    throw ex;
                }
            }
        }
    }
}
