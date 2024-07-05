package com.lubase.core;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.util.SpringUtil;
import com.lubase.core.extend.IInvokeMethod;
import com.lubase.core.service.multiApplications.AppConfig;
import com.lubase.core.service.multiApplications.ExtendFileService;
import com.lubase.core.service.multiApplications.ILoadExtendApplication;
import com.lubase.core.service.multiApplications.model.ExtendFileModel;
import com.lubase.core.util.ClassUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LoadExtenDJarFileTest {

    @Autowired
    ExtendFileService extendFileService;

    @Autowired
    ILoadExtendApplication loadExtendApplication;

    @Test
    void testGetFileFromDirectory() {
        System.out.println(JSON.toJSONString(extendFileService.getExtendFileLoadStatus()));
    }

    @Test
    void testGetJarFileInfo() throws IOException {

    }

    @SneakyThrows
    @Test
    void testBuildAppConfig() {
        String fullPath = "E:\\JavaCode\\fastdev\\qibao\\target\\contactperson-0.0.1-SNAPSHOT.jar";
        String a = "";
    }

    @Test
    void testLoad() {
        String methodServicePath = "123";
        assert !SpringUtil.getApplicationContext().containsBean(methodServicePath);
        methodServicePath = "com.lcp.qibao.invoke.RegisterTableTriggerByClient";
        assert SpringUtil.getApplicationContext().containsBean(methodServicePath);
        IInvokeMethod service = (IInvokeMethod) SpringUtil.getApplicationContext().getBean(methodServicePath);
        assert service.getDescription().equals("注册表触发器");

        methodServicePath = "com.fastdev.example.invokemethod.HelloFromExtend";
        assert SpringUtil.getApplicationContext().containsBean(methodServicePath);
    }

    @Test
    void testClassLoader() {
        //String fileName = "E:\\JavaCode\\invokemethod\\target\\invokemethod-0.0.1-SNAPSHOT.jar";
        String fileName = "D:\\code\\qibao-server\\workflow\\target\\workflow-0.0.1-SNAPSHOT.jar";
        //目前的代码来看 ，groupdId 是必填参数，等后续对 classLoader机制更清晰后 再优化
        String groupId = "com.lcp.workflow";


        File fileJar = new File(fileName);
        System.out.println(fileJar.getAbsolutePath());

        //通过URLClassLoader加载外部jar
        URLClassLoader urlClassLoader = null;
        try {
            urlClassLoader = new URLClassLoader(new URL[]{new URL("file:" + fileJar.getAbsolutePath())});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //要求组装必须以com开头
        Set<Class<?>> invokeClasses = ClassUtil.getClassesFromExternalJar(urlClassLoader, groupId);
        System.out.println(String.format("文件 %s 找到 %s 个class文件", fileJar.getName(), invokeClasses.size()));
        for (Class<?> obj : invokeClasses) {
            System.out.println(obj.getName());
        }
    }

    @SneakyThrows
    @Test
    void testLoadJar() {
        String fileName = "D:\\demo\\qibao-server\\workflow\\target\\workflow-0.0.1-SNAPSHOT.jar";
        //目前的代码来看 ，groupdId 是必填参数，等后续对 classLoader机制更清晰后 再优化
        String groupId = "com.lcp.workflow";

        File fileJar = new File(fileName);
        System.out.println(fileJar.getAbsolutePath());

        //通过URLClassLoader加载外部jar
        URLClassLoader urlClassLoader = null;
        try {
            urlClassLoader = new URLClassLoader(new URL[]{new URL("file:" + fileJar.getAbsolutePath())});
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //要求组装必须以com开头
        Set<Class<?>> invokeClasses = ClassUtil.getClassesFromExternalJar(urlClassLoader, groupId);
        System.out.println(String.format("文件 %s 找到 %s 个class文件", fileJar.getName(), invokeClasses.size()));
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) SpringUtil.getApplicationContext().getAutowireCapableBeanFactory();
        for (Class<?> obj : invokeClasses) {
            System.out.println(obj.getName());
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(obj);
            factory.registerBeanDefinition(obj.getName(), beanDefinitionBuilder.getBeanDefinition());
        }
        String methodServicePath = "com.lcp.workflow.controller.TestController";
        assert SpringUtil.getApplicationContext().containsBean(methodServicePath);
        IInvokeMethod service = (IInvokeMethod) SpringUtil.getApplicationContext().getBean(methodServicePath);
        assert service != null;
        //到这里已经从容器中获取到外部jar包的serv了
        System.out.println(service.getDescription());
        HashMap<String, String> map = new HashMap<>();
        map.put("code", "admin");
        Object result = service.exe(map);
        assert result != null;
        System.out.println(result);
    }
}
