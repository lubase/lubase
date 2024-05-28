package com.lubase.wfengine.node.operator;

import com.lubase.core.exception.WarnCommonException;
import com.lubase.core.extend.IColumnRemoteService;
import com.lubase.core.model.DbCollection;
import com.lubase.model.DbEntity;
import com.lubase.wfengine.auto.entity.WfFInsEntity;
import com.lubase.wfengine.auto.entity.WfOperEntity;
import com.lubase.wfengine.auto.entity.WfTaskEntity;
import com.lubase.wfengine.model.EOperatorType;
import com.lubase.wfengine.model.OperatorUserModel;
import com.lubase.wfengine.node.OperatorService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 指定处理人
 */
@Service
public class AssignUserService implements OperatorService {
    @Override
    public EOperatorType getOperatorType() {
        return EOperatorType.AssignUser;
    }

    @Autowired
    @Qualifier("userColumnServiceImpl")
    IColumnRemoteService remoteServiceById;

    @Autowired
    @Qualifier("userInfoByCodeServiceImpl")
    IColumnRemoteService remoteServiceByCode;

    @SneakyThrows
    @Override
    public List<OperatorUserModel> getUserIdList(WfTaskEntity taskEntity, WfOperEntity operEntity, WfFInsEntity fIns, DbCollection bisData) {
        List<OperatorUserModel> userList = new ArrayList<>();
        OperatorUserModel userModel = new OperatorUserModel();
        //指定处理人支持工号，为了兼容以前的id 暂时保留两处判断逻辑
        DbEntity userEntity = remoteServiceByCode.getCacheDataByKey(operEntity.getOper_value());
        if (userEntity == null) {
            userEntity = remoteServiceById.getCacheDataByKey(operEntity.getOper_value());
            if (userEntity == null) {
                throw new WarnCommonException(String.format("未找到用户%s，请检查流程设置和业务数据", userModel.getUserId()));
            }
            userModel.setUserId(operEntity.getOper_value());
        } else {
            userModel.setUserId(userEntity.getId().toString());
        }
        userModel.setUserName(operEntity.getOper_desc());
        userList.add(userModel);
        return userList;
    }
}
