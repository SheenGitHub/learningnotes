package com.sheen.pc.config;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by zxj7044 on 2018-11-20.
 */
public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(rawPassword.toString());
    }
}
