package com.fanhl.doujinMoe.exception;

/**
 * Created by fanhl on 15/11/6.
 */
public class NetworkAccessFailException extends Exception {
    public NetworkAccessFailException() {
    }

    public NetworkAccessFailException(String msg, Throwable e) {
        super(msg, e);
    }

    public NetworkAccessFailException(Throwable throwable) {
        super(throwable);
    }
}
