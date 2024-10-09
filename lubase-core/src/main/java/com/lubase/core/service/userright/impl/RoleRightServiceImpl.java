package com.lubase.core.service.userright.impl;

import com.lubase.core.constant.CacheRightConstant;
import com.lubase.core.service.userright.RoleRightService;
import com.lubase.core.service.userright.mapper.UserRightMapper;
import com.lubase.core.service.userright.model.ColumnRightModelVO;
import com.lubase.core.service.userright.model.RoleModel;
import com.lubase.model.DbEntity;
import com.lubase.orm.multiDataSource.DBContextHolder;
import com.lubase.orm.util.TypeConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleRightServiceImpl implements RoleRightService {
    @Autowired
    UserRightMapper userRightMapper;

    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_ROLE_INFO + "+#roleId")
    @Override
    public RoleModel getRoleInfoById(Long roleId) {
        RoleModel roleModel = null;
        DBContextHolder.setMainDataSourceCode();
        DbEntity entity = userRightMapper.getRoleInfoById(roleId);
        if (entity != null) {
            roleModel = new RoleModel();
            roleModel.setId(roleId);
            roleModel.setAppId(TypeConverterUtils.object2Long(entity.get("app_id")));
        }
        return roleModel;
    }

    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_ROLE_FUNC + "+#roleId")
    @Override
    public List<Long> getRoleFuncList(Long roleId) {
        if (roleId == null) {
            return new ArrayList<>();
        }
        DBContextHolder.setMainDataSourceCode();
        return userRightMapper.getRoleFuncList(roleId);
    }

    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_ROLE_COLUMN + "+#roleId")
    @Override
    public List<ColumnRightModelVO> getRoleColList(Long roleId) {
        List<ColumnRightModelVO> voList = new ArrayList<>();
        if (roleId == null) {
            return voList;
        }
        DBContextHolder.setMainDataSourceCode();
        List<DbEntity> list = userRightMapper.getRoleColList(roleId);
        for (DbEntity rightEntity : list) {
            ColumnRightModelVO vo = new ColumnRightModelVO();
            vo.setTableId(TypeConverterUtils.object2Long(rightEntity.get("table_id")));
            vo.setColumnId(TypeConverterUtils.object2Long(rightEntity.get("column_id")));
            vo.setAccessRight(TypeConverterUtils.object2Integer(rightEntity.get("access_right")));
            voList.add(vo);
        }
        return voList;
    }

}
