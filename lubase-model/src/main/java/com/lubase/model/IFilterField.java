package com.lubase.model;

/**
 * 过滤字段
 *
 * @author A
 */
public interface IFilterField {
    boolean filter(DbField field);
}