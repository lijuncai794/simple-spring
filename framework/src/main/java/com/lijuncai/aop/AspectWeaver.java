package com.lijuncai.aop;

import com.lijuncai.beans.BeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 切面织入类
 * @author: lijuncai
 **/
public class AspectWeaver {

    /**
     * 1.获取所有标注了@Aspect注解的类
     * 2.遍历容器里的类
     * 3.筛选出需要动态代理的类
     * 4.尝试进行Aspect的织入,生成动态代理对象
     */
    public void doAop() {

        List<Class<?>> aspectList = BeanFactory.getClassesOfAspect();
        if (aspectList == null || aspectList.size() == 0) {
            System.out.println("list==null");
            return;
        }

        List<AspectInfo> aspectInfoList = packAspectInfoList(aspectList);

        List<Class<?>> classList = BeanFactory.getClassList();
        for (Class<?> cls : classList) {
            //排除Aspect类自身
            if (cls.isAnnotationPresent(Aspect.class)) {
                continue;
            }
//            System.out.println(cls.getName());
            //4.筛选出需要代理的类
            List<AspectInfo> roughMatchedAspectList = collectRoughMatchedAspectListForSpecificClass(aspectInfoList, cls);
            wrapIfNecessary(roughMatchedAspectList, cls);
        }
    }

    /**
     * 创建动态代理对象
     *
     * @param roughMatchedAspectList List<AspectInfo>
     * @param targetClass            Class<?>
     */
    private void wrapIfNecessary(List<AspectInfo> roughMatchedAspectList, Class<?> targetClass) {
        if (roughMatchedAspectList == null || roughMatchedAspectList.size() == 0) {
            return;
        }
        //创建动态代理对象
        AspectListProcessor aspectListExecutor = new AspectListProcessor(targetClass, roughMatchedAspectList);
        Object proxyBean = ProxyCreator.createProxy(targetClass, aspectListExecutor);

//        System.out.println(targetClass.getName() + "生成了代理类" + proxyBean.getClass().getName());
        BeanFactory.setBean(targetClass.getName(), proxyBean);
    }

    /**
     * 筛选出需要代理的类
     *
     * @param aspectInfoList List<AspectInfo> Aspect类的信息列表
     * @param targetClass    目标类
     * @return
     */
    private List<AspectInfo> collectRoughMatchedAspectListForSpecificClass(List<AspectInfo> aspectInfoList, Class<?> targetClass) {
        List<AspectInfo> roughMatchedAspectList = new ArrayList<>();
        for (AspectInfo aspectInfo : aspectInfoList) {
            //粗筛
            if (aspectInfo.getPointcutProcessor().roughMatches(targetClass)) {
                roughMatchedAspectList.add(aspectInfo);
            }
        }
        return roughMatchedAspectList;
    }

    /**
     * 拼装Aspect类的相关信息
     *
     * @param aspectList List<Class<?>> Aspect类列表
     * @return List<AspectInfo> Aspect类的信息列表
     */
    private List<AspectInfo> packAspectInfoList(List<Class<?>> aspectList) {
        List<AspectInfo> aspectInfoList = new ArrayList<>();
        for (Class<?> aspectClass : aspectList) {
            if (verifyAspect(aspectClass)) {
                Aspect aspectTag = aspectClass.getAnnotation(Aspect.class);
                DefaultAspect defaultAspect = (DefaultAspect) BeanFactory.getBean(aspectClass.getName());
                //初始化表达式处理器
                PointcutProcessor pointcutLocator = new PointcutProcessor(aspectTag.pointcut());
                AspectInfo aspectInfo = new AspectInfo(defaultAspect, pointcutLocator);
                aspectInfoList.add(aspectInfo);
            } else {
                //若不符合规范则直接抛出异常
                throw new RuntimeException("@Aspect must be added to the Aspect class, and Aspect class must extend from DefaultAspect");
            }
        }
        return aspectInfoList;
    }

    /**
     * 判断Aspect类是否添加@Aspect注解且继承了DefaultAspect.class
     *
     * @param aspectClass Aspect类
     * @return boolean 是否符合规范
     */
    private boolean verifyAspect(Class<?> aspectClass) {
        return aspectClass.isAnnotationPresent(Aspect.class) &&
                DefaultAspect.class.isAssignableFrom(aspectClass);
    }
}
