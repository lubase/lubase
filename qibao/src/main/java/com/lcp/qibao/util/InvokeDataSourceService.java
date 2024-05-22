package com.lcp.qibao.util;

import com.alibaba.fastjson.JSON;
import com.lcp.core.QueryOption;
import com.lcp.core.TableFilter;
import com.lcp.core.exception.InvokeCommonException;
import com.lcp.core.exception.WarnCommonException;
import com.lcp.core.mapper.DataAccessMapper;
import com.lcp.core.model.DbCollection;
import com.lcp.core.model.SqlEntity;
import com.lcp.core.multiDataSource.ChangeDataSourceService;
import com.lcp.core.operate.EOperateMode;
import com.lcp.core.service.DataAccess;
import com.lcp.core.util.TypeConverterUtils;
import com.lcp.coremodel.DbEntity;
import com.lcp.qibao.auto.entity.SsInvokeDatasourceEntity;
import com.lcp.qibao.model.EDataSourceResType;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ssdatasource 表的配置的服务
 *
 * @author A
 */
@Service
public class InvokeDataSourceService {

    @Autowired
    DataAccessMapper dataAccessMapper;

    @Autowired
    DataAccess dataAccess;

    @Autowired
    ChangeDataSourceService changeDataSourceService;

    public SsInvokeDatasourceEntity getDataSourceEntity(Long id) {
        List<SsInvokeDatasourceEntity> list = dataAccess.queryById("ss_invoke_datasource", id).getGenericData(SsInvokeDatasourceEntity.class);
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 用于返回左侧树形数据
     *
     * @param pageId
     * @param dataSourceId
     * @return
     */
    public List<DbEntity> queryListBySql(Long pageId, Long dataSourceId) {
        Object o = queryObjectByDataSource(0L, dataSourceId);
        if (o instanceof ArrayList) {
            return (ArrayList<DbEntity>) o;
        } else {
            return null;
        }
    }

    /**
     * 用于返回列表数据
     *
     * @param pageId
     * @param dataSourceId
     * @return
     */
    public DbCollection queryDBCollectionBySql(Long pageId, Long dataSourceId) {
        Object o = queryObjectByDataSource(0L, dataSourceId);
        if (o instanceof DbCollection) {
            return (DbCollection) o;
        } else {
            return null;
        }
    }

    /**
     * 免登录访问方法
     *
     * @param dataSourceId
     * @param appId
     * @param queryParamList
     * @return
     */
    @SneakyThrows
    public Object queryObjectByDataSourceNoRight(Long appId, Long dataSourceId, String... queryParamList) {
        SsInvokeDatasourceEntity datasource = getDataSourceEntity(dataSourceId);
        if (datasource == null) {
            return null;
        }
        Integer checkRight = TypeConverterUtils.object2Integer(datasource.get("check_right"), 1);
        if (checkRight.equals(1)) {
            throw new WarnCommonException("此方法需要登录才能访问，请联系管理员" + dataSourceId);
        }
        return queryObjectByDataSource(appId, dataSourceId, queryParamList);
    }

    /*
     * 用于返回列表数据
     *
     * @param dataSourceId
     * @param queryParamList
     * @return
     */
    @SneakyThrows
    public Object queryObjectByDataSource(Long appId, Long dataSourceId, String... queryParamList) {
        SsInvokeDatasourceEntity datasource = getDataSourceEntity(dataSourceId);
        if (datasource == null) {
            return null;
        }
        if (appId == 0L) {
            throw new InvokeCommonException("appId 参数异常");
        }
        if (queryParamList.length < datasource.getParam_count()) {
            throw new InvokeCommonException("参数数量不正确");
        }
        Object returnObj = null;
        //查询方式 1:sql 2: queryOption
        if (datasource.getQuery_setting_type() == 1) {
            String sqlStr = datasource.getSql_setting();
            SqlEntity sqlEntity = new SqlEntity();
            sqlEntity.setSqlStr(sqlStr);
            for (Integer i = 0; i < datasource.getParam_count(); i++) {
                sqlEntity.addParam(queryParamList[i]);
            }
            sqlEntity.put("appId", appId);
            //执行sql语句前需要切换数据源
            changeDataSourceService.changeDataSourceByTableCode(datasource.getTable_code().toLowerCase());
            // 1:数据对象  2：数据列表 3:DBCollection 4:表结构
            if (datasource.getResponse_type() == EDataSourceResType.Object.getType()) {
                returnObj = dataAccessMapper.executeSqlResDbEntity(sqlEntity);
            } else if (datasource.getResponse_type() == EDataSourceResType.List.getType()) {
                returnObj = dataAccessMapper.executeSqlResList(sqlEntity);
            } else if (datasource.getResponse_type() == EDataSourceResType.DBCollection.getType()) {
                throw new InvokeCommonException("sql模式不支持输出类型3");
            } else if (datasource.getResponse_type() == EDataSourceResType.FieldList.getType()) {
                throw new InvokeCommonException("sql模式不支持输出类型4");
            }
        } else if (datasource.getQuery_setting_type() == 2) {
            //todo:此处需要验证是否有页面权限
            if (StringUtils.isEmpty(datasource.getQuery_setting())) {
                return null;
            }
            QueryOption serverQuery = JSON.parseObject(datasource.getQuery_setting(), QueryOption.class);
            if (datasource.getResponse_type() == EDataSourceResType.FieldList.getType()) {
                //如果获取字段列表则条件一个没有数据的条件
                serverQuery.setTableFilter(new TableFilter("ID", 0, EOperateMode.Equals));
            }
            //替换 filterValue中的参数信息
            HashMap<String, String> clientParams = new HashMap<>();
            // queryoption 中设置的参数 从#{p1} 开始
            for (int i = 0; i < datasource.getParam_count(); i++) {
                clientParams.put(String.format("#{p%s}", i + 1), queryParamList[i]);
            }
            replaceFilter(serverQuery.getTableFilter(), clientParams);
            DbCollection collection = dataAccess.query(serverQuery);
            // 1:数据对象  2：数据列表 3:DBCollection 4:表结构
            if (datasource.getResponse_type() == EDataSourceResType.Object.getType()) {
                if (collection.getData().size() == 1) {
                    return collection.getData().get(0);
                } else {
                    throw new InvokeCommonException("没有查询到数据");
                }
            } else if (datasource.getResponse_type() == EDataSourceResType.List.getType()) {
                return collection.getData();
            } else if (datasource.getResponse_type() == EDataSourceResType.DBCollection.getType()) {
                return collection;
            } else if (datasource.getResponse_type() == EDataSourceResType.FieldList.getType()) {
                return collection.getTableInfo().getFieldList();
            }
            returnObj = null;
        }
        return returnObj;
    }

    /**
     * 替换客户端参数
     *
     * @param tableFilter
     * @param clientParams
     */
    private void replaceFilter(TableFilter tableFilter, HashMap<String, String> clientParams) {
        if (tableFilter.getChildFilters() != null) {
            for (TableFilter child : tableFilter.getChildFilters()) {
                replaceFilter(child, clientParams);
            }
        } else {
            if (tableFilter.getFilterValue() != null) {
                String tmpValue = tableFilter.getFilterValue().toString();
                if (clientParams.containsKey(tmpValue)) {
                    tableFilter.setFilterValue(clientParams.get(tmpValue));
                }
            }
        }
    }
}
