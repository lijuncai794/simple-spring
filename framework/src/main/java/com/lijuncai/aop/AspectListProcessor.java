package com.lijuncai.aop;


import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * @description:
 * @author: lijuncai
 **/
public class AspectListProcessor implements MethodInterceptor {
    //被代理的类
    private Class<?> targetClass;
    //Aspect类信息列表
    private List<AspectInfo> aspectInfoList;

    public AspectListProcessor(Class<?> targetClass, List<AspectInfo> aspectInfoList) {
        this.targetClass = targetClass;
        this.aspectInfoList = aspectInfoList;
    }

    /**
     * 原方法调用时进行拦截
     *
     * @param proxy       Object 由CGLib动态生成的代理类实例
     * @param method      Method 实体类所调用的被代理的方法引用
     * @param args        Object[] 参数值列表
     * @param methodProxy MethodProxy 代理类对方法的代理引用
     * @return Object 方法调用的返回值
     * @throws Throwable
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object returnValue = null;
        collectAccurateMatchedAspectList(method);
        if (aspectInfoList == null || aspectInfoList.size() == 0) {
            returnValue = methodProxy.invokeSuper(proxy, args);
            return returnValue;
        }
        //执行完所有Aspect的before方法
        invokeBeforeAdvices(method, args);
        System.out.println("代理方法执行");
        returnValue = methodProxy.invokeSuper(proxy, args);
        return returnValue;
    }

    /**
     * 找出需要代理的方法
     *
     * @param method Method
     */
    private void collectAccurateMatchedAspectList(Method method) {
        if (aspectInfoList == null || aspectInfoList.size() == 0) {
            return;
        }
        Iterator<AspectInfo> it = aspectInfoList.iterator();
        while (it.hasNext()) {
            AspectInfo aspectInfo = it.next();
            if (!aspectInfo.getPointcutProcessor().accurateMatches(method)) {
                it.remove();
            }
        }
    }

    /**
     * 执行before方法
     *
     * @param method Method
     * @param args   Object[]
     * @throws Throwable
     */
    private void invokeBeforeAdvices(Method method, Object[] args) throws Throwable {
        for (AspectInfo aspectInfo : aspectInfoList) {
            aspectInfo.getAspectObject().before(targetClass, method, args);
        }
    }
}
