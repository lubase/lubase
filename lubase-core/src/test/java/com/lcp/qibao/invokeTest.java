package com.lcp.qibao;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.service.DataAccess;
import com.lubase.orm.QueryOption;
import com.lubase.orm.model.DbCollection;
import com.lubase.orm.TableFilter;
import com.lubase.model.DbEntity;
import com.lubase.orm.util.SpringUtil;
import com.lubase.core.extend.IGetLeftDataService;
import com.lubase.core.extend.IGetMainDataService;
import com.lubase.core.extend.IInvokeMethod;
import com.lubase.core.util.ClientMacro;
import com.lubase.core.util.ClassUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class invokeTest {

    @Autowired
    List<IInvokeMethod> invokeMethodsList;

    @Autowired
    List<IGetLeftDataService> leftDataServices;
    @Autowired
    List<IGetMainDataService> mainDataServices;
    @Autowired
    DataAccess dataAccess;

    @Test
    void testAllInvokeMethod() {
        System.out.println("IInvokeMethod count is " + invokeMethodsList.size());
        System.out.println("IGetLeftDataService count is " + leftDataServices.size());
        System.out.println("IGetMainDataService count is " + mainDataServices.size());

        testMethodIdIsRight();
        testMainMethodIdIsRight();
        testLeftMethodIdIsRight();
    }

    @Test
    void testMethodIdIsRight() {
        assert invokeMethodsList != null;
        System.out.println(JSON.toJSON(invokeMethodsList));
        for (IInvokeMethod methodInfo : invokeMethodsList) {
            String path = methodInfo.getClass().getName();
            IInvokeMethod method = (IInvokeMethod) SpringUtil.getApplicationContext().getBean(path);
            assert method != null;
            String dbId = getIdByPath(path);
            assert dbId != null;
            System.out.println(String.format("method id is %s,dbid is %s", method.getId(), dbId));
            assert method.getId().equals(dbId);
        }
    }

    @Test
    void testLeftMethodIdIsRight() {
        assert leftDataServices != null;
        System.out.println(JSON.toJSON(leftDataServices));
        for (IGetLeftDataService methodInfo : leftDataServices) {
            String path = methodInfo.getClass().getName();
            IGetLeftDataService method = (IGetLeftDataService) SpringUtil.getApplicationContext().getBean(path);
            assert method != null;
            String dbId = getIdByPath(path);
            assert dbId != null;
            System.out.println(String.format("method id is %s,dbid is %s", method.getId(), dbId));
            assert method.getId().equals(dbId);
        }
    }

    @Test
    void testMainMethodIdIsRight() {
        assert mainDataServices != null;
        System.out.println(JSON.toJSON(mainDataServices));
        for (IGetMainDataService methodInfo : mainDataServices) {
            String path = methodInfo.getClass().getName();
            IGetMainDataService method = (IGetMainDataService) SpringUtil.getApplicationContext().getBean(path);
            assert method != null;
            String dbId = getIdByPath(path);
            assert dbId != null;
            System.out.println(String.format("method id is %s,dbid is %s", method.getId(), dbId));
            assert method.getId().equals(dbId);
        }
    }

    String getIdByPath(String path) {
        QueryOption queryOption = new QueryOption("ss_invoke_method");
        queryOption.setTableFilter(new TableFilter("path", path));
        DbCollection collection = dataAccess.query(queryOption);
        if (collection.getData().size() == 1) {
            return collection.getData().get(0).getId().toString();
        } else {
            System.out.println(String.format("path %s is not found", path));
            return "";
        }
    }

    @Test
    void test() {
        IGetLeftDataService service = (IGetLeftDataService) SpringUtil.getApplicationContext().getBean("aaa");
        //assert service == null;
        Object obj = service.exe("00", ClientMacro.init(""));
        System.out.println(service.getDescription() + obj);
        System.out.println(service.getClass());

        service = (IGetLeftDataService) SpringUtil.getApplicationContext().getBean("bbb");
        //assert service == null;
        obj = service.exe("00", ClientMacro.init(""));
        System.out.println(service.getDescription() + obj);
        System.out.println(service.getClass());
    }

    @Test
    void testGetCodeData() {
        IGetLeftDataService service = (IGetLeftDataService) SpringUtil.getApplicationContext().getBean("com.lcp.fastdev.invoke.pageLeftDataSource.CodeManager");
        System.out.println(service.getDescription());
        List<DbEntity> list = service.exe("", ClientMacro.init(""));
        System.out.println(list.size());

    }

    @Test
    void testRegister() {
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) SpringUtil.getApplicationContext().getAutowireCapableBeanFactory();
//        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(testInvoke2.class);
//        //beanDefinitionBuilder.addAutowiredProperty()
//        factory.registerBeanDefinition("test333", beanDefinitionBuilder.getBeanDefinition());

        IGetLeftDataService service = (IGetLeftDataService) SpringUtil.getApplicationContext().getBean("test333");
        System.out.println(service.getDescription());
    }

    @Test
    void getBlassByFanShe() {
        //get all leftdatasource
        Set<Class<?>> leftDataClasses = getClassByType(IGetLeftDataService.class);
        //regiter
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) SpringUtil.getApplicationContext().getAutowireCapableBeanFactory();
        for (Class<?> obj : leftDataClasses) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(obj);
            factory.registerBeanDefinition(obj.getName(), beanDefinitionBuilder.getBeanDefinition());
        }

        IGetLeftDataService service = (IGetLeftDataService) SpringUtil.getApplicationContext().getBean("com.lcp.fastdev.service.pageLeftDataSource.CodeManager");
        System.out.println(service.getDescription());
        service = (IGetLeftDataService) SpringUtil.getApplicationContext().getBean("com.lcp.fastdev.service.pageLeftDataSource.testInvoke");
        System.out.println(service.getDescription());
        service = (IGetLeftDataService) SpringUtil.getApplicationContext().getBean("com.lcp.fastdev.service.pageLeftDataSource.testInvoke2");
        System.out.println(service.getDescription());
        service = (IGetLeftDataService) SpringUtil.getApplicationContext().getBean("com.lcp.fastdev.service.test.testInvoke");
        System.out.println(service.getDescription());
    }

    Set<Class<?>> getClassByType(Class<?> type) {
        Set<Class<?>> leftDataClasses = new HashSet<>();
        Set<Class<?>> classList = ClassUtil.getClasses("com.lcp.fastdev");
        for (Class<?> tt : classList) {

            for (Class<?> inter : tt.getInterfaces()) {
                if (inter == type) {
                    System.out.println("-----------------" + tt.getName());
                    leftDataClasses.add(tt);
                }
            }
        }
        return leftDataClasses;
    }
}
