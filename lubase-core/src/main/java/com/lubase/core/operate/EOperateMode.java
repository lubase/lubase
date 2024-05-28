package com.lubase.core.operate;

/**
 * tableFilter
 *
 * @author A
 */

public enum EOperateMode {
    /// <summary>
    /// 全匹配%LIKE% 0
    /// </summary>
    LikeAll,

    /// <summary>
    /// 左匹配LIKE% 1
    /// </summary>
    LikeLeft,

    /// <summary>
    /// 右匹配%LIKE  2
    /// </summary>
    LikeRight,

    /// <summary>
    /// 等于=  3
    /// </summary>
    Equals,

    /// <summary>
    /// 不等于&lt;&gt; 4
    /// </summary>
    NotEquals,

    /// <summary>
    /// 大于&gt; 5
    /// </summary>
    Great,

    /// <summary>
    /// 大于等于&gt;= 6
    /// </summary>
    GreateEquals,

    /// <summary>
    /// 小于&lt; 7
    /// </summary>
    Less,

    /// <summary>
    /// 小于等于&lt;= 8
    /// </summary>
    LessEquals,

    /// <summary>
    /// 在...里面 IN 9
    /// </summary>
    In,
    /// <summary>
    /// 在...里面 IN 10
    /// </summary>
    NotIn,

    /// <summary>
    /// 是否为空 IS NULL  11
    /// </summary>
    IsNull,

    /// <summary>
    /// 是否为空 IS NOT NULL  12
    /// </summary>
    IsNotNull;

    @Override
    public String toString() {
        return this.ordinal() + "";
    }
}

