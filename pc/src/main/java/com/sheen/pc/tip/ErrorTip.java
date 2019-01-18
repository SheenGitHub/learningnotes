package com.sheen.pc.tip;

/**
 * Created by zxj7044 on 2018-11-6.
 */

public class ErrorTip extends BaseTip {
    /**
     *
     * @param code
     * @param message
     */
    public ErrorTip(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}
