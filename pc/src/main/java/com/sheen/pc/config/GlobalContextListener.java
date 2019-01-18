package com.sheen.pc.config;

import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by zxj7044 on 2018-11-7.
 */
@WebListener
@Component
public class GlobalContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("启动了");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("结束了");
    }

}
