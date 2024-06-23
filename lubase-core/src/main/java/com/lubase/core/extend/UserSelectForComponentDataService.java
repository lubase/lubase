package com.lubase.core.extend;

import com.lubase.core.model.SelectUserModel;
import com.lubase.core.model.UserSelectCollection;

import java.util.List;

/**
 * 选人组件所需的外部数据源
 */
public interface UserSelectForComponentDataService {

    /***
     * 用于弹窗选人等服务
     * @param userCode 用户登录账号，不为空则根据此属性检索
     * @param userName 用户姓名，不为空则根据此属性检索
     * @param pageIndex 分页，设置大小
     * @param pageSize 页面数量
     * @return
     */
    UserSelectCollection selectUserList(String userCode, String userName, Integer pageIndex, Integer pageSize);
}
