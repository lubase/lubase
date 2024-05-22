package com.lcp.qibao.extend;

/**
 * 跨组件服务
 */
public interface CrossComponentService {

    /**
     * 过时，请使用call方法
     *
     * @param arg
     * @return
     */
    @Deprecated
    String getData(String arg);

    /**
     * 执行方法调用
     *
     * @param arg
     * @return
     */
    default String call(String arg) {
        return this.getData(arg);
    }

    String getServiceIdentification();

    String getDescription();
}
