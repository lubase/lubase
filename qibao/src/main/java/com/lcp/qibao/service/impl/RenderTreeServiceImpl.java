package com.lcp.qibao.service.impl;

import com.alibaba.fastjson.JSON;
import com.lcp.core.QueryOption;
import com.lcp.core.model.DbCollection;
import com.lcp.core.service.DataAccess;
import com.lcp.core.util.ServerMacroService;
import com.lcp.coremodel.DbEntity;
import com.lcp.qibao.auto.entity.SsPageEntity;
import com.lcp.qibao.extend.IGetLeftDataService;
import com.lcp.qibao.extend.service.InvokeMethodAdapter;
import com.lcp.qibao.service.AppFuncDataService;
import com.lcp.qibao.service.RenderBaseService;
import com.lcp.qibao.service.RenderTreeService;
import com.lcp.qibao.util.ClientMacro;
import com.lcp.qibao.util.InvokeDataSourceService;
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
