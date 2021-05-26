package com.lijuncai.aop;

import java.lang.reflect.Method;

/**
 * @description: Ĭ��������
 * @author: lijuncai
 **/
public abstract class DefaultAspect {

    /**
     * ǰ��֪ͨ,����ǰ��ǿ
     *
     * @param targetClass Class<?> �������Ŀ����
     * @param method      Method �������Ŀ�귽��
     * @param args        Object[] �������Ŀ�귽������Ӧ�Ĳ����б�
     */
    public void before(Class<?> targetClass, Method method, Object[] args) throws Throwable{
    }
}
