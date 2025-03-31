package com.epoch.exception;
/**
 * @author zhangyuanfeng@xiaomi.com
 * @version 2019-12-03
 */
public class AccessDeniedException extends BaseException{
    public AccessDeniedException() {
    }

    public AccessDeniedException(String msg) {
        super(msg);
    }
}
