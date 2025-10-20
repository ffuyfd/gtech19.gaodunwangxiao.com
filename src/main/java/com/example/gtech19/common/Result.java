package com.example.gtech19.common;

import lombok.Data;

/**
 * 通用响应结果类
 * 统一API返回格式
 */
@Data
public class Result<T> {
    
    /**
     * 状态码
     */
    private int status;
    
    /**
     * 消息
     */
    private String message;
    
    /**
     * 返回数据
     */
    private T result;
    
    private Result() {
    }
    
    /**
     * 成功返回结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setStatus(200);
        result.setMessage("success");
        result.setResult(data);
        return result;
    }
    
    /**
     * 成功返回结果
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setStatus(200);
        result.setMessage(message);
        result.setResult(data);
        return result;
    }
    
    /**
     * 失败返回结果
     */
    public static <T> Result<T> error(int status, String message) {
        Result<T> result = new Result<>();
        result.setStatus(status);
        result.setMessage(message);
        return result;
    }
    
    /**
     * 失败返回结果
     */
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }
}