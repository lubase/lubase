package com.lubase.core.extend.demo;

import com.lubase.model.DbEntity;
import com.lubase.core.entity.SsButtonEntity;
import com.lubase.core.extend.PageJumpDataService;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class DemoPageJumpData implements PageJumpDataService {
    @Override
    public String getId() {
        return "1118565716082036736";
    }

    @Override
    public String getDescription() {
        return "demo：页面跳转从服务端取数";
    }

    @Override
    public DbEntity convertClientData(SsButtonEntity button, HashMap<String, String> hashMap) throws Exception {
        String oInsId = checkAndGetParam("id", hashMap);
        DbEntity fInsEntity = new DbEntity();
        for (String key : hashMap.keySet()) {
            fInsEntity.putWithNoTrace(key, hashMap.get(key));
        }
        fInsEntity.putWithNoTrace(ORIGINALID, oInsId);
        fInsEntity.putWithNoTrace(NAVCODE, button.getNav_address());
        return fInsEntity;
    }
}
