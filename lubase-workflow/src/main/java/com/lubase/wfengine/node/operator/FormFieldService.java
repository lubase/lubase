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
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class FormFieldService implements OperatorService {

    @Autowired
    @Qualifier("userColumnServiceImpl")
    IColumnRemoteService columnRemoteService;

    @Override
    public EOperatorType getOperatorType() {
        return EOperatorType.FormField;
    }

    @SneakyThrows
    @Override
    public List<OperatorUserModel> getUserIdList(WfTaskEntity taskEntity, WfOperEntity operEntity, WfFInsEntity fIns, DbCollection collBisData) {
        List<OperatorUserModel> userList = new ArrayList<>();
        DbEntity bisData = collBisData.getData().get(0);
        String operator = operEntity.getOper_value();
        if (StringUtils.isEmpty(operator)) {
            throw new WarnCommonException("处理者设置错误，请联系管理员");
        }
        if (!bisData.containsKey(operator) || StringUtils.isEmpty(bisData.get(operator).toString())) {
            throw new WarnCommonException(String.format("在业务数据中未找到字段 %s 的值", operEntity.getOper_value()));
        }
        for (String userId : bisData.get(operator).toString().split(",")) {
            OperatorUserModel userModel = new OperatorUserModel();
            userModel.setUserId(userId);
            DbEntity userEntity = columnRemoteService.getCacheDataByKey(userId);
            if (userEntity == null) {
                throw new WarnCommonException(String.format("未找到用户%s，请检查流程设置和业务数据", userId));
            }
            userModel.setUserName(userEntity.get(columnRemoteService.displayCol()).toString());
            userList.add(userModel);
        }
        return userList;
    }
}
