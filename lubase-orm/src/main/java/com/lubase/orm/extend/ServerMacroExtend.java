package com.lubase.orm.extend;

/**
 * 服务端宏扩展接口
 */
public interface ServerMacroExtend {
    /**
     * 宏代码
     *
     * @return
     */
    String macroCode();

    /**
     * 显示名称
     */
    String displayName();

    /**
     * 获取宏变量的值
     *
     * @param appId  当前应用id
     * @param userId
     * @return
     */
    String getValue(Long appId, String userId);
}
