package com.dr.archive.fuzhou.approve.exception;

/**
 * 归档相关异常
 * 对外异常应该相对友好，提示的异常信息应该都是相对明确的
 *
 * @author dr
 */
public class ReceiveException extends RuntimeException {
    public ReceiveException() {
    }

    public ReceiveException(String message) {
        super(message);
    }

    public ReceiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReceiveException(Throwable cause) {
        super(cause);
    }

    public ReceiveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
