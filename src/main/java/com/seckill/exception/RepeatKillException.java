package com.seckill.exception;

/**
 * 重复秒杀异常----运行期异常
 *
 * @Author LvZhang
 * @Date 2020/10/1 13:30
 * @Version 1.0
 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
