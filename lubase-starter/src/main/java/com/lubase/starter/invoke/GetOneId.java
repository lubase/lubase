package com.lubase.starter.invoke;

import com.lubase.core.service.IDGenerator;
import com.lubase.starter.extend.IInvokeMethod;
import com.lubase.starter.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class GetOneId implements IInvokeMethod {
    @Autowired
    IDGenerator idGenerator;

    @Override
    public String getDescription() {
        return "获取一个id";
    }

    @Override
    public String getId() {
        return "813099213847007232";
    }

    @Override
    public Object exe(HashMap<String, String> mapParam) throws Exception {
        Long id = idGenerator.nextId();
        return ResponseData.success(id.toString());
    }
}
