package com.sheen.pc.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sheen.pc.mapper.IUserMapper;
import com.sheen.pc.model.User;
import com.sheen.pc.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by zxj7044 on 2018-10-19.
 */
@Service("userService")
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService{

    @Resource
    private IUserMapper userMapper;

    @Override
    public List<User> listUsers(User user) throws Exception {
        return userMapper.list(user);
    }

    @Override
    public PageInfo<User> findByPage(int pageNo, int pageSize) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        return new PageInfo<>(userMapper.findByPage());
    }
}
