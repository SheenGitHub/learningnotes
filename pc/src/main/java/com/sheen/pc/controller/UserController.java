package com.sheen.pc.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sheen.pc.exception.BusinessException;
import com.sheen.pc.exception.ResponseExceptionEnum;
import com.sheen.pc.exception.ServiceException;
import com.sheen.pc.model.ListCond;
import com.sheen.pc.model.User;
import com.sheen.pc.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxj7044 on 2018-10-19.
 */
@Api(value = "/user", tags="测试查询接口")
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "展示列表", notes = "展示查询的结果列表")
    @GetMapping(value = "/list")
    public List<User> listUser(@Valid @ModelAttribute User user) throws Exception {
        System.out.println(user);
        List<User> result = null;
        try {
            result = userService.listUsers(user);
        } catch (Exception e) {
            throw new BusinessException(ResponseExceptionEnum.FAILURE);
        }

        return result;
    }

    @ApiOperation(value = "分页展示列表", notes = "展示查询的结果列表")
    @GetMapping(value = "/listby")
    public PageInfo<User> listUserBy(@Valid @ModelAttribute ListCond listCond) throws Exception {
        System.out.println(listCond);
        PageInfo<User> result = null;
        try {
            result = userService.findByPage(listCond.getPageNum(), listCond.getPageSize());
        } catch (Exception e) {
            throw new BusinessException(ResponseExceptionEnum.FAILURE);
        }

        return result;
    }


}
