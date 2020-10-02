package com.seckill.service;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.exception.SeckillException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @Author LvZhang
 * @Date 2020/10/1 15:31
 * @Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"
})
public class SeckillServiceTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;


    @Test
    public void getSeckillList() {
        List<Seckill> seckillList = seckillService.getSeckillList();
        logger.info("list={}",seckillList);
    }

    @Test
    public void getById() {
        long id = 1000L;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}",seckill.toString());
    }

    @Test
    public void exportSeckillUrl() {
        long id = 1000L;
        Exposer exposer = seckillService.exposeSeckillUrl(id);
        logger.info("exposer={}",exposer.toString());
        //exposer=Exposer{exported=true, md5='5e5441e56cf6a22a37ffaab541922085', seckillId=1000, now=null, start=null, end=null}
    }

    @Test
    public void executeSeckill() {
        long id = 1000L;
        long userPhone = 13245738819L;
        String md5 = "5e5441e56cf6a22a37ffaab541922085";
        SeckillExecution seckillExecution = seckillService.executeSeckill(id, userPhone, md5);
        logger.info("execution={}",seckillExecution);
        //execution=SeckillExecution{seckillId=1000, state=1, stateInfo='秒杀成功', successSeckilled=SuccessSeckilled{seckillId=1000, userPhone=13245738819, state=0, createTime=Fri Oct 02 00:03:32 CST 2020}}
    }

    @Test
    public void seckillServiceTest(){
        long seckillId = 1000L;
        long userPhone = 13247738818L;
        Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
        if(exposer.isExported()){
            String md5 = exposer.getMd5();
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, md5);
                logger.info("execute={}",seckillExecution);
            }catch (SeckillException re){
                logger.error(re.getMessage());
            }
        }else {
            logger.info("exposer={}",exposer);
        }
    }
}