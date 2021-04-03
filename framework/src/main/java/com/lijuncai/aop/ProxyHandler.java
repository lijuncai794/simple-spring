package com.lijuncai.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description:��̬��������
 * @author: lijuncai
 **/
public class ProxyHandler implements InvocationHandler {
    Object obj;

    /**
     * ��ȡ�������
     *
     * @param obj ��Ҫ����Ķ���
     * @return �����Ķ���
     */
    public Object getProxy(Object obj) {
        this.obj = obj;
        //ʹ��Proxy.newProxyInstance()���Եõ�ָ���ӿڵĴ�����Ķ���
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(), this);
    }

    /**
     * �Ա�ע���е�ķ���ʵ�־������ǿ�߼�
     *
     * @param proxy  �������
     * @param method �������ķ���
     * @param args   �����Ĳ���
     * @return ִ�н��
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;

        if (method.isAnnotationPresent(Piontcut.class)) {//�жϷ����Ƿ���@Piontcutע��
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
