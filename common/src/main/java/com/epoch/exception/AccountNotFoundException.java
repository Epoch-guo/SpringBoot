package com.epoch.exception;
/**
 * @author zhangsh
 * @version 1.0
 * @date 2020/4/1 10:02
 */
public class AccountNotFoundException extends BaseException {

    public AccountNotFoundException() {
    }

    public AccountNotFoundException(String msg) {
        super(msg);
    }
    
}