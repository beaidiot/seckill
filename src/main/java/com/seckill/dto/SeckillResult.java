package com.seckill.dto;

/**
 * 封装json结果
 * @Author LvZhang
 * @Date 2020/10/2 14:16
 * @Version 1.0
 */

public class SeckillResult<T> {
    private boolean success;
    private T data;
    private String error;

    //成功信息json
    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    //失败信息json
    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
