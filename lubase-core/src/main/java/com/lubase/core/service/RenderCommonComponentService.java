package com.lubase.core.service;

import com.lubase.core.model.SelectUserModel;
import com.lubase.core.model.UserSelectCollection;

import java.util.List;

/**
 * 常用组件数据源：选人、选部门等
 */
public interface RenderCommonComponentService {

    /**
     * 为弹窗选人提供数据源
     *
     * @param userCode
     * @param userName
     * @param pageIndex
     * @param pageSize
     * @param isSystemUser 是否是系统内部用户
     * @return
     */
    UserSelectCollection selectUserList(String userCode, String userName, Integer pageIndex, Integer pageSize, Boolean isSystemUser);

}
