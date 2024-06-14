package com.lubase.core.invoke;

import com.lubase.core.extend.IInvokeMethod;
import com.lubase.core.service.RenderCommonComponentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class QueryUserList implements IInvokeMethod {

    @Autowired
    RenderCommonComponentService renderCommonComponentService;

    @Override
    public boolean checkRight() {
        return false;
    }

    @Override
    public String getDescription() {
        return "弹窗选人查询用户列表";
    }

    @Override
    public String getId() {
        return "1179386853791371264";
    }

    @Override
    public Object exe(HashMap<String, String> mapParam) throws Exception {
        String userCode = null, userName = null;
        if (mapParam.containsKey("userCode")) {
            userCode = mapParam.get("userCode");
        }
        if (mapParam.containsKey("userName")) {
            userName = mapParam.get("userName");
        }
        Integer pageSize = Integer.parseInt(checkAndGetParam("pageSize", mapParam));
        Integer pageIndex = Integer.parseInt(checkAndGetParam("pageIndex", mapParam));
        Boolean isSystemUserTag = true;
        // 设定 如果   outerTage=1 则表示从外部获取用户数据，否则都是内部
        if (mapParam.containsKey("outerTag") && mapParam.get("outerTag").equals("1")) {
            isSystemUserTag = false;
        }
        return renderCommonComponentService.selectUserList(userCode, userName, pageIndex, pageSize, isSystemUserTag);
    }
}
