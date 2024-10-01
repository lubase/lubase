package com.lubase.cache.constant;

/**
 * 缓存前缀常量
 */
public class CacheConst {
    /**
     * 表结构信息缓存名字
     */
    public static final String CACHE_NAME_TABLE_STRUCT = "tableStruct";
    /**
     * 表触发器缓存名字
     */
    public static final String CACHE_NAME_TABLE_TRIGGER = "tableTrigger";
    /**
     * 用户信息缓存名字
     */
    public static final String CACHE_NAME_USER_INFO = "userInfo";

    public static final String PRE_CACHE_TABLE = "'dm:t:'";
    public static final String PRE_CACHE_TABLE_NAME = "'dm:t:n:'";
    public static final String PRE_CACHE_COLUMNS = "'dm:t:c:'";
    public static final String PRE_CACHE_COLUMN = "'dm:c:'";
    /**
     * 代码表缓存
     */
    public static final String PRE_CACHE_CODE_DATA = "'dm:cd:'";
    /**
     * 多语言缓存
     */
    public static final String PRE_CACHE_RESOURCE_DATA = "'dm:r:'";
    /**
     * 附件显示名称
     */
    public static final String PRE_CACHE_FILE_DATA = "'file:'";
    /**
     * 受控表清单
     */
    public static final String PRE_CACHE_CONTROLLED_TABLE_LIST = "'controlled:t:list'";
    /**
     * 获取缓存设置列表
     */
    public static final String PRE_CACHE_TABLE_CACHE_LIST = "'cache:list'";
    /**
     * 获取级别关联关系列表
     */
    public static final String PRE_CACHE_TABLE_RELATE_LIST = "'relate:list'";
    /**
     * 获取用户字段表
     */
    public static final String PRE_CACHE_USER_COLUMN = "'user:name:'";
}
