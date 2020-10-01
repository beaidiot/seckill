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
        3.1.RESTful接口设计
        3.2.前端交互
    
