package com.lijuncai.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description:动态代理处理类
 * @author: lijuncai
 **/
public class ProxyHandler implements InvocationHandler {
    Object obj;

    /**
     * 获取代理对象
     *
     * @param obj 需要代理的对象
     * @return 代理后的对象
     */
    public Object getProxy(Object obj) {
        this.obj = obj;
        //使用Proxy.newProxyInstance()可以得到指定接口的代理类的对象
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(), this);
    }

    /**
     * 对标注了切点的方法实现具体的增强逻辑
     *
     * @param proxy  代理对象
     * @param method 代理对象的方法
     * @param args   方法的参数
     * @return 执行结果
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;

        if (method.isAnnotationPresent(Piontcut.class)) {//判断方法是否有@Piontcut注解
            System.out.println(method.getName() + ":method need proxy");
            System.out.println("Before call...");
            result = method.invoke(obj, args);
            System.out.println("After call...");
        } else {
            System.out.println(method.getName() + "not need proxy");
            result = method.invoke(obj, args);
        }
        return result;
    }
}
