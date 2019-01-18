package com.sheen.pc.util;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.sheen.pc.constant.BaseConstant.MD5;
import static com.sheen.pc.constant.BaseConstant.UTF8;

/**
 * Created by zxj7044 on 2018-11-8.
 */
public class MD5Util {
    private MD5Util(){}

    public static String encrypt(String source) {
        try {
            return encodeMD5(source.getBytes(UTF8));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    private static String encodeMD5(byte[] bytes) {
        try {
            return encodeHex(MessageDigest.getInstance(MD5).digest(bytes));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    private static String encodeHex(byte[] bytes) {
        int length = bytes.length;
        StringBuffer stringBuffer = new StringBuffer(length * 2);
        for (int i = 0; i < length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                stringBuffer.append("0");
            }
            stringBuffer.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return stringBuffer.toString();
    }


}
