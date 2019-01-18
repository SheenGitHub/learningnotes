package com.sheen.pc.controller;

import com.sheen.pc.exception.BusinessException;
import com.sheen.pc.exception.ResponseExceptionEnum;
import com.sheen.pc.service.ILoginService;
import com.sheen.pc.service.ILoginTicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zxj7044 on 2018-11-7.
 */
@Api(value = "登录验证", tags = {"登录操作接口"})
@RequestMapping("/api/login")
@RestController
public class LoginController {

    @Autowired
    private ILoginService loginService;

    @Autowired
    private ILoginTicketService loginTicketService;

    @PostMapping(value = "/signup", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "注册用户", consumes = "application/json")
    public Map<String, Object> signUp(@RequestBody Map<String, Object> params, Errors errors, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> resultmap = new HashMap<>();
        Map<String, Object> map = loginService.signUp(params.get("username").toString(), params.get("password").toString());
        setResponseCookie(response, map);
        resultmap.put("code", ResponseExceptionEnum.SUCCESS.getCode());
        resultmap.put("message", "注册成功");
        resultmap.put("username", params.get("username").toString());

        return resultmap;
    }

    @PostMapping(value = "/signin", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "用户登录", consumes = "application/json")
    public Map<String, Object> signIn(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> resultmap = new HashMap<>();
        Map<String, Object> map = loginService.signIn(params.get("username").toString(), params.get("password").toString());
        setResponseCookie(response, map);
        resultmap.put("code", ResponseExceptionEnum.SUCCESS.getCode());
        resultmap.put("message", "登录成功");
        resultmap.put("username", params.get("username").toString());
        return resultmap;
    }

    private void setResponseCookie(HttpServletResponse response, Map<String, Object> map) {
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        } else {
            throw new BusinessException(ResponseExceptionEnum.USER_NOT_LOGIN.getCode(), map.get("msg").toString());
        }
    }
}
