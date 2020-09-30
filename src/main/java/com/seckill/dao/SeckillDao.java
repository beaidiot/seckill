package com.seckill.dao;

import com.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SeckillDao {
    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return 如果影响行数>1,表示更新的记录行数
     */
    int reduceINumber(@Param("seckillId") Long seckillId,@Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀商品
     * @param seckillId
     * @return 返回对应秒杀id的商品
     */
    Seckill queryById(@Param("seckillId") Long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return 返回查询的结果列表
     */
    List<Seckill> queryAll(@Param("offset") Integer offset, @Param("limit") Integer limit);
}
