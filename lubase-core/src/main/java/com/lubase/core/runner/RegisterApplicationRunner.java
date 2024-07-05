package com.lubase.core.runner;

import com.lubase.orm.extend.ExtendAppLoadCompleteService;
import com.lubase.orm.util.SpringUtil;
import com.lubase.core.service.multiApplications.ILoadExtendApplication;
import com.lubase.core.runner.rocketmq.RocketMqListenerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;


@Order(2)
@Component
@Slf4j
public class RegisterApplicationRunner implements ApplicationRunner {
    /**
     * 应用ID，如果为空则表示为主应用
     */
    @Value("${appId:default}")
    private String appIdStr;
    /**
     * 应用ID
     */
    Long appId = 0L;

    @Autowired
    ILoadExtendApplication loadExtendApplication;
    @Autowired
    RocketMqListenerRegistry rocketMqListenerRegistry;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //无参数输入走加载主框架加载
        if (appIdStr.equals("default")) {
            System.out.println(System.getProperty("user.dir"));
            loadExtendApplication.autoLoad();
            System.out.println("自动加载所有……");
        } else {
            try {
                appId = Long.parseLong(appIdStr);
            } catch (Exception ex) {
                System.out.println("appId 输入错误:" + ex.getMessage());
            }
            //参数如果有问题,没有加载任何库
            System.out.println("业务应用模式启动，应用ID:" + appId);
        }
        // 执行模块加载完成事件
        Map<String, ExtendAppLoadCompleteService> serviceMap = SpringUtil.getApplicationContext().getBeansOfType(ExtendAppLoadCompleteService.class);
        log.info(String.format("当前读取到loadCompleteServiceList数量为：%s", serviceMap.keySet().size()));
        for (String key : serviceMap.keySet()) {
            serviceMap.get(key).LoadCompleted(SpringUtil.getApplicationContext());
        }

        rocketMqListenerRegistry.afterSingletonsInstantiated();
    }
}
