package com.lubase.core.invoke;

import com.alibaba.fastjson.JSON;
import com.lubase.core.extend.IInvokeMethod;
import com.lubase.core.model.SelectUserModel;
import com.lubase.core.service.UserInfoService;
import com.lubase.orm.exception.WarnCommonException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
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
                SelectUserModel user = null;
                try {
                    user = JSON.parseObject(JSON.toJSONString(map), SelectUserModel.class);
                } catch (Exception exception) {
                    log.warn("参数传递错误", exception);
                }
                // 判断属性不能为空或者空字符串
                if (user == null || StringUtils.isEmpty(user.getId())
                        || StringUtils.isEmpty(user.getUserCode())
                        || StringUtils.isEmpty(user.getUserName())
                        || StringUtils.isEmpty(user.getDeptId())
                        || StringUtils.isEmpty(user.getDeptName())) {
                    throw new WarnCommonException("参数传递错误");
                }
                String userId = user.getId();
                if (userList.stream().anyMatch(x -> x.getId().equals(userId))) {
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
