package com.lijuncai.web.server;

import com.lijuncai.web.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

/**
 * @description: Tomcat服务器
 * @author: lijuncai
 **/
public class TomcatServer {
    private Tomcat tomcat;
    private String[] args;

    public TomcatServer(String[] args) {
        this.args = args;
    }

    /**
     * 启动Tomcat服务器
     *
     * @throws LifecycleException
     */
    public void startServer() throws LifecycleException {
        //创建tomcat，设置监听端口并启动
        tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.start();


        /**
         * 创建Context容器，使用标准实现创建
         * 设置路径
         * 添加生命周期监听器，使用默认的
         */
        Context context = new StandardContext();
        context.setPath("");
        context.addLifecycleListener(new Tomcat.FixContextListener());

        /**
         * 创建DispatcherServlet
         * 将其注册到容器中，并设置支持异步
         */
        DispatcherServlet servlet = new DispatcherServlet();
        Tomcat.addServlet(context, "dispatcherServlet", servlet).setAsyncSupported(true);

        //添加Servlet到根目录的映射
        context.addServletMappingDecoded("/", "dispatcherServlet");
        //context依附在host中，因此将其注册到默认的host容器中
        tomcat.getHost().addChild(context);


        //为了防止服务器退出，需要添加一个常驻线程
        Thread awaitThread = new Thread("tomcat_await_thread") {
            @Override
            public void run() {
                //一直等待，维持tomcat服务的运行
                TomcatServer.this.tomcat.getServer().await();
            }
        };
        awaitThread.setDaemon(false);
        awaitThread.start();
    }
}
