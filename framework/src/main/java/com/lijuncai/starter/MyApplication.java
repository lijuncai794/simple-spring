package com.lijuncai.starter;

import com.lijuncai.beans.BeanFactory;
import com.lijuncai.core.ClassScanner;
import com.lijuncai.web.handler.HandlerManager;
import com.lijuncai.web.server.TomcatServer;
import org.apache.catalina.LifecycleException;

import java.io.IOException;
import java.util.List;

/**
 * @description: MyApplication, ��ܵĳ�ʼ�������
 * @author: lijuncai
 **/
public class MyApplication {
    public static void run(Class<?> cls, String[] args) {
        System.out.println("Hello my-spring!");

        System.out.println("Tomcat starting...");
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            tomcatServer.startServer();

            //�ڿ���л�ȡ���е�class
            List<Class<?>> classList = ClassScanner.scanClasses(cls.getPackage().getName());
            classList.forEach(it -> System.out.println(it.getName()));

            //�ڿ�����ʹ��BeanFactory��initBean()��ʼ��Bean
            BeanFactory.initBean(classList);
            //�ڿ�����ʹ��HandlerManager��ʼ�����е�MappingHandler
            HandlerManager.resolveMappingHandler(classList);

        } catch (LifecycleException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
