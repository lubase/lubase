package com.lubase.wfengine.service;

import com.lubase.wfengine.model.OperatorUserModel;

public interface WFUserInfoService {
    OperatorUserModel getOperatorUserByUserIdOrCode(String idOrCode);
}
