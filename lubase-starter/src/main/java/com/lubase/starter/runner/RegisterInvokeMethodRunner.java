package com.lubase.starter.runner;

import com.lubase.starter.extend.IGetLeftDataService;
import com.lubase.starter.extend.IGetMainDataService;
import com.lubase.starter.extend.IInvokeMethod;
import com.lubase.starter.service.multiApplications.ILoadExtendApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Order(value = 1)
public class RegisterInvokeMethodRunner implements ApplicationRunner {
    @Autowired
    ILoadExtendApplication ApplicationLoader;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //get all invokeMethod
        Set<Class<?>> invokeClasses = ApplicationLoader.getClassByType(IInvokeMethod.class);
        //register
        ApplicationLoader.RegisterInvoke(invokeClasses);

        //get all leftdatasource
        Set<Class<?>> leftDataClasses = ApplicationLoader.getClassByType(IGetLeftDataService.class);
        //register
        ApplicationLoader.RegisterInvoke(leftDataClasses);

        //get all IGetMainDataService
        Set<Class<?>> mainDataClasses = ApplicationLoader.getClassByType(IGetMainDataService.class);
        //register
        ApplicationLoader.RegisterInvoke(mainDataClasses);
    }
}
