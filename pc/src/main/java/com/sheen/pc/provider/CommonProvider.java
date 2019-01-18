package com.sheen.pc.provider;

/**
 * Created by zxj7044 on 2018-11-8.
 */
public class CommonProvider {
    public String getSequence(String sequence) {
        return "select nextval('" + sequence + "')";
    }
}
