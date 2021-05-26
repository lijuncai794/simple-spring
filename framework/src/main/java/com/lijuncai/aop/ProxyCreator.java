package com.lijuncai.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @description: ��������
 * @author: lijuncai
 **/
public class ProxyCreator {
    /**
     * ������̬������󲢷���
     *
     * @param targetClass       �������Class����
     * @param methodInterceptor ����������
     * @return
     */
    public static Object createProxy(Class<?> targetClass, MethodInterceptor methodInterceptor) {
        return Enhancer.create(targetClass, methodInterceptor);
    }


}
