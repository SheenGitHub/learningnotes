package com.sheen.pc.service;

import com.sheen.pc.model.LoginTicket;
import com.sheen.pc.provider.LoginTicketProvider;
import org.apache.ibatis.annotations.InsertProvider;

/**
 * Created by zxj7044 on 2018-11-9.
 */
public interface ILoginTicketService {
    boolean deleteByPrimaryKey(Integer id) throws Exception;

    @InsertProvider(type = LoginTicketProvider.class, method = "save")
    boolean save(LoginTicket record) throws Exception;

    LoginTicket queryByPrimaryKey(Integer id) throws Exception;

    LoginTicket queryByTicket(String ticket) throws Exception;

    boolean updateByPrimaryKey(LoginTicket record) throws Exception;

    boolean updateStatus(String ticket, Integer status) throws Exception;

}
