package com.lcp.qibao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@SpringBootTest
public class FileTest {
    @Test
    public void folderTest() {
        File file = new File("D:\\a\\a\\a");
        boolean mkdir = file.mkdir();
        if (file.isDirectory() && !file.exists()) {
            mkdir = file.mkdirs();
        }
        String localPath = System.getProperty("user.dir");
        System.out.println(mkdir);


    }

    void getJarName(String jarFile) throws Exception {

        try{
            //通过将给定路径名字符串转换为抽象路径名来创建一个新File实例
            File f = new File(jarFile);
            URL url1 = f.toURI().toURL();
            URLClassLoader myClassLoader = new URLClassLoader(new URL[]{url1},Thread.currentThread().getContextClassLoader());

            //通过jarFile和JarEntry得到所有的类
            JarFile jar = new JarFile(jarFile);
            //返回zip文件条目的枚举
            Enumeration<JarEntry> enumFiles = jar.entries();
            JarEntry entry;

            //测试此枚举是否包含更多的元素
            while(enumFiles.hasMoreElements()){
                entry = (JarEntry)enumFiles.nextElement();
                if(entry.getName().indexOf("META-INF")<0){
                    String classFullName = entry.getName();
                    if(!classFullName.endsWith(".class")){
                        classFullName = classFullName.substring(0,classFullName.length()-1);
                    } else{
                        //去掉后缀.class
                        String className = classFullName.substring(0,classFullName.length()-6).replace("/", ".");
                        Class<?> myclass = myClassLoader.loadClass(className);
                        //打印类名
                        System.out.println("*****************************");
                        System.out.println("全类名:" + className);

                        //得到类中包含的属性
                        Method[] methods = myclass.getMethods();
                        for (Method method : methods) {
                            String methodName = method.getName();
                            System.out.println("方法名称:" + methodName);
                            Class<?>[] parameterTypes = method.getParameterTypes();
                            for (Class<?> clas : parameterTypes) {
                                // String parameterName = clas.getName();
                                String parameterName = clas.getSimpleName();
                                System.out.println("参数类型:" + parameterName);
                            }
                            System.out.println("==========================");
                        }
                    }
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }


    @Test
    public void loadFile2() throws  Exception {
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        String filePath = "D:\\java\\projects\\kuozhandemo\\target\\invokemethod-0.0.1-SNAPSHOT.jar";
        boolean recursive = true;
        String packageName = "com.fastdev";
        getJarName(filePath);
    }


}
