package com.seckill.dao;

import com.seckill.entity.SuccessSeckilled;
import org.apache.ibatis.annotations.Param;

public interface SuccessSeckillDao {

    /**
     * 插入购买明细，可过滤重复
     * @param seckillId
     * @param userPhone
     * @return 插入的行数量
     */
    int insertSuccessSeckkilled(@Param("seckillId") Long seckillId, @Param("userPhone") Long userPhone);


    /**
     * 根据id查询SuccessSeckilled并携带秒杀商品对象实体
     * @param seckillId
     * @return
     */
    SuccessSeckilled queryByIdWithSeckill(@Param("seckillId") Long seckillId,@Param("userPhone") Long userPhone);
}
