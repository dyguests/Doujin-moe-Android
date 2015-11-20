package com.fanhl.doujinMoe.exception;

import java.io.IOException;

/**
 * Created by fanhl on 15/11/20.
 */
public class GetBookFailException extends IOException {
    public GetBookFailException() {
    }

    public GetBookFailException(String detailMessage) {
        super(detailMessage);
    }

    public GetBookFailException(String detailMessage, Throwable e) {
        super(detailMessage, e);
    }

    public GetBookFailException(Throwable throwable) {
        super(throwable);
    }
}
