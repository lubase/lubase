package com.lubase.cache.service.impl;

import com.lubase.model.DbEntity;
import com.lubase.cache.mapper.UserInfoMapper;
import com.lubase.cache.service.InitCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@CacheConfig(cacheNames = "userInfo")
@Slf4j
@Service
public class UserInfoService implements InitCacheService {
    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public void appStartInitCache() {
        System.out.println("缓存表设置信息初始化缓存成功");
    }

    @Cacheable(key = "'u:id:'" + "+#userId", unless = "#result == null")
    //@Cacheable(key = "'u:id:'" + "+#userId")
    public DbEntity getUserNameById(Long userId) {
        if (userId == null) {
            return null;
        }
        DbEntity entity = userInfoMapper.getUserInfoById(userId);
        if (entity != null) {
            entity.put("user_name", String.format("%s(%s)", entity.get("user_name"), entity.get("user_code")));
        }
        return entity;
    }

    @Cacheable(key = "'u:code:'" + "+#userCode", unless = "#result == null")
    public DbEntity getUserNameByCode(String userCode) {
        if (userCode == null) {
            return null;
        }
        DbEntity entity = userInfoMapper.getUserInfoByCode(userCode);
        if (entity != null) {
            entity.put("user_name", String.format("%s(%s)", entity.get("user_name"), entity.get("user_code")));
        }
        return entity;
    }

    @Cacheable(key = "'dept:id:'" + "+#id")
    public DbEntity getDeptInfoByCode(Long id) {
        if (id == null) {
            return null;
        }
        return userInfoMapper.getDeptInfoByCode(id);
    }
}
