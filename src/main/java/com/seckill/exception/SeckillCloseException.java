package com.seckill.exception;

import com.seckill.dto.SeckillExecution;

/**
 * 秒杀关闭异常
 * @Author LvZhang
 * @Date 2020/10/1 13:32
 * @Version 1.0
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }


}
