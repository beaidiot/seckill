package com.seckill.service.impl;

import com.seckill.dao.SeckillDao;
import com.seckill.dao.SuccessSeckillDao;
import com.seckill.dao.cache.RedisDao;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.entity.SuccessSeckilled;
import com.seckill.enums.SeckillStatEnum;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillException;
import com.seckill.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author LvZhang
 * @Date 2020/10/1 13:46
 * @Version 1.0
 */

@Service
public class SeckillServiceImpl implements SeckillService {

    //日志
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //md5盐值字符串，用于混淆MD5
    private final String slat = "jaiwsueoi12i3o*&u^9123kjlkas56yh1y23g";
    //注入service依赖
    @Autowired(required = false)
    private SeckillDao seckillDao;

    @Autowired(required = false)
    private SuccessSeckillDao successSeckillDao;

    @Autowired(required = false)
    private RedisDao redisDao;

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 5);
    }

    @Override
    public Seckill getById(Long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exposeSeckillUrl(Long seckillId) {
        //优化点：缓存优化，在超时缓存的基础上维护一致性
        //1.访问Redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            //2.访问数据库
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                //3.放入redis
                redisDao.putSeckill(seckill);
            }
        }

        //获取seckillId商品的秒杀开始时间
        Date startTime = seckill.getStartTime();
        //获取seckillId商品的秒杀结束时间
        Date endTime = seckill.getEndTime();
        //获取系统当前时间
        Date nowTime = new Date();
        //判断时间是否处于秒杀时间段内
        //当前时间在秒杀时间之外，则返回商品秒杀的开始时间和结束时间
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        //生成seckillID的md5
        String md5 = getMD5(seckillId);
        //当前时间在秒杀时间段内，则后续返回秒杀地址
        return new Exposer(true, md5, seckillId);
    }

    @Override
    @Transactional
    /**
     * 使用注解方式的有点：
     * 1.开发团队达成一致约定，明确标注事务方法的风格
     * 2.保证事务方法的执行时间尽可能的短，不要穿插其他网络操作，如RPC/http请求等，若需要，可将相关操作剥离到事务方法外部；
     * 3.不是所有的方法都需要事务控制，如只有一条修改操作、只读操作等不需要事务控制；
     */
    public SeckillExecution executeSeckill(Long seckillId, Long userPhone, String md5) throws SeckillException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑：减库存+记录购买行为
        Date nowTime = new Date();
        //减库存
        int updateCount = seckillDao.reduceINumber(seckillId, nowTime);

        System.out.println("updatecount=" + updateCount);

        try {
            if (updateCount <= 0) {
                //没有更新到记录，秒杀结束
                throw new SeckillException("seckill is closed");
            } else {
                //记录购买行为
                int inserCount = successSeckillDao.insertSuccessSeckkilled(seckillId, userPhone);

                if (inserCount <= 0) {
                    //重复秒杀
                    throw new RepeatKillException("seckill repeated");
                } else {
                    //秒杀成功
                    SuccessSeckilled successSeckilled = successSeckillDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successSeckilled);
                }
            }
        } catch (RepeatKillException re) {
            throw re;
        } catch (SeckillCloseException sce) {
            throw sce;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            //所有编译器异常。转化为运行期异常
            throw new SeckillException("Seckill inner error" + ex.getMessage());
        }

    }

    /**
     * 使用数据库的存储过程执行秒杀操作
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */
    @Override
    public SeckillExecution executeSeckillProcedure(Long seckillId, Long userPhone, String md5) {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("Seckill was REWRITE");
        }
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);
        try {
            seckillDao.killByProcedure(map);
            //获取result
            int result = MapUtils.getInteger(map, "result");
            if (result == 1) {
                SuccessSeckilled successSeckilled = successSeckillDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successSeckilled);
            } else {
                return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }
    }

    /**
     * 生成md5
     *
     * @param seckillId
     * @return
     */
    private String getMD5(Long seckillId) {
        String base = seckillId + "/" + slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    private String hello(){
        return "hello,feature-b";
    }

}
