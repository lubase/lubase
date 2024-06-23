package com.lubase.core.extend;

import com.lubase.core.model.SelectUserModel;

import java.util.List;

public interface UserCreateExtendService {
    /**
     * 创建用户
     *
     * @param list
     * @return
     */
    Integer createUser(List<SelectUserModel> list);
}
