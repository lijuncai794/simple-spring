package com.lijuncai.aop;

import java.lang.reflect.Method;

/**
 * @description: 默认切面类
 * @author: lijuncai
 **/
public abstract class DefaultAspect {

    /**
     * 前置通知,调用前增强
     *
     * @param targetClass Class<?> 被代理的目标类
     * @param method      Method 被代理的目标方法
     * @param args        Object[] 被代理的目标方法所对应的参数列表
     */
    public void before(Class<?> targetClass, Method method, Object[] args) throws Throwable{
    }
}
