package com.seckill.dao;

import com.seckill.entity.SuccessSeckilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @Author LvZhang
 * @Date 2020/9/30 16:01
 * @Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessSeckillDaoTest {

    @Resource
    private SuccessSeckillDao successSeckillDao;

    @Test
    public void insertSuccessSeckkilled() {
        Long seckillId = 1000L;
        Long userPhone = 1234928398L;
        int insertCount = successSeckillDao.insertSuccessSeckkilled(seckillId, userPhone);
        System.out.println("inserCount="+insertCount);
    }

    @Test
    public void queryByIdWithSeckill() {
        Long seckillId = 1000L;
        Long userPhone = 1234928398L;
        SuccessSeckilled successSeckilled = successSeckillDao.queryByIdWithSeckill(seckillId, userPhone);
        System.out.println(successSeckilled);
        System.out.println(successSeckilled.getSeckill());

    }
}