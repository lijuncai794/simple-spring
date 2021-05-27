package com.lijuncai.beans;

import com.lijuncai.aop.Aspect;
import com.lijuncai.aop.AspectWeaver;
import com.lijuncai.aop.ProxyHandler;
import com.lijuncai.web.mvc.Controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @description: ����Bean�Ĺ�����
 * @author: lijuncai
 **/
public class BeanFactory {

    /**
     * ʹ��Map�洢��Bean����-->Beanʵ��֮���ӳ��
     */
    public static Map<String, Object> classToBean = new ConcurrentHashMap<>();

    private static List<Class<?>> classList = new CopyOnWriteArrayList<>();

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
     * ����Beanʵ��
     *
     * @param beanName String Bean����
     * @param obj      Object Bean����
     */
    public static void setBean(String beanName, Object obj) {
        classToBean.put(beanName, obj);
    }

    /**
     * ��ȡ��ע��@Aspectע�����
     */
    public static List<Class<?>> getClassesOfAspect() {
        List<Class<?>> classes = new ArrayList<>();
        for (Class<?> cls : classList) {
            if (cls.isAnnotationPresent(Aspect.class)) {
                classes.add(cls);
            }
        }
        return classes;
    }

    /**
     * �������б�
     *
     * @param classList List<Class<?>> ���б�
     */
    public static void setClassList(List<Class<?>> classList) {
        BeanFactory.classList = classList;
    }

    /**
     * ��ȡ���б�
     */
    public static List<Class<?>> getClassList() {
        return classList;
    }

    /**
     * ��ʼ��Bean
     *
     * @throws Exception ѭ�������쳣
     */
    public static void initBean() throws Exception {
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

        //ִ��Bean�ĳ�ʼ��
        Object bean = cls.newInstance();

        //�����������:��ȡ�������,���䱻@AutoWiredע��,����Ҫ����ע��
        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(AutoWired.class)) {
//                System.out.println("��Ҫע��...");
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
         */
        classToBean.put(cls.getName(), bean);
        return true;
    }

    /**
     * ��̬����֮��,ˢ��Bean������
     */
    public static void freshDependence() throws IllegalAccessException {
        for (Map.Entry<String, Object> entry : classToBean.entrySet()) {
            Object bean = entry.getValue();
            Class<?> clazz = bean.getClass();

            //����ע��������Խ�������ע��,ע���µĶ�̬����֮��Ķ���
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(AutoWired.class)) {
                    Class<?> filedType = field.getType();
                    Object reliantBean = BeanFactory.getBean(filedType.getName());

                    field.setAccessible(true);
                    field.set(bean, reliantBean);
                }
            }
        }
    }
}
