package com.fanhl.doujinMoe.exception;

/**
 * Created by fanhl on 15/11/8.
 */
public class GetDataFailException extends Exception {
    public GetDataFailException() {
    }

    public GetDataFailException(String detailMessage) {
        super(detailMessage);
    }

    public GetDataFailException(String detailMessage, Throwable e) {
        super(detailMessage, e);
    }

    public GetDataFailException(Throwable throwable) {
        super(throwable);
    }
}
