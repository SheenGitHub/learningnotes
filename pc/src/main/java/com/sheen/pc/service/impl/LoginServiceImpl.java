package com.sheen.pc.service.impl;

import com.sheen.pc.constant.SequenceConstant;
import com.sheen.pc.constant.StatusConstant;
import com.sheen.pc.mapper.ICommonMapper;
import com.sheen.pc.mapper.ILoginMapper;
import com.sheen.pc.model.LoginModel;
import com.sheen.pc.model.LoginTicket;
import com.sheen.pc.service.ILoginService;
import com.sheen.pc.service.ILoginTicketService;
import com.sheen.pc.util.MD5Util;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zxj7044 on 2018-11-8.
 */
@Service("loginService")
public class LoginServiceImpl implements ILoginService, InitializingBean, DisposableBean {

    @Resource
    private ILoginMapper loginMapper;

    @Resource
    private ICommonMapper commonMapper;

    @Resource
    private ILoginTicketService loginTicketService;

    @Override
    public Map<String, Object> signIn(String username, String password) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isEmpty(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        LoginModel loginModel = loginMapper.queryByUserName(username);
        if (loginModel == null) {
            map.put("msg", "用户不存在");
            return map;
        }

        if (!MD5Util.encrypt(password).equals(loginModel.getPassword())) {
            map.put("msg", "密码不正确");
            return map;
        }

        loginModel.setLastLoginTime(new Date());
        loginMapper.updateByModel(loginModel);

        String ticket = getLoginTicket(loginModel.getId());
        map.put("ticket", ticket);
        return map;

    }

    @Override
    public Map<String, Object> signUp(String username, String password) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isEmpty(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        LoginModel loginModel = loginMapper.queryByUserName(username);

        if (loginModel != null) {
            map.put("msg", "用户名已被注册");
            return map;
        }

        loginModel = new LoginModel();
        Integer id = commonMapper.getSequence(SequenceConstant.LOGIN_SEQ);
        loginModel.setId(id);
        loginModel.setUsername(username);
        loginModel.setPassword(MD5Util.encrypt(password));
        loginModel.setCreateTime(new Date());
        loginMapper.save(loginModel);

        String ticket = getLoginTicket(loginModel.getId());
        map.put("ticket", ticket);

        return map;
    }

    public String getLoginTicket(Integer userid) throws Exception {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserid(userid);
        Date nowDate = new Date();
        nowDate.setTime(1000 * 60 * 60 * 24 + nowDate.getTime());
        ticket.setExpired(nowDate);
        ticket.setStatus(StatusConstant.VALID);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("_", ""));
        loginTicketService.save(ticket);
        return ticket.getTicket();
    }

    @Override
    public Map<String, Object> getToken() throws Exception {
        return null;
    }

    @PostConstruct
    public void start() {
        System.out.println("Login Service Bean start;");
    }

    @PreDestroy
    public void end() {
        System.out.println("Login Service Bean end;");
    }


    @Override
    public void destroy() throws Exception {
        System.out.println("Login ticket Bean destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Login ticket Bean start");
    }
}
