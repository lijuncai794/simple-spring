package com.lijuncai.web.server;

import com.lijuncai.web.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

/**
 * @description: Tomcat������
 * @author: lijuncai
 **/
public class TomcatServer {
    private Tomcat tomcat;
    private String[] args;

    public TomcatServer(String[] args) {
        this.args = args;
    }

    /**
     * ����Tomcat������
     *
     * @throws LifecycleException
     */
    public void startServer() throws LifecycleException {
        //����tomcat�����ü����˿ڲ�����
        tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.start();


        /**
         * ����Context������ʹ�ñ�׼ʵ�ִ���
         * ����·��
         * ����������ڼ�������ʹ��Ĭ�ϵ�
         */
        Context context = new StandardContext();
        context.setPath("");
        context.addLifecycleListener(new Tomcat.FixContextListener());

        /**
         * ����DispatcherServlet
         * ����ע�ᵽ�����У�������֧���첽
         */
        DispatcherServlet servlet = new DispatcherServlet();
        Tomcat.addServlet(context, "dispatcherServlet", servlet).setAsyncSupported(true);

        //���Servlet����Ŀ¼��ӳ��
        context.addServletMappingDecoded("/", "dispatcherServlet");
        //context������host�У���˽���ע�ᵽĬ�ϵ�host������
        tomcat.getHost().addChild(context);


        //Ϊ�˷�ֹ�������˳�����Ҫ���һ����פ�߳�
        Thread awaitThread = new Thread("tomcat_await_thread") {
            @Override
            public void run() {
                //һֱ�ȴ���ά��tomcat���������
                TomcatServer.this.tomcat.getServer().await();
            }
        };
        awaitThread.setDaemon(false);
        awaitThread.start();
    }
}
