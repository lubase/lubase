package com.lubase.core.extend;

import com.lubase.core.model.SelectUserModel;

import java.util.List;

/**
 * 用户信息扩展
 */
public interface UserInfoExtendService {

    /**
     * 根据用户名密码进行登录
     *
     * @param userId
     * @param password
     */
    void userLogin(String userId, String password);

    /**
     * @param userCode
     * @param userName
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<SelectUserModel> selectUserList(String userCode, String userName, Integer pageIndex, Integer pageSize);
}
