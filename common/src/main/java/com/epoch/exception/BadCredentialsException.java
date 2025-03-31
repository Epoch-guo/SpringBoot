package com.epoch.exception;

/**
 * @author zhangyao
 * @date 2020/12/11
 **/

public class BadCredentialsException extends BaseException {

    public BadCredentialsException() {
        super();
    }

    public BadCredentialsException(String message) {
        super(message);
    }
}