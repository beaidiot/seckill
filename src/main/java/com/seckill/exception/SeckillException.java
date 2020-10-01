package com.seckill.exception;

/**
 * 秒杀相关业务异常
 * @Author LvZhang
 * @Date 2020/10/1 13:33
 * @Version 1.0
 */
public class SeckillException extends RuntimeException{

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
