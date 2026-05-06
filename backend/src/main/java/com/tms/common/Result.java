package com.tms.common;

/**
 * 统一响应结果类
 *
 * @param <T> 数据类型
 * @author TMS Team
 * @version 1.0.0
 */
public class Result<T> {

    /**
     * 响应码：成功
     */
    public static final int SUCCESS = 200;

    /**
     * 响应码：失败
     */
    public static final int ERROR = 500;

    /**
     * 响应码：参数错误
     */
    public static final int BAD_REQUEST = 400;

    /**
     * 响应码：未授权
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 响应码：禁止访问
     */
    public static final int FORBIDDEN = 403;

    /**
     * 响应码：资源不存在
     */
    public static final int NOT_FOUND = 404;

    private int code;
    private String message;
    private T data;
    private long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应
     *
     * @return 结果对象
     */
    public static <T> Result<T> success() {
        return new Result<>(SUCCESS, "操作成功", null);
    }

    /**
     * 成功响应（带数据）
     *
     * @param data 响应数据
     * @return 结果对象
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS, "操作成功", data);
    }

    /**
     * 成功响应（带消息和数据）
     *
     * @param message 响应消息
     * @param data    响应数据
     * @return 结果对象
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(SUCCESS, message, data);
    }

    /**
     * 失败响应
     *
     * @param message 错误消息
     * @return 结果对象
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(ERROR, message, null);
    }

    /**
     * 失败响应（带状态码）
     *
     * @param code    状态码
     * @param message 错误消息
     * @return 结果对象
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
