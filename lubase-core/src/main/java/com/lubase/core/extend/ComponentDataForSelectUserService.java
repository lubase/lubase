package com.lubase.core.extend;

import com.lubase.core.model.SelectUserModel;

import java.util.List;

/**
 * 选人组件所需的外部数据源
 */
public interface ComponentDataForSelectUserService {

    /***
     * 用于弹窗选人等服务
     * @param userCode
     * @param userName
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<SelectUserModel> selectUserList(String userCode, String userName, Integer pageIndex, Integer pageSize);

    /**
     * 是否启用selectUserList方法扩展
     *
     * @return
     */
    default Boolean enableSelectUserList() {
        return false;
    }
}
