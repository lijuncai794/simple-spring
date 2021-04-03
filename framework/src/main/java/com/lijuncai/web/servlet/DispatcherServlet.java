package com.lijuncai.web.servlet;

import com.lijuncai.web.handler.HandlerManager;
import com.lijuncai.web.handler.MappingHandler;

import javax.servlet.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @description: �������е�����ַ�
 * @author: lijuncai
 **/
public class DispatcherServlet implements Servlet {

    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    /**
     * ��һ�������͹���ʱ���ж��б��е�handler�ܷ�����������ܴ�������Ӧ���
     *
     * @param req Servlet����
     * @param res Servlet��Ӧ
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        res.getWriter().println("A DispatcherServlet Running On Tomcat");
        for (MappingHandler mappingHandler : HandlerManager.mappingHandlerList) {
            try {
                if (mappingHandler.handle(req, res)) {
                    return;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
