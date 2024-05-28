package com.lubase.starter.extend.demo;

import com.alibaba.fastjson.JSON;
import com.lubase.starter.extend.IInvokeMethod;

import java.util.HashMap;
import java.util.List;

public class DemoButtonInvoke implements IInvokeMethod {
    @Override
    public String getDescription() {
        return "demo：按钮invoke方法";
    }

    @Override
    public String getId() {
        return "1118565182482681856";
    }

    @Override
    public Object exe(HashMap<String, String> mapParam) throws Exception {
        return "服务端收到一条消息，请刷新列表。" + JSON.toJSONString(mapParam);
    }

    @Override
    public Object exe(List<HashMap<String, String>> listMapParam) throws Exception {
        return "服务端收到多条消息，请刷新列表。" + JSON.toJSONString(listMapParam);
    }
}
