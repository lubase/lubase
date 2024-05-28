package com.lubase.starter.service.userright.impl;

import com.lubase.core.model.LoginUser;
import com.lubase.core.service.AppHolderService;
import com.lubase.core.service.DataAccess;
import com.lubase.core.service.query.DataAccessColumnRightService;
import com.lubase.model.DbField;
import com.lubase.model.EAccessGrade;
import com.lubase.starter.service.userright.UserRightService;
import com.lubase.starter.service.userright.model.ColumnRightModelVO;
import com.lubase.starter.service.userright.model.UserRightInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 判断字段权限
 */
@Service
public class ColumnAccessRightService implements DataAccessColumnRightService {

    @Autowired
    UserRightService userRightService;
    @Autowired
    AppHolderService appHolderService;
    @Autowired
    DataAccess dataAccess;

    @Override
    public List<DbField> checkAccessRight(List<DbField> fieldList) {
        //判断是否开启了字段权限控制
        Object[] tableIds = fieldList.stream().map(o -> o.getTableId()).distinct().toArray();
        List<String> enableControlledTableIds = new ArrayList<>();
        for (Object tableId : tableIds) {
            if (enableColumnAccessControl(tableId.toString())) {
                enableControlledTableIds.add(String.valueOf(tableId));
            }
        }
        if (enableControlledTableIds.size() == 0) {
            return fieldList;
        }

        List<DbField> newFieldList = new ArrayList<>();
        List<ColumnRightModelVO> colRightList = getUserColRightList();
        for (DbField field : fieldList) {
            //只过滤开启了字段权限控制低表，且可见字段的权限
            if (field.getFieldAccess().getIndex().equals(EAccessGrade.Invisible.getIndex())
                    || !enableControlledTableIds.contains(field.getTableId())) {
                newFieldList.add(field);
                continue;
            }
            ColumnRightModelVO existsRightVo = colRightList.stream().filter(r -> r.getColumnId().toString().equals(field.getId())).findFirst().orElse(null);
            //字段访问权限有只读以上权限才进行数据查询
            if (existsRightVo != null && existsRightVo.getAccessRight() > EAccessGrade.Invisible.getIndex()) {
                field.setRight(existsRightVo.getAccessRight());
                newFieldList.add(field);
            }
        }
        return newFieldList;
    }

    /**
     * 判断表是否开启字段权限控制
     *
     * @param tableId
     * @return
     */
    Boolean enableColumnAccessControl(String tableId) {
        return dataAccess.getControlledTableList().contains(tableId);
    }

    List<ColumnRightModelVO> getUserColRightList() {
        LoginUser user = appHolderService.getUser();
        UserRightInfo rightInfo = userRightService.getUserRight(user.getId());
        return rightInfo.getColRightList();
    }
}
