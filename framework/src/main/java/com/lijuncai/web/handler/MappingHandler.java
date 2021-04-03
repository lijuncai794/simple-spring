package com.lijuncai.web.handler;

import com.lijuncai.beans.BeanFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @description: uri映射的处理类
 * @author: lijuncai
 **/
public class MappingHandler {
    private String uri;
    private Method method;
    private Class<?> controller;
    private String[] args;

    /**
     * 具体处理
     *
     * @param req Servlet请求
     * @param res Servlet响应
     * @return boolean:能否处理
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IOException
     */
    public boolean handle(ServletRequest req, ServletResponse res) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        //将ServletRequest转换为HttpServletRequest
        String requestUri = ((HttpServletRequest) req).getRequestURI();
        //判断请求uri是否与此handler相同，不同则返回false
        if (!uri.equals(requestUri)) {
            return false;
        }

        //若uri相同，则调用method方法；调用前，先准备需要的参数
        Object[] parameters = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            parameters[i] = req.getParameter(args[i]);
        }

        /**
         * 创建controller对象
         * Object ctl = controller.newInstance();
         * 在实现好依赖注入之后，这里直接从BeanFactory中获取对象
         */
        Object ctl = BeanFactory.getBean(controller.getName());
        //使用反射中的invoke()来执行目标方法
        Object response = method.invoke(ctl, parameters);
        //使用res写入响应结果
        res.getWriter().println(response.toString());
        return true;
    }

    /**
     * MappingHandler的构造函数
     *
     * @param uri        映射的uri
     * @param method     需要调用的方法
     * @param controller 控制类
     * @param args       方法参数
     */
    MappingHandler(String uri, Method method, Class<?> controller, String[] args) {
        this.uri = uri;
        this.method = method;
        this.controller = controller;
        this.args = args;
    }
}
