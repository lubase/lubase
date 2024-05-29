package com.lubase.orm.extend;

import org.springframework.context.ApplicationContext;

import java.util.ArrayList;


/**
 * 扩展应用加载完毕后事件
 */
public interface ExtendAppLoadCompleteService {

    /**
     * 扩展应用加载完成事件
     *
     * @param applicationContext
     */
    void LoadCompleted(ApplicationContext applicationContext);

    /**
     * 刷新数据
     */
    void clearData();

    /**
     * 刷新后，再次重新加载
     *
     * @param applicationContext
     */
    default void refreshAndLoadData(ApplicationContext applicationContext) {
        clearData();
        LoadCompleted(applicationContext);
    }
}
