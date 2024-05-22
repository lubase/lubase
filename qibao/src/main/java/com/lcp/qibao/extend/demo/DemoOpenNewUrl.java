package com.lcp.qibao.extend.demo;

import com.lcp.qibao.extend.IInvokeMethod;

import java.util.HashMap;

public class DemoOpenNewUrl implements IInvokeMethod {
    @Override
    public String getDescription() {
        return "demo：按钮打开新链接";
    }

    @Override
    public String getId() {
        return "1118565182482681235";
    }

    @Override
    public Object exe(HashMap<String, String> mapParam) throws Exception {
        return "https://www.baidu.com";
    }
}
