package com.seckill.dto;

/**
 * 暴露秒杀地址DTO
 *
 * 封装Service返回数据
 * @Author LvZhang
 * @Date 2020/10/1 13:20
 * @Version 1.0
 */
public class Exposer {
    //标记是否开启秒杀
    private boolean exported;
    //加密
    private String md5;

    //秒杀id
    private Long seckillId;

    //系统当前时间(毫秒)
    private Long now;

    //秒杀开启时间
    private Long start;
    //秒杀结束时间
    private Long end;

    public Exposer(boolean exported, String md5, Long seckillId) {
        this.exported = exported;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exported, Long seckillId, Long now, Long start, Long end) {
        this.exported = exported;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(boolean exported, Long seckillId) {
        this.exported = exported;
        this.seckillId = seckillId;
    }

    public boolean isExported() {
        return exported;
    }

    public void setExported(boolean exported) {
        this.exported = exported;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public Long getNow() {
        return now;
    }

    public void setNow(Long now) {
        this.now = now;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "exported=" + exported +
                ", md5='" + md5 + '\'' +
                ", seckillId=" + seckillId +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
