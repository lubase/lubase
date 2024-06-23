package com.lubase.core.invoke;

import com.lubase.core.extend.IInvokeMethod;
import com.lubase.core.model.SelectUserModel;
import com.lubase.core.service.UserInfoService;
import com.lubase.orm.exception.WarnCommonException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddUserAndOrg implements IInvokeMethod {
    @Autowired
    UserInfoService userInfoService;

    @Override
    public Object exe(HashMap<String, String> mapParam) throws Exception {
        return null;
    }

    @Override
    public boolean checkRight() {
        return false;
    }

    @Override
    public Object exe(List<HashMap<String, String>> listMapParam) throws Exception {
        List<SelectUserModel> userList = getUserList(listMapParam);
        if (userList.isEmpty()) {
            throw new WarnCommonException("未选择数据或者参数传递错误");
        }
        return userInfoService.createUser(userList);
    }

    @SneakyThrows
    private List<SelectUserModel> getUserList(List<HashMap<String, String>> listMapParam) {
        List<SelectUserModel> userList = new ArrayList<>();
        if (!listMapParam.isEmpty()) {
            for (HashMap<String, String> map : listMapParam) {
                SelectUserModel user = new SelectUserModel();
                user.setId(map.get("id"));
                user.setUserCode(map.get("userCode"));
                user.setUserName(map.get("userName"));
                user.setDeptId(map.get("deptId"));
                user.setDeptName(map.get("deptName"));
                // 判断属性不能为空或者空字符串
                if (user.getId() == null || user.getId().isEmpty() || user.getUserCode() == null || user.getUserCode().isEmpty() || user.getUserName() == null || user.getUserName().isEmpty() || user.getDeptId() == null || user.getDeptId().isEmpty() || user.getDeptName() == null || user.getDeptName().isEmpty()) {
                    throw new WarnCommonException("参数传递错误");
                }
                if (userList.stream().anyMatch(x -> x.getId().equals(user.getId()))) {
                    continue;
                }
                userList.add(user);
            }
        }
        return userList;
    }

    @Override
    public String getDescription() {
        return "外部用户列表，选中后增加用户数据";
    }

    @Override
    public String getId() {
        return "698533841412820992";
    }
}
