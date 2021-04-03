package com.lijuncai.starter;

import com.lijuncai.beans.BeanFactory;
import com.lijuncai.core.ClassScanner;
import com.lijuncai.web.handler.HandlerManager;
import com.lijuncai.web.server.TomcatServer;
import org.apache.catalina.LifecycleException;

import java.io.IOException;
import java.util.List;

/**
 * @description: MyApplication, 框架的初始化入口类
 * @author: lijuncai
 **/
public class MyApplication {
    public static void run(Class<?> cls, String[] args) {
        System.out.println("Hello my-spring!");

        System.out.println("Tomcat starting...");
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            tomcatServer.startServer();

            //在框架中获取所有的class
            List<Class<?>> classList = ClassScanner.scanClasses(cls.getPackage().getName());
            classList.forEach(it -> System.out.println(it.getName()));

            //在框架入口使用BeanFactory的initBean()初始化Bean
            BeanFactory.initBean(classList);
            //在框架入口使用HandlerManager初始化所有的MappingHandler
            HandlerManager.resolveMappingHandler(classList);

        } catch (LifecycleException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
