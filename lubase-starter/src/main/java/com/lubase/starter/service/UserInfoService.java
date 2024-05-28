package com.lubase.starter.service;

import com.lubase.core.model.LoginUser;
import com.lubase.starter.exception.LoginErrorException;
import com.lubase.starter.model.NavVO;
import org.apache.tomcat.websocket.AuthenticationException;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author A
 */
public interface UserInfoService {
    /**
     * 根据用户名和密码检索用户信息
     *
     * @param uid
     * @param pwd
     * @return
     */
    LoginUser getUser(String uid, String pwd, String verifyCode) throws LoginErrorException;

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
     * 获取token
     *
     * @param user
     * @return
     */
    String createUserToken(LoginUser user);

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


    int calcErrorCount(String userCode);

    void clearCacheErrorCount(String userCode);

    BufferedImage getVerifyCode(String userCode);
}
