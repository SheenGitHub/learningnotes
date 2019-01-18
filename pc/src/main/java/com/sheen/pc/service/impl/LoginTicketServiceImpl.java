package com.sheen.pc.service.impl;

import com.sheen.pc.constant.SequenceConstant;
import com.sheen.pc.mapper.ICommonMapper;
import com.sheen.pc.mapper.ILoginTicketMapper;
import com.sheen.pc.model.LoginTicket;
import com.sheen.pc.service.ILoginTicketService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zxj7044 on 2018-11-9.
 */
@Service("loginTicketService")
public class LoginTicketServiceImpl implements ILoginTicketService{
    @Resource
    private ICommonMapper commonMapper;

    @Resource
    private ILoginTicketMapper loginTicketMapper;

    @Override
    public boolean deleteByPrimaryKey(Integer id) throws Exception {
        return false;
    }

    @Override
    public boolean save(LoginTicket record) throws Exception {
        boolean flag = false;
        Integer id = commonMapper.getSequence(SequenceConstant.LOGIN_TICKET_SEQ);
        int i = loginTicketMapper.save(record);
        if (i > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public LoginTicket queryByPrimaryKey(Integer id) throws Exception {
        return null;
    }

    @Override
    public LoginTicket queryByTicket(String ticket) throws Exception {
        return loginTicketMapper.queryByTicket(ticket);
    }

    @Override
    public boolean updateByPrimaryKey(LoginTicket ticket) throws Exception {
        return false;
    }

    @Override
    public boolean updateStatus(String ticket, Integer status) throws Exception {
        return false;
    }

}
