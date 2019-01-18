package com.sheen.pc.tip;

import lombok.Data;

/**
 * Created by zxj7044 on 2018-11-6.
 */
@Data
public abstract class BaseTip<Result> {
    protected int code;
    protected String message;
    protected Result result;
}
