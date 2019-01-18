package com.sheen.pc.service;

import java.util.Map;

/**
 * Created by zxj7044 on 2018-11-8.
 */
public interface ILoginService {
    Map<String, Object> signIn(String username, String password) throws Exception;
    Map<String, Object> signUp(String username, String password) throws Exception;
    Map<String, Object> getToken() throws Exception;
}
