package com.epoch.exception;

/**
 * @author zhangyao
 * @date 2020/07/07
 **/
public class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

}