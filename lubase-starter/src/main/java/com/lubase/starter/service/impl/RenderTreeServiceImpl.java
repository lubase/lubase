package com.lubase.starter.service.impl;

import com.alibaba.fastjson.JSON;
import com.lubase.core.QueryOption;
import com.lubase.core.model.DbCollection;
import com.lubase.core.service.DataAccess;
import com.lubase.core.util.ServerMacroService;
import com.lubase.model.DbEntity;
import com.lubase.starter.auto.entity.SsPageEntity;
import com.lubase.starter.extend.IGetLeftDataService;
import com.lubase.starter.extend.service.InvokeMethodAdapter;
import com.lubase.starter.service.AppFuncDataService;
import com.lubase.starter.service.RenderBaseService;
import com.lubase.starter.service.RenderTreeService;
import com.lubase.starter.util.ClientMacro;
import com.lubase.starter.util.InvokeDataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class RenderTreeServiceImpl implements RenderBaseService, RenderTreeService {


    @Autowired
    DataAccess dataAccess;

    @Autowired
    InvokeMethodAdapter invokeMethodAdapter;

    @Autowired
    InvokeDataSourceService sqlDataSource;

    @Autowired
    ServerMacroService serverMacroService;

    @Autowired
    AppFuncDataService appFuncDataService;

    @Override
    public List<DbEntity> getTreeDataByPageId(String pageId, ClientMacro clientMacro) {
        List<DbEntity> list;
        if (StringUtils.isEmpty(pageId)) {
            return null;
        }
        SsPageEntity pageEntity = appFuncDataService.getPageById(pageId);
        //todo:此处需要验证是否有页面权限
        if (pageEntity == null || StringUtils.isEmpty(pageEntity.getTree_query())) {
            return null;
        }
        QueryOption serverQuery = JSON.parseObject(pageEntity.getTree_query(), QueryOption.class);
        if (StringUtils.isEmpty(serverQuery.getFixField())) {
            serverQuery.setFixField("*");
        }
        if (serverQuery.getQueryType() == 2) {
            //注意: 只能接收无参数的方法。如果需要客户端传递的参数，请使用queryType=3。详见↓
            list = sqlDataSource.queryListBySql(Long.parseLong(pageId), serverQuery.getServerId());
        } else if (serverQuery.getQueryType() == 3) {
            IGetLeftDataService service = invokeMethodAdapter.getLeftDataService(serverQuery.getServerId());
            list = service.exe(pageId, clientMacro);
        } else {
            replaceClientMacro(serverQuery.getTableFilter(), clientMacro);
            //检索所有数据 0724修改
            DbCollection coll = dataAccess.queryAllData(serverQuery);
            list = coll.getData();
        }
        return list;
    }

    @Override
    public ServerMacroService getServerMacroService() {
        return serverMacroService;
    }
}
