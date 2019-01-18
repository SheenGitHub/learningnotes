package com.sheen.pc.provider;

import com.sheen.pc.model.LoginTicket;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by zxj7044 on 2018-11-9.
 */
public class LoginTicketProvider {
    private static final String TABLENAME = "login_ticket";
    private static final String ALL = "*";
    public String save(LoginTicket record) {
        return new SQL()
                .INSERT_INTO(TABLENAME)
                .VALUES("id", "#{id}")
                .VALUES("userid", "#{userid}")
                .VALUES("expired", "#{expired}")
                .VALUES("status", "#{status}")
                .VALUES("ticket", "#{ticket}")
                .toString();

    }

    public String queryByTicket(String ticket) {
        return new SQL()
                .SELECT(ALL)
                .FROM(TABLENAME)
                .WHERE("ticket=#{ticket}")
                .toString();
    }

}
