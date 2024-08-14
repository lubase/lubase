package com.lubase.core.service.multiApplications.impl;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.lubase.core.extend.IGetLeftDataService;
import com.lubase.core.extend.IInvokeMethod;
import com.lubase.core.service.multiApplications.AppConfig;
import com.lubase.core.service.multiApplications.ExtendFileService;
import com.lubase.core.service.multiApplications.ILoadExtendApplication;
import com.lubase.core.service.multiApplications.model.ExtendFileModel;
import com.lubase.core.util.ClassUtil;
import com.lubase.orm.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.loader.jar.JarFile;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.indexOfAnyBut;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 查找和加载扩展应用的核心，通过查找数据库或者接收参数，来组装appConfig，用于查找相关controller，并且注册到当前线程当中
 */
@Component
@Slf4j
public class LoadExtendApplicationImpl implements ILoadExtendApplication, ApplicationContextAware {

    /**
     * 扩展包路径，如果配置后则优先从此路径获取数据
     */
    @Value("${lubase.extend-path:}")
    String extendPath;
    @Autowired
    ExtendFileService extendFileService;
    /**
     * 注入父applicationContext
     */
    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) this.applicationContext;
        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        defaultListableBeanFactory.setAllowBeanDefinitionOverriding(true);
    }

    /**
     * 从数据库中查询需要启动的应用，并且查找到后加载。
     *
     * @return
     */
    @Override
    public AppInfoImpl autoLoad() throws IOException {
        String jarPath = "";

        if (StringUtils.isEmpty(extendPath)) {
            jarPath = Path.of(System.getProperty("user.dir"), "extend").toString();
            // 路径拼接
        } else {
            jarPath = extendPath;
        }

        log.info("extendPath is " + jarPath);
        return autoLoadFromDirectory(jarPath);
    }

    @Override
    public AppInfoImpl autoLoadFromDirectory(String path) throws IOException {
        List<ExtendFileModel> fileModelList = null;
        if (!StringUtils.isEmpty(path)) {
            fileModelList = extendFileService.getExtendFileListFromDirectory(path);
        } else {
            fileModelList = new ArrayList<>();
        }
        for (ExtendFileModel fileModel : fileModelList) {
            log.info(String.format("%s%s%s", fileModel.getFilePath(), File.separator, fileModel.getFileName()));
            AppConfig appConfig = BuildAppConfigByEntity(fileModel);
            if (appConfig != null) {
                load(appConfig);
                fileModel.setIsLoaded(true);
            } else {
                log.info("未在启动服务器下的指定目录中找到需要加载的扩展jar包！" + fileModel.getFileName());
            }
        }
        //invoke方法
        return null;
    }

    @Override
    public AppInfoImpl load(AppConfig AppConfig) {
        if (log.isInfoEnabled()) {
            log.info("Loading Application: {}", AppConfig);
        }
        List<String> tempFileApplicationURLs = AppConfig.getModuleUrlPath();
        if (log.isInfoEnabled()) {
            log.info("Local Applications: {}", tempFileApplicationURLs);
        }
        //获取重写的ClassLoader
        AppClassLoader moduleClassLoader = new AppClassLoader(AppConfig.getModuleUrl(), applicationContext.getClassLoader(), AppConfig.getOverridePackages());
        Set<Class<?>> classList = loadApplication(AppConfig, moduleClassLoader);
        loaderInvoke(classList, IInvokeMethod.class);
        loaderInvoke(classList, IGetLeftDataService.class);
        loaderInvoke(classList, IGetLeftDataService.class);
        if (log.isInfoEnabled()) {
            log.info("Loading Application  complete：{}", AppConfig);
        }
        return new AppInfoImpl(AppConfig, AppConfig.getVersion(), AppConfig.getName());
    }

    /**
     * 根据本地临时文件Application，初始化模块自己的ClassLoader，初始化Spring Application Context，同时要设置当前线程上下文的ClassLoader问模块的ClassLoader
     *
     * @param AppConfig
     * @return
     */
    Set<Class<?>> loadApplication(AppConfig AppConfig, AppClassLoader moduleClassLoader) {
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        Set<Class<?>> classList = new LinkedHashSet<Class<?>>();
        try {
            for (String pack : AppConfig.getOverridePackages()) {
                if (isBlank(pack)) {
                    continue;
                }
                classList = moduleClassLoader.loadClassByPack(pack, AppConfig.getModuleUrl());
                for (Class obj : classList) {
                    if (obj.isInterface() || Modifier.isAbstract(obj.getModifiers())) {
                        continue;
                    }
                    registerBean(obj);
                }
                for (Class obj : classList) {
                    if (obj.getName().contains("Controller")) {
                        registerController(StringUtils.uncapitalize(obj.getName().substring(obj.getName().lastIndexOf(".") + 1)));
                    }
                }
            }
        } catch (Throwable e) {
            CachedIntrospectionResults.clearClassLoader(moduleClassLoader);
            throw Throwables.propagate(e);
        } finally {
            //还原当前线程的ClassLoader
            Thread.currentThread().setContextClassLoader(currentClassLoader);
        }
        return classList;
    }


    void registerBean(Class clazz) throws Exception {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        //设置当前bean定义对象是单利的
        beanDefinition.setScope("singleton");
        //将变量首字母置小写
        String beanName = StringUtils.uncapitalize(clazz.getName());
        beanName = beanName.substring(beanName.lastIndexOf(".") + 1);
        beanName = StringUtils.uncapitalize(beanName);
        //将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        String[] BeanNames = configurableApplicationContext.getBeanDefinitionNames();
        //遍历是否已经注册
        //todo 为了管理需要自己维护注册的bean
        for (String BeanName : BeanNames) {
            if (BeanName.equals(beanName)) {
                log.info("{} 已经注册", beanName);
                return;
            }
        }
        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinition);
        log.info("{} 注册", beanName);
    }

    /**
     * 注册Controller
     *
     * @param controllerBeanName
     * @throws Exception
     */
    void registerController(String controllerBeanName) throws Exception {
        final RequestMappingHandlerMapping requestMappingHandlerMapping =
                applicationContext.getBean(RequestMappingHandlerMapping.class);
        if (requestMappingHandlerMapping != null) {
            String handler = controllerBeanName;
            Object controller = applicationContext.getBean(handler);
            if (controller == null) {
                return;
            }
            unregisterController(controllerBeanName);
            //注册Controller,获取detectHandlerMethods方法
            Method method = requestMappingHandlerMapping.getClass().getSuperclass().getSuperclass().
                    getDeclaredMethod("detectHandlerMethods", Object.class);
            //将private改为可使用
            method.setAccessible(true);
            //执行requestMappingHandlerMapping的detectHandlerMethods 注册controller
            method.invoke(requestMappingHandlerMapping, handler);
            System.out.println("注册controller：" + handler);
            log.info("{} 注册", handler);
        }
    }

    /**
     * 去掉Controller的Mapping
     *
     * @param controllerBeanName
     */
    void unregisterController(String controllerBeanName) {
        final RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping)
                applicationContext.getBean("requestMappingHandlerMapping");
        if (requestMappingHandlerMapping != null) {
            String handler = controllerBeanName;
            Object controller = applicationContext.getBean(handler);
            if (controller == null) {
                return;
            }
            final Class<?> targetClass = controller.getClass();
            ReflectionUtils.doWithMethods(targetClass, new ReflectionUtils.MethodCallback() {
                public void doWith(Method method) {
                    Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
                    try {
                        Method createMappingMethod = RequestMappingHandlerMapping.class.
                                getDeclaredMethod("getMappingForMethod", Method.class, Class.class);
                        createMappingMethod.setAccessible(true);
                        RequestMappingInfo requestMappingInfo = (RequestMappingInfo)
                                createMappingMethod.invoke(requestMappingHandlerMapping, specificMethod, targetClass);
                        if (requestMappingInfo != null) {
                            requestMappingHandlerMapping.unregisterMapping(requestMappingInfo);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, ReflectionUtils.USER_DECLARED_METHODS);
        }
    }

    <T> Set<Class<?>> loaderInvoke(Set<Class<?>> classList, Class<T> T) {
        Set<Class<?>> classByType = getClassByType(classList, T);
        RegisterInvoke(classByType);
        return classByType;
    }


    @Override
    public AppConfig BuildAppConfigByEntity(ExtendFileModel fileModel) throws IOException {
        //把找到的类加载到容器,需要判断是否已经加载
        if (StringUtils.isEmpty(fileModel.getFilePath()) || StringUtils.isEmpty(fileModel.getFileName())) {
            return null;
        }
        //todo 判断/\
        String fullPath = String.format("%s%s%s", fileModel.getFilePath(), File.separator, fileModel.getFileName());

        if (fullPath.toLowerCase().endsWith(".jar")) {
            //获取workflow-0.0.1-SNAPSHOT
            String[] OriginalNameArray = fileModel.getFileName().split(".jar");
            String verSion = "";
            String JarName = "";
            if (OriginalNameArray.length > 0) {
                //拆分成workflow    | 0.0.1  |  SNAPSHOT
                String[] JarNameArray = OriginalNameArray[0].split("-");
                JarName = JarNameArray[0];
                //拼装0.0.1-SNAPSHOT 预留最后一位单独组装
                for (int i = 1; i < JarNameArray.length - 1; i++) {
                    verSion += JarNameArray[i] + "-";
                }
                verSion += JarNameArray[JarNameArray.length - 1];
            }
            //组装配置
            AppConfig appConfig = BuildAppConfig(JarName, verSion, fullPath, fileModel.getGroupId());
            return appConfig;
        }
        return null;
    }

    private AppConfig BuildAppConfig(String JarName, String verSion, String fullPath, String pack) throws IOException {
        AppConfig appConfig = new AppConfig();
        appConfig.setName(JarName);
        appConfig.setEnabled(true);
        appConfig.setVersion(verSion);
        appConfig.setOverridePackages(ImmutableList.of(pack));
        File fileJar = new File(fullPath);
        if (!fileJar.exists()) {
            return null;
        }
        JarFile jarFile = new JarFile(fileJar);
        appConfig.setModuleUrl(ImmutableList.of(jarFile.getUrl()));
        return appConfig;
    }

    /**
     * 默认从当前启动目录下获取class
     *
     * @param type
     */
    @Override
    public Set<Class<?>> getClassByType(Class<?> type) {
        Set<Class<?>> classList = ClassUtil.getClasses(ClassUtil.BASE_JAR_PATH);
        return getClassByType(classList, type);
    }

    /**
     * 默认从指定的路径和包目录下获取class
     *
     * @param type
     */
    Set<Class<?>> getClassByType(Set<Class<?>> classList, Class<?> type) {
        Set<Class<?>> externalClass = new HashSet<>();
        for (Class<?> tt : classList) {
            for (Class<?> inter : tt.getInterfaces()) {
                if (inter == type) {
                    System.out.println("-----------------" + tt.getName());
                    externalClass.add(tt);
                }
            }
        }
        return externalClass;
    }


    @Override
    public void RegisterInvoke(Set<Class<?>> classByType) {
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) SpringUtil.getApplicationContext().getAutowireCapableBeanFactory();
        for (Class<?> obj : classByType) {
            if (!factory.containsBean(obj.getName())) {
                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(obj);
                factory.registerBeanDefinition(obj.getName(), beanDefinitionBuilder.getBeanDefinition());
                System.out.println("register type " + obj.getName());
            }
        }
    }
}
