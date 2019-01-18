package com.sheen.pc.aop;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by zxj7044 on 2018-11-13.
 */
@Component
public class MickeyBeanNameAware implements BeanNameAware {
    @Override
    public void setBeanName(String s) {
        //System.out.println("BeanName Aware:"+s);
    }
}
