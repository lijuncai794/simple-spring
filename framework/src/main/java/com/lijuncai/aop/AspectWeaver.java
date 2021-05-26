package com.lijuncai.aop;

import com.lijuncai.beans.BeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: ����֯����
 * @author: lijuncai
 **/
public class AspectWeaver {

    /**
     * 1.��ȡ���б�ע��@Aspectע�����
     * 2.�������������
     * 3.ɸѡ����Ҫ��̬�������
     * 4.���Խ���Aspect��֯��,���ɶ�̬�������
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
            //�ų�Aspect������
            if (cls.isAnnotationPresent(Aspect.class)) {
                continue;
            }
//            System.out.println(cls.getName());
            //4.ɸѡ����Ҫ�������
            List<AspectInfo> roughMatchedAspectList = collectRoughMatchedAspectListForSpecificClass(aspectInfoList, cls);
            wrapIfNecessary(roughMatchedAspectList, cls);
        }
    }

    /**
     * ������̬�������
     *
     * @param roughMatchedAspectList List<AspectInfo>
     * @param targetClass            Class<?>
     */
    private void wrapIfNecessary(List<AspectInfo> roughMatchedAspectList, Class<?> targetClass) {
        if (roughMatchedAspectList == null || roughMatchedAspectList.size() == 0) {
            return;
        }
        //������̬�������
        AspectListProcessor aspectListExecutor = new AspectListProcessor(targetClass, roughMatchedAspectList);
        Object proxyBean = ProxyCreator.createProxy(targetClass, aspectListExecutor);

//        System.out.println(targetClass.getName() + "�����˴�����" + proxyBean.getClass().getName());
        BeanFactory.setBean(targetClass.getName(), proxyBean);
    }

    /**
     * ɸѡ����Ҫ�������
     *
     * @param aspectInfoList List<AspectInfo> Aspect�����Ϣ�б�
     * @param targetClass    Ŀ����
     * @return
     */
    private List<AspectInfo> collectRoughMatchedAspectListForSpecificClass(List<AspectInfo> aspectInfoList, Class<?> targetClass) {
        List<AspectInfo> roughMatchedAspectList = new ArrayList<>();
        for (AspectInfo aspectInfo : aspectInfoList) {
            //��ɸ
            if (aspectInfo.getPointcutProcessor().roughMatches(targetClass)) {
                roughMatchedAspectList.add(aspectInfo);
            }
        }
        return roughMatchedAspectList;
    }

    /**
     * ƴװAspect��������Ϣ
     *
     * @param aspectList List<Class<?>> Aspect���б�
     * @return List<AspectInfo> Aspect�����Ϣ�б�
     */
    private List<AspectInfo> packAspectInfoList(List<Class<?>> aspectList) {
        List<AspectInfo> aspectInfoList = new ArrayList<>();
        for (Class<?> aspectClass : aspectList) {
            if (verifyAspect(aspectClass)) {
                Aspect aspectTag = aspectClass.getAnnotation(Aspect.class);
                DefaultAspect defaultAspect = (DefaultAspect) BeanFactory.getBean(aspectClass.getName());
                //��ʼ�����ʽ������
                PointcutProcessor pointcutLocator = new PointcutProcessor(aspectTag.pointcut());
                AspectInfo aspectInfo = new AspectInfo(defaultAspect, pointcutLocator);
                aspectInfoList.add(aspectInfo);
            } else {
                //�������Ϲ淶��ֱ���׳��쳣
                throw new RuntimeException("@Aspect must be added to the Aspect class, and Aspect class must extend from DefaultAspect");
            }
        }
        return aspectInfoList;
    }

    /**
     * �ж�Aspect���Ƿ����@Aspectע���Ҽ̳���DefaultAspect.class
     *
     * @param aspectClass Aspect��
     * @return boolean �Ƿ���Ϲ淶
     */
    private boolean verifyAspect(Class<?> aspectClass) {
        return aspectClass.isAnnotationPresent(Aspect.class) &&
                DefaultAspect.class.isAssignableFrom(aspectClass);
    }
}
