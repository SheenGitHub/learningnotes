package com.sheen.pc.service;

import com.github.pagehelper.PageInfo;
import com.sheen.pc.model.User;

import java.util.List;

/**
 * Created by zxj7044 on 2018-10-19.
 */
public interface IUserService {
    List<User> listUsers(User user) throws Exception;
    PageInfo<User> findByPage(int pageNo, int pageSize) throws Exception;
}
