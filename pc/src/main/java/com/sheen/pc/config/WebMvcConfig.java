package com.sheen.pc.config;

import com.sheen.pc.exception.BusinessException;
import com.sheen.pc.exception.ResponseExceptionEnum;
import com.sheen.pc.model.LoginTicket;
import com.sheen.pc.service.ILoginTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import sun.security.pkcs11.P11Util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by zxj7044 on 2018-11-12.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ILoginTicketService loginTicketService;

    @Autowired
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());
        addInterceptor.excludePathPatterns("/api/login/**");
        addInterceptor.addPathPatterns("/api/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            System.out.println("======== request.getCookies():" + request.getCookies());
            System.out.println("======== request.getRequestURL():" + request.getRequestURL());
            String ticket = null;

            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    System.out.println("======== cookie.getName():" + cookie.getName());
                    if (cookie.getName().equals("ticket")) {
                        ticket = cookie.getValue();
                        System.out.println("======== cookie.getValue():" + cookie.getValue());
                    }
                }
            }else {

            }

            if (ticket != null) {
                LoginTicket loginTicket = loginTicketService.queryByTicket(ticket);
                if (loginTicket == null) {
                    throw new BusinessException(ResponseExceptionEnum.TICKET_INVALID.getCode(), "Ticket无效");
                }
                if (loginTicket.getExpired().before(new Date())) {
                    throw new BusinessException(ResponseExceptionEnum.TICKET_INVALID.getCode(), "Ticket 已经过期，请更新Ticket");
                }
                if (loginTicket.getStatus() != 0) {
                    throw new BusinessException(ResponseExceptionEnum.TICKET_INVALID.getCode(), "已注销，请重新登录");
                }
            } else {
                throw new BusinessException(ResponseExceptionEnum.TICKET_INVALID.getCode(), "ticket为空");
            }

            return true;
        }
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setExposeContextBeansAsAttributes(true);
        return resolver;
    }

    @Bean
    public MultipartResolver multipartResolver() throws IOException {
        return new StandardServletMultipartResolver();
    }

}
