package com.sheen.pc.provider;

import com.sheen.pc.model.User;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

import static org.apache.ibatis.jdbc.SelectBuilder.*;

/**
 * Created by zxj7044 on 2018-10-19.
 */
public class UserProvider {
    private static final String ALL = "*";
    private static final String TABLENAME = "user";


    public String ListUsers(User user){
        boolean isAnd = false;
        BEGIN();
        SELECT(ALL);
        FROM(TABLENAME);
        StringBuilder sb = new StringBuilder("");

        if (user.getId() != null) {
            sb.append("id=#{id} ");
            isAnd = true;
        }

        if (user.getName() != null) {
            System.out.println(user.getName());
            if (isAnd) {
                sb.append("and ");
            }
            sb.append(("name=#{name} "));
        }

        if (user.getAge() != null) {
            if (isAnd) {
                sb.append("and ");
            }
            sb.append(("age=#{age} "));
            isAnd = true;
        }

        if (user.getGender() != null) {
            if (isAnd) {
                sb.append("and ");
            }
            sb.append(("gender=#{gender} "));
            isAnd = true;
        }

        if (user.getRole() != null) {
            if (isAnd) {
                sb.append("and ");
            }
            sb.append(("role=#{role}"));
        }

        SQL sql = new SQL()
                    .SELECT(ALL)
                    .FROM(TABLENAME);

        if (sb.length() > 0) {
            System.out.println(sb);
            sql = sql.WHERE(sb.toString());
        }
        return sql.toString();
    }

    public String findByPage() {
        return new SQL()
                .SELECT(ALL)
                .FROM(TABLENAME)
                .toString();
    }
}
