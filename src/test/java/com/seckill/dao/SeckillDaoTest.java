package com.seckill.dao;

import com.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author LvZhang
 * @Date 2020/9/30 15:04
 * @Version 1.0
 *
 * 配置spring和junit整合，Junit启动时加载SpringIOC容器
 * spring-test
 * junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入DAO实现类依赖
    @Resource
    SeckillDao seckillDao;

    @Test
    public void reduceINumber() {
        long seckillId = 1000L;
        Date killTime = new Date();
        int reduceCount = seckillDao.reduceINumber(seckillId, killTime);
        System.out.println("reduceCount="+reduceCount);

    }

    @Test
    public void queryById() {
        long id = 1000L;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill);
    }

    @Test
    public void queryAll() {
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        for (Seckill sk : seckills) {
            System.out.println(sk);
        }
    }
}