package com.lubase.core.service;

import com.lubase.core.exception.LoginErrorException;
import com.lubase.core.model.LoginInfoModel;
import com.lubase.core.model.NavVO;
import com.lubase.core.model.SelectUserModel;
import com.lubase.core.model.UserInfoModel;
import com.lubase.orm.model.LoginUser;
import org.apache.tomcat.websocket.AuthenticationException;

import java.util.List;

/**
 * @author A
 */
public interface UserInfoService {

    /**
     * 根据用户登录账号获取用户信息
     *
     * @param userCode
     * @return
     */
    UserInfoModel getUserInfo(String userCode);

    /**
     * 根据用户名和密码检索用户信息
     *
     * @param uid
     * @param pwd
     * @return
     */
    LoginInfoModel userLogin(String uid, String pwd, String verifyCode) throws LoginErrorException;

    /**
     * 创建用户
     *
     * @param list
     * @return
     */
    Integer createUser(List<SelectUserModel> list);

    /**
     * 更改密码
     *
     * @param newPwd
     * @return
     */
    Integer modifyUserPwd(String uid, String newPwd, String oldPwd);

    /**
     * 验证密码复杂度
     *
     * @param password 密码
     * @return
     */
    String validatePassword(String password);

    /**
     * 验证token是否合法
     *
     * @param token
     * @return
     */
    LoginUser verifyToken(String token) throws AuthenticationException;

    /**
     * 获取后端管理员菜单列表
     *
     * @return
     */
    List<NavVO> getAdminNavData();

    /**
     * 获取应用配置菜单列表
     *
     * @return
     */
    List<NavVO> getSettingNavData(Long appId);

    /**
     * 获取当前用户的菜单权限
     *
     * @return
     */
    List<NavVO> getAppPreviewNavDataId(Long appId);

    /**
     * 创建用户token
     *
     * @return
     */
    String createUserToken(LoginUser user);
}
