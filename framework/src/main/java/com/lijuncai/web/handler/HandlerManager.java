package com.lijuncai.web.handler;

import com.lijuncai.web.mvc.Controller;
import com.lijuncai.web.mvc.RequestMapping;
import com.lijuncai.web.mvc.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: MappingHandler�Ĺ�����
 * @author: lijuncai
 **/
public class HandlerManager {
    public static List<MappingHandler> mappingHandlerList = new ArrayList<>();

    /**
     * ���������@Controllerע�����ɸѡ����
     *
     * @param classList ���б�
     */
    public static void resolveMappingHandler(List<Class<?>> classList) {
        for (Class<?> cls : classList) {
            if (cls.isAnnotationPresent(Controller.class)) {//@Controllerע��
                parseHandlerFromController(cls);
            }
        }
    }

    /**
     * ������MappingHandler,�������������
     *
     * @param cls ��@Controllerע�����
     */
    private static void parseHandlerFromController(Class<?> cls) {
        //�����ȡ��𷽷�
        Method[] methods = cls.getDeclaredMethods();
        //�ҳ���@RequestMappingע�������εķ���
        for (Method method : methods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            //��ȡ��@RequestMappingע��ķ�����uri
            String uri = method.getDeclaredAnnotation(RequestMapping.class).value();
            //��ȡ��@RequestParamע��Ĳ���
            List<String> paramNameList = new ArrayList<>();
            for (Parameter parameter : method.getParameters()) {
                if (parameter.isAnnotationPresent(RequestParam.class)) {
                    //������������б�
                    paramNameList.add(parameter.getDeclaredAnnotation(RequestParam.class).value());
                }
            }
            String[] params = paramNameList.toArray(new String[paramNameList.size()]);

            //��Ҫ�Ĳ�����ȡ���֮�󣬿�ʼ����MappingHandler
            MappingHandler mappingHandler = new MappingHandler(uri, method, cls, params);
            //��������MappingHandler�����mappingHandlerList
            HandlerManager.mappingHandlerList.add(mappingHandler);
        }
    }
}
