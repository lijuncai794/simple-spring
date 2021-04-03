package com.lijuncai.web.handler;

import com.lijuncai.beans.BeanFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @description: uriӳ��Ĵ�����
 * @author: lijuncai
 **/
public class MappingHandler {
    private String uri;
    private Method method;
    private Class<?> controller;
    private String[] args;

    /**
     * ���崦��
     *
     * @param req Servlet����
     * @param res Servlet��Ӧ
     * @return boolean:�ܷ���
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IOException
     */
    public boolean handle(ServletRequest req, ServletResponse res) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        //��ServletRequestת��ΪHttpServletRequest
        String requestUri = ((HttpServletRequest) req).getRequestURI();
        //�ж�����uri�Ƿ����handler��ͬ����ͬ�򷵻�false
        if (!uri.equals(requestUri)) {
            return false;
        }

        //��uri��ͬ�������method����������ǰ����׼����Ҫ�Ĳ���
        Object[] parameters = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            parameters[i] = req.getParameter(args[i]);
        }

        /**
         * ����controller����
         * Object ctl = controller.newInstance();
         * ��ʵ�ֺ�����ע��֮������ֱ�Ӵ�BeanFactory�л�ȡ����
         */
        Object ctl = BeanFactory.getBean(controller.getName());
        //ʹ�÷����е�invoke()��ִ��Ŀ�귽��
        Object response = method.invoke(ctl, parameters);
        //ʹ��resд����Ӧ���
        res.getWriter().println(response.toString());
        return true;
    }

    /**
     * MappingHandler�Ĺ��캯��
     *
     * @param uri        ӳ���uri
     * @param method     ��Ҫ���õķ���
     * @param controller ������
     * @param args       ��������
     */
    MappingHandler(String uri, Method method, Class<?> controller, String[] args) {
        this.uri = uri;
        this.method = method;
        this.controller = controller;
        this.args = args;
    }
}
