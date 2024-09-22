package com.lubase.orm.extend.service.impl;

import com.lubase.orm.extend.ExtendAppLoadCompleteService;
import com.lubase.orm.extend.ServerMacroExtend;
import com.lubase.orm.extend.service.ServerMacroExtendAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ServerMacroExtendAdapterImpl implements ServerMacroExtendAdapter, ExtendAppLoadCompleteService {

    private List<ServerMacroExtend> serverMacroExtendList;

    @Override
    public ServerMacroExtend getServerMacroExtendByName(String macroName) {
        for (ServerMacroExtend serverMacroExtend : serverMacroExtendList) {
            if (  serverMacroExtend.macroCode().equals(macroName)) {
                return serverMacroExtend;
            }
        }
        return null;
    }

    @Override
    public List<ServerMacroExtend> getAllServerMacroExtend() {
        return serverMacroExtendList;
    }

    @Override
    public void LoadCompleted(ApplicationContext applicationContext) {
        if (serverMacroExtendList == null) {
            serverMacroExtendList = new ArrayList<>();
            Map<String, ServerMacroExtend> triggerMap = applicationContext.getBeansOfType(ServerMacroExtend.class);
            for (String key : triggerMap.keySet()) {
                serverMacroExtendList.add(triggerMap.get(key));
            }
        }
    }

    @Override
    public void clearData() {
        serverMacroExtendList = null;
    }
}
