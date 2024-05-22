package com.lcp.qibao.service.impl;

import com.lcp.core.QueryOption;
import com.lcp.core.TableFilter;
import com.lcp.core.model.DbCollection;
import com.lcp.core.operate.EOperateMode;
import com.lcp.core.service.DataAccess;
import com.lcp.core.util.QueryOptionWrapper;
import com.lcp.core.util.TypeConverterUtils;
import com.lcp.coremodel.DbEntity;
import com.lcp.qibao.auto.entity.DmCodeEntity;
import com.lcp.qibao.constant.CacheRightConstant;
import com.lcp.qibao.constant.CommonConstant;
import com.lcp.qibao.model.CodeDataTypeVO;
import com.lcp.qibao.model.CodeDataVO;
import com.lcp.qibao.service.CodeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 代码表服务，后续需要缓存处理
 *
 * @author A
 */
@Service
public class CodeDataServiceImpl implements CodeDataService {
    @Autowired
    DataAccess dataAccess;
    @Autowired
    PersonalizationDataServiceImpl personalizationDataService;

    @Override
    public List<CodeDataTypeVO> getCodeListForAppSetting(String appId) {
        if (StringUtils.isEmpty(appId)) {
            return new ArrayList<>();
        }

        List<DbEntity> dmCodeList = personalizationDataService.getAllCode(appId);
        //如果是后端应用，则连带获取流程应用的码表数据
        if (!appId.equals(CommonConstant.WORKFLOW_APP_ID.toString())) {
            dmCodeList.addAll(personalizationDataService.getAllCode(CommonConstant.WORKFLOW_APP_ID.toString()));
        }
        if (!appId.equals(CommonConstant.SYSTEM_APP_ID.toString())) {
            dmCodeList.addAll(personalizationDataService.getAllCode(CommonConstant.SYSTEM_APP_ID.toString()));
        }
        return processCodeList(dmCodeList);
    }

    private List<CodeDataTypeVO> processCodeList(List<DbEntity> dmCodeList) {
        List<CodeDataTypeVO> list = new ArrayList<>();
        HashMap<Long, List<DbEntity>> map = new HashMap<>();
        for (DbEntity dmCode : dmCodeList) {
            if (!map.containsKey(TypeConverterUtils.object2Long(dmCode.get("code_type_id")))) {
                map.put(TypeConverterUtils.object2Long(dmCode.get("code_type_id")), new ArrayList<>());
            }
            map.get(TypeConverterUtils.object2Long(dmCode.get("code_type_id"))).add(dmCode);
        }
        for (Long typeId : map.keySet()) {
            CodeDataTypeVO codeDataTypeVO = new CodeDataTypeVO();
            codeDataTypeVO.setCodeDataTypeId(typeId);
            List<CodeDataVO> listCodeVO = new ArrayList<>();
            for (DbEntity dmcode : map.get(typeId)) {
                CodeDataVO codeDataVO = new CodeDataVO();
                codeDataVO.setCode(TypeConverterUtils.object2String(dmcode.get("code_value")));
                codeDataVO.setName(TypeConverterUtils.object2String(dmcode.get("code_name")));
                codeDataVO.setPydm(TypeConverterUtils.object2String(dmcode.get("pydm")));
                codeDataVO.setPCode(TypeConverterUtils.object2String(dmcode.get("parent_code")));
                codeDataVO.setOrderId(TypeConverterUtils.object2Integer(dmcode.get("order_id")));
                codeDataVO.setEnableTag(TypeConverterUtils.object2Integer(dmcode.get("enable_tag")));
                listCodeVO.add(codeDataVO);
            }
            codeDataTypeVO.setCodeList(listCodeVO);
            list.add(codeDataTypeVO);
        }
        return list;
    }

    @Override
    public List<CodeDataTypeVO> getCodeListByAppId(Long appId) {
        if (StringUtils.isEmpty(appId)) {
            return new ArrayList<>();
        }
        List<DbEntity> dmCodeList = personalizationDataService.getAllCode(appId.toString());
        return processCodeList(dmCodeList);
    }

    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_CODE_INFO + "+#typeId")
    @Override
    public List<DmCodeEntity> getCodeListByTypeId(Long typeId) {
        QueryOption queryOption = QueryOptionWrapper.select("*").from("dm_code")
                .where(new TableFilter("code_type_id", typeId, EOperateMode.Equals)).build();
        DbCollection collection = dataAccess.query(queryOption);
        List<DmCodeEntity> dmCodeEntities = collection.getGenericData(DmCodeEntity.class);
        return dmCodeEntities;
    }
}
