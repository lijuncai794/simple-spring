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
    //���������
    private Class<?> targetClass;
    //Aspect����Ϣ�б�
    private List<AspectInfo> aspectInfoList;

    public AspectListProcessor(Class<?> targetClass, List<AspectInfo> aspectInfoList) {
        this.targetClass = targetClass;
        this.aspectInfoList = aspectInfoList;
    }

    /**
     * ԭ��������ʱ��������
     *
     * @param proxy       Object ��CGLib��̬���ɵĴ�����ʵ��
     * @param method      Method ʵ���������õı�����ķ�������
     * @param args        Object[] ����ֵ�б�
     * @param methodProxy MethodProxy ������Է����Ĵ�������
     * @return Object �������õķ���ֵ
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
        //ִ��������Aspect��before����
        invokeBeforeAdvices(method, args);
        System.out.println("������ִ��");
        returnValue = methodProxy.invokeSuper(proxy, args);
        return returnValue;
    }

    /**
     * �ҳ���Ҫ����ķ���
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
     * ִ��before����
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
