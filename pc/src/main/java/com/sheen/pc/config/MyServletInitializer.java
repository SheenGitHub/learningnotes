package com.sheen.pc.config;

import org.springframework.web.WebApplicationInitializer;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by zxj7044 on 2018-11-19.
 */
public class MyServletInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
//        ServletRegistration.Dynamic myServlet = servletContext.addServlet("myServlet", Myservlet.class);
//        myServlet.addMapping("/custom/**");

//        FilterRegistration.Dynamic filter = servletContext.addFilter("myFilter", MyFilter.class);
//        filter.addMappingForUrlPatterns(null, false, "/custom/*");

    }
}
