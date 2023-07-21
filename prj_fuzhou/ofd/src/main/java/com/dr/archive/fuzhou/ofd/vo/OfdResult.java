package com.dr.archive.fuzhou.ofd.vo;

/**
 * ofd回调返回结果参数
 *
 * @author dr
 */
public class OfdResult<T> {
    /**
     * 成功
     */
    public static final Integer RET_STATUS_SUCCESS = 0;
    /**
     * 不存在该数据
     */
    public static final Integer RET_STATUS_DATA_NOT_EXISTS = 1;

    /**
     * 系统错误
     */
    public static final Integer RET_STATUS_SERVER_ERROR = 2;
    /**
     * 请求参数为空
     */
    public static final Integer RET_STATUS_NONE_ARGUMENTS = 3;
    /**
     * 返回结果状态码
     */
    private int ret;
    private String message;
    private T data;

    public OfdResult(int ret, String message, T data) {
        this.ret = ret;
        this.message = message;
        this.data = data;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
