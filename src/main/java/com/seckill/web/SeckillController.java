package com.seckill.web;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.dto.SeckillResult;
import com.seckill.entity.Seckill;
import com.seckill.enums.SeckillStatEnum;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author LvZhang
 * @Date 2020/10/2 13:40
 * @Version 1.0
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    //日志管理
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillService seckillService;

    /**
     * 返回详情页
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        //获取秒杀列表
        List<Seckill> seckillList = seckillService.getSeckillList();
        //将获取列表结果传输到前端
        model.addAttribute("list", seckillList);
        return "list";
    }

    /**
     * 返回查询页面
     *
     * @param model
     * @param seckillId
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        //当秒杀id为空值，重定向到list.jsp页面
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        //秒杀商品不存在时，请求转发到list.jsp页面
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        //秒杀商品存在时，跳转到详情页detail.jsp
        model.addAttribute("seckill", seckill);
        return "detail";
    }


    /**
     * ajax接口，返回秒杀网址
     *
     * @param seckillId
     * @return json
     */
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;

        try {
            //当前时间段在秒杀时间段内
            Exposer exposer = seckillService.exposeSeckillUrl(seckillId);
            //给出秒杀地址
            result = new SeckillResult<>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //不在秒杀时间段内
            result = new SeckillResult<>(false, e.getMessage());
        }
        return result;
    }


    /**
     * 执行秒杀接口
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    @RequestMapping(value = "/{seckillId}/{md5}/executin",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("userPhone") Long userPhone,
                                                   @CookieValue(value = "killPhone", required = false) String md5) {
        //电话号码为空，返回“未注册”结果信息json
        if (userPhone == null) {
            return new SeckillResult<SeckillExecution>(false, "未注册");
        }

        try {
            //执行秒杀操作
            SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, md5);
            //执行成功，则返回成功信息json
            return new SeckillResult<SeckillExecution>(true, seckillExecution);
        } catch (RepeatKillException e) {
            //重复秒杀操作，返回相关操作异常json
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(false, seckillExecution);
        } catch (SeckillCloseException e) {
            //秒杀已结束，返回相关操作异常json
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecution>(false, seckillExecution);
        } catch (Exception e) {
            //业务逻辑异常，返回“内部异常”json
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(false, seckillExecution);
        }
    }

    /**
     * 获取系统时间
     *
     * @return
     */
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }
}
