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
    │  │  │          └─service
    │  │  │              └─impl
    │  │  ├─resources
    │  │  │  ├─mapper
    │  │  │  └─spring
    │  │  ├─sql
    │  │  └─webapp
    │  │      └─WEB-INF
# 难点
    如何高效的解决竞争问题？
        事务+行级锁
# 实现的功能
    1.秒杀接口暴露
    2.执行秒杀
    3.秒杀相关查询
# 编码阶段
## 1.DAO层设计编码
        1.1.数据库的表设计
        1.2.DAO的接口
        1.3.Mybatis如何去实现DAO
            * xml实现SQL编写
            * Mapper自动实现DAO接口
## 2.Service层设计编码
        2.1.Service接口设计及编码实现
        2.2.通过Spring去管理Service
        2.3.通过声明式事务简化对事务的控制
            * @Transactional注解
### 3.Web设计编码
        3.1.前端交互设计
        3.2.RESTful接口设计
            * Restful规范
                * GET——>查询操作
                * POST——>添加/修改操作
                * PUT——>修改操作
                * DELETE——>删除操作
            * URL设计
                */模块/资源/{标示}/集合1/...
                    eg：/user/{uid}/friends ——> 好友列表
                *秒杀API的URL设计
                    #1.GET /seckill/list -->秒杀列表
                    #2.GET /seckill/{id}/detail -->详情页
                    #3.GET /seckill/time/now -->系统时间
                    #4.POST /seckill/{id}/exposer -->暴露秒杀页面
                    #5.POST /seckill/{id}/{md5}/execution -->执行秒杀操作
        3.3.SpringMVC框架
            * 
        3.4.bootStrap+jQuery
            *页面布局和样式控制
            *数据交互实现
    
