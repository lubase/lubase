package com.lcp.qibao.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * 客户端宏变量
 *
 * @author A
 */
public class ClientMacro extends HashMap<String, String> {
    /**
     * 客户端宏变量前缀
     */
    public static final String clientMacroPre = "@@C.";
    /**
     * 服务端宏变量前缀
     */
    public static final String serverMacroPre = "@@S.";

    /**
     * 根据客户端传递宏变量信息初始化宏变量对象
     *
     * @param clientMacroStr
     * @return
     */
    public static ClientMacro init(String clientMacroStr) {
        ClientMacro clientMacro = new ClientMacro();
        if (StringUtils.isEmpty(clientMacroStr)) {
            return clientMacro;
        }
        JSONObject object = (JSONObject) JSON.parse(clientMacroStr);
        for (String key : object.keySet()) {
            clientMacro.put(clientMacroPre + key, object.get(key).toString());
        }
        return clientMacro;
    }

    private String getClientMacroByKey(String name) {
        String key = String.format("%s%s", clientMacroPre, name);
        if (super.containsKey(key)) {
            return super.get(key).toString();
        } else {
            return "";
        }
    }

    /**
     * 以下为具体的已经支持的宏变量*********************************
     * /**
     * 左树模板中，在选择节点后，需要对此属性赋值。获取树节点数据对象中 “keyField” 属性值
     *
     * @return
     */
    public String getTreeKey() {
        return getClientMacroByKey("treeKey");
    }

    /**
     * 左树模板中，在选择节点后，需要对此属性赋值。获取树节点数据对象中 “displayFields” 属性值,如果displayFields设置多个，取第一个
     *
     * @return
     */
    public String getTreeName() {
        return getClientMacroByKey("treeName");
    }

    public Long getAppId() {
        String appIdStr = getClientMacroByKey("appId");
        Long appId = -1L;
        try {
            appId = Long.parseLong(appIdStr);
        } catch (Exception ex) {
           //TODO: 需要记录日志
        }
        return appId;
    }
}
