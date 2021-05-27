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
 * @description: 管理Bean的工厂类
 * @author: lijuncai
 **/
public class BeanFactory {

    /**
     * 使用Map存储：Bean名称-->Bean实例之间的映射
     */
    public static Map<String, Object> classToBean = new ConcurrentHashMap<>();

    private static List<Class<?>> classList = new CopyOnWriteArrayList<>();

    /**
     * 获取Bean实例
     *
     * @param beanName Bean名称
     * @return Bean实例
     */
    public static Object getBean(String beanName) {
        return classToBean.get(beanName);
    }


    /**
     * 设置Bean实例
     *
     * @param beanName String Bean名称
     * @param obj      Object Bean对象
     */
    public static void setBean(String beanName, Object obj) {
        classToBean.put(beanName, obj);
    }

    /**
     * 获取标注了@Aspect注解的类
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
     * 设置类列表
     *
     * @param classList List<Class<?>> 类列表
     */
    public static void setClassList(List<Class<?>> classList) {
        BeanFactory.classList = classList;
    }

    /**
     * 获取类列表
     */
    public static List<Class<?>> getClassList() {
        return classList;
    }

    /**
     * 初始化Bean
     *
     * @throws Exception 循环依赖异常
     */
    public static void initBean() throws Exception {
        List<Class<?>> toCreate = new ArrayList<>(classList);

        //只要还存在未创建Bean实例的类，就继续做初始化
        while (toCreate.size() != 0) {
            int remainSize = toCreate.size();
            for (int i = 0; i < toCreate.size(); i++) {
                //完成Bean的初始化之后，在容器中移除该class
                if (isFinishCreate(toCreate.get(i))) {
                    toCreate.remove(i);
                }
            }
            //每次遍历都需要判断:容器的size是否有变化，若无变化则代表陷入了循环依赖，抛出异常
            if (toCreate.size() == remainSize) {
                throw new Exception("Cycle Dependency");
            }
        }
    }

    /**
     * 创建指定类的Bean实例
     *
     * @param cls 需要创建Bean实例的类
     * @return boolean:是否创建成功
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static boolean isFinishCreate(Class<?> cls) throws IllegalAccessException, InstantiationException {
        /**
         * 首先，判断该类是否需要初始化为Bean，若不需要则直接返回True
         * 这里不仅要判断是否有@Bean注解，还要判断@Controller注解和@Aspect注解(它们也是一种特殊的Bean)
         */
        if (!cls.isAnnotationPresent(Bean.class) &&
                !cls.isAnnotationPresent(Controller.class) &&
                !cls.isAnnotationPresent(Aspect.class)) {
            return true;
        }

        //执行Bean的初始化
        Object bean = cls.newInstance();

        //解决依赖问题:获取类的属性,若其被@AutoWired注解,则需要依赖注入
        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(AutoWired.class)) {
//                System.out.println("需要注入...");
                //通过字段类型,从Bean工厂内获取需要依赖的Bean
                Class<?> filedType = field.getType();
                Object reliantBean = BeanFactory.getBean(filedType.getName());

                //如果需要依赖的Bean不存在,则当前Bean创建失败
                if (reliantBean == null) {
                    return false;
                }

                /**
                 * 如果需要依赖的Bean存在,则将其注入到当前创建的Bean中
                 * 首先修改字段(属性)的可接触性为True,作用是可以取消该字段的访问修饰符
                 * 再将bean注入到相应的字段(属性)
                 */
                field.setAccessible(true);
                field.set(bean, reliantBean);
            }
        }

        /**
         * 注入完成后,建立Bean类型-->Bean实例之间的映射
         */
        classToBean.put(cls.getName(), bean);
        return true;
    }

    /**
     * 动态代理之后,刷新Bean的依赖
     */
    public static void freshDependence() throws IllegalAccessException {
        for (Map.Entry<String, Object> entry : classToBean.entrySet()) {
            Object bean = entry.getValue();
            Class<?> clazz = bean.getClass();

            //对于注入过的属性进行重新注入,注入新的动态代理之后的对象
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
