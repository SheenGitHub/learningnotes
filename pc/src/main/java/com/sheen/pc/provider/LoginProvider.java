package com.sheen.pc.provider;

import com.sheen.pc.model.LoginModel;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by zxj7044 on 2018-11-9.
 */
public class LoginProvider {
    private static final String TABLENAME = "login ";
    private static final String ALLFIELD = "*";
    public String queryByUserName(String username) {
        return new SQL()
                    .SELECT(ALLFIELD)
                    .FROM(TABLENAME)
                    .WHERE("USER_NAME = #{username}")
                    .toString();
    }

    public String save(LoginModel record) {
        return new SQL()
                .INSERT_INTO(TABLENAME)
                .VALUES("ID", "#{id}")
                .VALUES("USER_NAME", "#{username}")
                .VALUES("PASSWORD", "#{password}")
                .VALUES("CREATE_TIME", "#{createTime}")
                .toString();
    }

    public String updateByModel(LoginModel record) {
        SQL sql = new SQL().UPDATE(TABLENAME);
        if (record.getPassword() != null) {
            sql.SET("PASSWORD=#{password}");
        }
        if (record.getEmail() != null) {
            sql.SET("EMAIL=#{email}");
        }
        if (record.getLastLoginTime() != null) {
            sql.SET("LAST_LOGIN_TIME=#{lastLoginTime}");
        }
        return sql.toString();
    }
}
