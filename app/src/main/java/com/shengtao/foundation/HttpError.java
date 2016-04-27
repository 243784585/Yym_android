package com.shengtao.foundation;

/**
 * @copyright：Copyright © 2015 - 2020. All Rights Reserved
 * @author：dane55
 * @email：dane8789@gmail.com
 * @date： 15/9/16 09:57
 * @package： com.motu.foundation
 * @classname： HttpError.java
 * @description： 对HttpError.java类的功能描述
 */
public class HttpError {

    /**
     * 网络链接错误
     */
    public static final byte ERROR_NETWORD = 1;

    /**
     * http链接状态非法错误，如500,400等
     */
    public static final byte ERROR_STATUSCODE = 2;

    /**
     * http链接超时
     */
    public static final byte ERROR_TIMEOUT = 3;

    /**
     * io流读写错误
     */
    public static final byte ERROR_IO = 4;

    /**
     * socket错误
     */
    public static final byte ERROR_SOCKET = 5;

    /**
     * 返回结果data出错
     */
    public static final byte ERROR_DATA_TRAN = 6;

    /**
     * 其他错误
     */
    public static final byte ERROR_OTHER = 7;

    private int mErrorCode;
    private Exception mException;

    public HttpError(int errorCode, Exception exception) {
        this.mErrorCode = errorCode;
        this.mException = exception;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(int code) {
        this.mErrorCode = code;
    }

    public Exception getException() {
        return mException;
    }

    public void setException(Exception exception) {
        this.mException = exception;
    }
}
