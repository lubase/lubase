package com.lubase.starter.extend;

/**
 * 扩展的自定义方法
 *
 * @author A
 */
public interface IBaseInvoke {
    /**
     * 设置方法描述
     *
     * @return
     */
    String getDescription();

    /**
     * 获取方法的唯一id
     *
     * @return
     */
    String getId();

    /**
     * 方法是否校验权限，默认校验权限
     *
     * @return
     */
    default boolean checkRight() {
        return true;
    }
}
