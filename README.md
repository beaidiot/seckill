# Java高并发秒杀项目
    1.主要技术栈：Spring + SpringMVC+Mybatis+MySQL
    2.项目结构
    ├─src
    │  ├─main
    │  │  ├─java
    │  │  │  └─com
    │  │  │      └─seckill
    │  │  │          ├─dao
    │  │  │          ├─dto
    │  │  │          ├─entity
    │  │  │          ├─enums
    │  │  │          ├─exception
    │  │  │          ├─service
    │  │  │          │  └─impl
    │  │  │          └─web
    │  │  ├─resources
    │  │  │  ├─mapper
    │  │  │  └─spring
    │  │  ├─sql
    │  │  └─webapp
    │  │      ├─resources
    │  │      │  └─script
    │  │      └─WEB-INF
    │  │          └─jsp
    │  │              └─common
# 难点
    如何高效的解决竞争问题？
        事务+行级锁
# 实现的功能
    1.秒杀接口暴露
    2.执行秒杀
    3.秒杀相关查询
# 编码阶段
## 1.DAO层设计
        1.1.数据库的表设计
        1.2.DAO的接口
        1.3.Mybatis如何去实现DAO
            * xml实现SQL编写
            * Mapper自动实现DAO接口
## 2.Service层设计
        2.1.Service接口设计及编码实现
        2.2.通过Spring去管理Service
        2.3.通过声明式事务简化对事务的控制
            * @Transactional注解
## 3.Web层设计
        3.1.前端交互设计
        3.2.RESTful接口设计
            * RESTful规范
                * GET ——>查询操作
                * POST ——>添加/修改操作
                * PUT ——>修改操作
                * DELETE ——>删除操作
            * 秒杀API的URL设计
                * GET /seckill/list 秒杀列表
                * GET /seckill/{id}/detail 秒杀详情页
                * GET /seckill/time/now 系统时间
                * POST /seckill/{id/exposer 暴露秒杀地址
                * POST /seckill/{id}/{md5}/execution 执行秒杀操作
        3.3.SpringMVC
        3.4.Bootstrap+jQuery
            * CDN的理解
                * CDN（内容分发系统）加速用户获取数据的系统
                * 部署在离用户最近的网络节点上
                * 命中CDN不需要访问后端的服务器
                *互联网公司自己搭建或租用
# 高并发优化
## 1.可能发生高并发的点
    1.详情页
        * 部署到CDN
    2.获取系统时间
        * 单独设计接口
    3.地址暴露接口
        * 无法使用CDN缓存
        * 服务端引入Redis进行缓存优化
        * 一致性维护：超时穿透/主动更新
    4.执行秒杀操作
        * 分析：
            * 无法使用CDN缓存
            * 后端缓存困难：库存问题
            * 一行数据的竞争：热点商品
        * 方案：
            * 原子计数器--->redis/NoSQL
            * 记录行为消息---->分布式MQ
            * 消费消息并落地---->MySQL
## 优化总结
    * 前端控制：暴露接口，按钮防重复
    * 动静态数据分离：CDN缓存，后端缓存
    * 事务竞争优化：减少事务锁持有时间
