package com.lubase.wfengine.node.operator;

import com.lubase.core.exception.WarnCommonException;
import com.lubase.core.model.DbCollection;
import com.lubase.wfengine.auto.entity.WfFInsEntity;
import com.lubase.wfengine.auto.entity.WfOperEntity;
import com.lubase.wfengine.auto.entity.WfTaskEntity;
import com.lubase.wfengine.dao.WfOperatorDao;
import com.lubase.wfengine.model.EOperatorType;
import com.lubase.wfengine.model.OperatorUserModel;
import com.lubase.wfengine.node.OperatorService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 指定处理人
 */
@Service
public class OtherNodeUserService implements OperatorService {
    @Override
    public EOperatorType getOperatorType() {
        return EOperatorType.OtherNode;
    }

    @Autowired
    WfOperatorDao operatorDao;

    @SneakyThrows
    @Override
    public List<OperatorUserModel> getUserIdList(WfTaskEntity taskEntity, WfOperEntity operEntity, WfFInsEntity fIns, DbCollection collBisData) {
        String operator = operEntity.getOper_value();
        if (StringUtils.isEmpty(operator)) {
            throw new WarnCommonException("处理者设置错误，请联系管理员");
        }
        return operatorDao.getNodeValidProcessUserList(fIns.getId().toString(), operator);
    }
}
