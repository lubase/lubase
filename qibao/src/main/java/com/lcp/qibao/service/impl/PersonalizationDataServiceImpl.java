package com.lcp.qibao.service.impl;

import com.lcp.core.QueryOption;
import com.lcp.core.TableFilter;
import com.lcp.core.model.DbCollection;
import com.lcp.core.service.DataAccess;
import com.lcp.core.util.TableFilterWrapper;
import com.lcp.core.util.TypeConverterUtils;
import com.lcp.coremodel.DbEntity;
import com.lcp.coremodel.EDBEntityState;
import com.lcp.qibao.auto.entity.SdPersonalizationEntity;
import com.lcp.qibao.constant.CacheRightConstant;
import com.lcp.qibao.model.DisplayListVO;
import com.lcp.qibao.service.PersonalizationDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonalizationDataServiceImpl implements PersonalizationDataService {

    @Autowired
    DataAccess dataAccess;

    @Cacheable(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_PERSONALIZATION + "+#pageId" + "+#accountId")
    @Override
    public DisplayListVO getDisplaySetting(Long pageId, Long accountId) {
        DbCollection collFile = getPersonalization(pageId, accountId);
        DisplayListVO displayListVO = new DisplayListVO();
        if (collFile.getData().size() == 1) {
            displayListVO = getDisplayByDbEntity(collFile.getData().get(0));
        }
        return displayListVO;
    }

    private DisplayListVO getDisplayByDbEntity(DbEntity entity) {
        DisplayListVO displayListVO = new DisplayListVO();
        displayListVO.setColumnIds(TypeConverterUtils.object2String(entity.get("display_list")));
        displayListVO.setColumnWidthSetting(TypeConverterUtils.object2String(entity.get("column_width_setting")));
        displayListVO.setLockColumnCount(TypeConverterUtils.object2Integer(entity.get("lock_column_count"), 0));
        return displayListVO;
    }

    private DbCollection getPersonalization(Long pageId, Long accountId) {
        QueryOption qoFile = new QueryOption("sd_personalization");
        qoFile.setFixField("page_id,account_id,display_list,lock_column_count,column_width_setting");
        TableFilterWrapper wrapper = new TableFilterWrapper(true);
        wrapper.eq("page_id", pageId).eq("account_id", accountId);
        qoFile.setTableFilter(wrapper.build());
        DbCollection collFile = dataAccess.query(qoFile);
        return collFile;
    }

    @CachePut(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_PERSONALIZATION + "+#pageId" + "+#accountId")
    @Override
    public DisplayListVO saveDisplayColumn(Long pageId, Long accountId, String columnIds, int lockColumnCount) {
        //获取当前个性化表单配置
        DbCollection collFile = getPersonalization(pageId, accountId);
        //如果此用户没有保存过,将新增一条
        if (collFile.getData().size() == 0) {
            SdPersonalizationEntity displayEntity = new SdPersonalizationEntity();
            displayEntity.setAccount_id(accountId);
            displayEntity.setPage_id(pageId);
            displayEntity.setDisplay_list(columnIds);
            displayEntity.setLock_column_count(lockColumnCount);
            displayEntity.setState(EDBEntityState.Added);
            collFile.getData().add(displayEntity);
        }
        //如果此用户保存过,修改此页面配置
        else {
            collFile.getData().get(0).put("display_list", columnIds);
            collFile.getData().get(0).put("lock_column_count", lockColumnCount);
            collFile.getData().get(0).setState(EDBEntityState.Modified);
        }
        dataAccess.update(collFile);
        return getDisplaySetting(pageId, accountId);
    }

    @CachePut(value = CacheRightConstant.CACHE_NAME_USER_RIGHT, key = CacheRightConstant.PRE_PERSONALIZATION + "+#pageId" + "+#accountId")
    @Override
    public DisplayListVO saveColumnWidthSetting(Long pageId, Long accountId, String columnWidthSetting) {
        //获取当前个性化表单配置
        DbCollection collFile = getPersonalization(pageId, accountId);
        //如果此用户没有保存过,将新增一条
        if (collFile.getData().size() == 0) {
            SdPersonalizationEntity displayEntity = new SdPersonalizationEntity();
            displayEntity.setAccount_id(accountId);
            displayEntity.setPage_id(pageId);
            displayEntity.setColumn_width_setting(columnWidthSetting);
            displayEntity.setState(EDBEntityState.Added);
            collFile.getData().add(displayEntity);
        }
        //如果此用户保存过,修改此页面配置
        else {
            collFile.getData().get(0).put(SdPersonalizationEntity.COL_COLUMN_WIDTH_SETTING, columnWidthSetting);
            collFile.getData().get(0).setState(EDBEntityState.Modified);
        }
        dataAccess.update(collFile);
        return getDisplaySetting(pageId, accountId);
    }

    @Cacheable(value = CacheRightConstant.CACHE_NAME_CODE_DATA, key = CacheRightConstant.PRE_CODE + "+#appId")
    public List<DbEntity> getAllCode(String appId) {
        if (StringUtils.isEmpty(appId)) {
            return new ArrayList<>();
        }
        QueryOption queryOption = new QueryOption("dm_code");
        queryOption.setTableFilter(new TableFilter("code_type_id.app_id", appId));
        DbCollection collection = dataAccess.queryAllData(queryOption);
        return collection.getGenericData(DbEntity.class);
    }
}
