package com.lubase.core.service.userright.impl;

import com.lubase.core.constant.CacheRightConstant;
import com.lubase.core.service.userright.RoleRightService;
import com.lubase.core.service.userright.model.ColumnRightModelVO;
import com.lubase.core.service.userright.model.RoleModel;
import com.lubase.model.DbEntity;
import com.lubase.orm.QueryOption;
import com.lubase.orm.TableFilter;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.operate.EOperateMode;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.util.TypeConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleRightServiceImpl implements RoleRightService {
    @Autowired
    DataAccess dataAccess;

    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_ROLE_INFO + "+#roleId")
    @Override
    public RoleModel getRoleInfoById(Long roleId) {
        DbCollection collection = dataAccess.queryById("sa_role", roleId, "app_id,role_type");
        RoleModel roleModel = null;
        if (collection.getData().size() == 1) {
            roleModel = new RoleModel();
            roleModel.setId(roleId);
            roleModel.setAppId(TypeConverterUtils.object2Long(collection.getData().get(0).get("app_id")));
        }
        return roleModel;
    }


    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_ROLE_FUNC + "+#roleId")
    @Override
    public List<Long> getRoleFuncList(Long roleId) {
        QueryOption queryOption = new QueryOption("sa_func_assign");
        queryOption.setFixField("func_id");
        queryOption.setTableFilter(new TableFilter("role_id", roleId, EOperateMode.Equals));
        List<DbEntity> roleFuncList = dataAccess.queryAllData(queryOption).getData();
        List<Long> list = new ArrayList<>();
        for (DbEntity entity : roleFuncList) {
            list.add(TypeConverterUtils.object2Long(entity.get("func_id")));
        }
        return list;
    }

    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_ROLE_COLUMN + "+#roleId")
    @Override
    public List<ColumnRightModelVO> getRoleColList(Long roleId) {
        List<ColumnRightModelVO> voList = new ArrayList<>();
        QueryOption queryOption = new QueryOption("sa_column_assign");
        queryOption.setBuildLookupField(false);
        queryOption.setFixField("id,table_id,column_id,access_right");
        queryOption.setTableFilter(new TableFilter("role_id", roleId, EOperateMode.Equals));
        DbCollection collColRight = dataAccess.queryAllData(queryOption);

        for (DbEntity rightEntity : collColRight.getData()) {
            ColumnRightModelVO vo = new ColumnRightModelVO();
            vo.setTableId(TypeConverterUtils.object2Long(rightEntity.get("table_id")));
            vo.setColumnId(TypeConverterUtils.object2Long(rightEntity.get("column_id")));
            vo.setAccessRight(TypeConverterUtils.object2Integer(rightEntity.get("access_right")));
            voList.add(vo);
        }
        return voList;
    }

}
