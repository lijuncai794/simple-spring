package com.lijuncai.beans;

import com.lijuncai.aop.Aspect;
import com.lijuncai.aop.ProxyHandler;
import com.lijuncai.web.mvc.Controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: ����Bean�Ĺ�����
 * @author: lijuncai
 **/
public class BeanFactory {

    /**
     * ʹ��Map�洢��Bean����-->Beanʵ��֮���ӳ��
     * ���Ǵ�ӳ�������չʱ���ܴ��ڲ��������ʹ��ConcurrentHashMap��֤�̰߳�ȫ
     */
    private static Map<String, Object> classToBean = new ConcurrentHashMap<>();

    /**
     * ��ȡBeanʵ��
     *
     * @param beanName Bean����
     * @return Beanʵ��
     */
    public static Object getBean(String beanName) {
        return classToBean.get(beanName);
    }

    /**
     * �ж��Ƿ���Ҫ����
     *
     * @param cls ��Ҫ�жϵ���
     * @return boolean:�Ƿ���Ҫ������
     */
    public static boolean isProxy(Class<?> cls) {
        //�ж��Ƿ��ע��@Aspectע��
        if (cls.isAnnotationPresent(Aspect.class)) {
            return true;
        }
        return false;
    }

    /**
     * �˷������ڳ�ʼ��Bean
     *
     * @param classList ���б�
     * @throws Exception ѭ�������쳣
     */
    public static void initBean(List<Class<?>> classList) throws Exception {
        List<Class<?>> toCreate = new ArrayList<>(classList);

        //ֻҪ������δ����Beanʵ�����࣬�ͼ�������ʼ��
        while (toCreate.size() != 0) {
            int remainSize = toCreate.size();
            for (int i = 0; i < toCreate.size(); i++) {
                //���Bean�ĳ�ʼ��֮�����������Ƴ���class
                if (isFinishCreate(toCreate.get(i))) {
                    toCreate.remove(i);
                }
            }
            //ÿ�α�������Ҫ�ж�:������size�Ƿ��б仯�����ޱ仯�����������ѭ���������׳��쳣
            if (toCreate.size() == remainSize) {
                throw new Exception("Cycle Dependency");
            }
        }
    }

    /**
     * ����ָ�����Beanʵ��
     *
     * @param cls ��Ҫ����Beanʵ������
     * @return boolean:�Ƿ񴴽��ɹ�
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static boolean isFinishCreate(Class<?> cls) throws IllegalAccessException, InstantiationException {
        /**
         * ���ȣ��жϸ����Ƿ���Ҫ��ʼ��ΪBean��������Ҫ��ֱ�ӷ���True
         * ���ﲻ��Ҫ�ж��Ƿ���@Beanע�⣬��Ҫ�ж�@Controllerע���@Aspectע��(����Ҳ��һ�������Bean)
         */
        if (!cls.isAnnotationPresent(Bean.class) &&
                !cls.isAnnotationPresent(Controller.class) &&
                !cls.isAnnotationPresent(Aspect.class)) {
            return true;
        }

        //ִ��Bean�ĳ�ʼ����Ҫ�ж��Ǵ�����ͨ�����Ǵ������
        Object bean = null;
        boolean needProxy = isProxy(cls);
        if (needProxy) {
            System.out.println(cls.getName() + ":need Proxy=======================");
            Object base = cls.newInstance();

            //��ȡ��������
            bean = new ProxyHandler().getProxy(base);
        } else {
            bean = cls.newInstance();
        }

        //�����������:��ȡ�������,���䱻@AutoWiredע��,����Ҫ����ע��
        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(AutoWired.class)) {
                //ͨ���ֶ�����,��Bean�����ڻ�ȡ��Ҫ������Bean
                Class<?> filedType = field.getType();
                Object reliantBean = BeanFactory.getBean(filedType.getName());

                //�����Ҫ������Bean������,��ǰBean����ʧ��
                if (reliantBean == null) {
                    return false;
                }

                /**
                 * �����Ҫ������Bean����,����ע�뵽��ǰ������Bean��
                 * �����޸��ֶ�(����)�ĿɽӴ���ΪTrue,�����ǿ���ȡ�����ֶεķ������η�
                 * �ٽ�beanע�뵽��Ӧ���ֶ�(����)
                 */
                field.setAccessible(true);
                field.set(bean, reliantBean);
            }
        }

        /**
         * ע����ɺ�,����Bean����-->Beanʵ��֮���ӳ��
         * ������಻��Ҫ��̬����,����BeanFactory�����[Bean����-->Beanʵ��]
         * ���������Ҫ��̬����,����BeanFactory�����[Bean����-->������Beanʵ��]
         */
        if (needProxy) {
            classToBean.put(cls.getInterfaces()[0].getName(), bean);
        } else {
            classToBean.put(cls.getName(), bean);
        }
        return true;
    }
}
