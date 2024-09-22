package com.lubase.orm.extend.service;

import com.lubase.orm.extend.ServerMacroExtend;

import java.util.List;

public interface ServerMacroExtendAdapter {

    /**
     * 根据服务宏名称获取服务宏扩展
     *
     * @param macroName
     * @return
     */
    ServerMacroExtend getServerMacroExtendByName(String macroName);

    /**
     * 获取所有服务宏扩展
     *
     * @return
     */
    List<ServerMacroExtend> getAllServerMacroExtend();

}
