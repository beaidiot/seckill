package com.seckill.dao.cache;

import com.seckill.dao.SeckillDao;
import com.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @Author LvZhang
 * @Date 2020/10/4 14:26
 * @Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class RedisDaoTest {

    private long id = 1001L;

    @Autowired(required = false)
    private SeckillDao seckillDao;

    @Autowired(required = false)
    private RedisDao redisDao;

    @Test
    public void testSeckill() {
        Seckill seckill = redisDao.getSeckill(id);
        if (seckill==null){
            Seckill seckill1 = seckillDao.queryById(id);
            if(seckill1!=null){
                String result = redisDao.putSeckill(seckill1);
                System.out.println(result);
                Seckill seckill2 = redisDao.getSeckill(id);
                System.out.println(seckill2);
            }
        }
    }


}