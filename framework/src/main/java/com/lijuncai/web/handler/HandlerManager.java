package com.lijuncai.web.handler;

import com.lijuncai.web.mvc.Controller;
import com.lijuncai.web.mvc.RequestMapping;
import com.lijuncai.web.mvc.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: MappingHandler的管理类
 * @author: lijuncai
 **/
public class HandlerManager {
    public static List<MappingHandler> mappingHandlerList = new ArrayList<>();

    /**
     * 将被标记了@Controller注解的类筛选出来
     *
     * @param classList 类列表
     */
    public static void resolveMappingHandler(List<Class<?>> classList) {
        for (Class<?> cls : classList) {
            if (cls.isAnnotationPresent(Controller.class)) {//@Controller注解
                parseHandlerFromController(cls);
            }
        }
    }

    /**
     * 解析出MappingHandler,并将其加入容器
     *
     * @param cls 被@Controller注解的类
     */
    private static void parseHandlerFromController(Class<?> cls) {
        //反射获取类别方法
        Method[] methods = cls.getDeclaredMethods();
        //找出被@RequestMapping注解所修饰的方法
        for (Method method : methods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            //获取被@RequestMapping注解的方法的uri
            String uri = method.getDeclaredAnnotation(RequestMapping.class).value();
            //获取被@RequestParam注解的参数
            List<String> paramNameList = new ArrayList<>();
            for (Parameter parameter : method.getParameters()) {
                if (parameter.isAnnotationPresent(RequestParam.class)) {
                    //将参数添加至列表
                    paramNameList.add(parameter.getDeclaredAnnotation(RequestParam.class).value());
                }
            }
            String[] params = paramNameList.toArray(new String[paramNameList.size()]);

            //必要的参数获取完毕之后，开始创建MappingHandler
            MappingHandler mappingHandler = new MappingHandler(uri, method, cls, params);
            //将创建的MappingHandler添加至mappingHandlerList
            HandlerManager.mappingHandlerList.add(mappingHandler);
        }
    }
}
