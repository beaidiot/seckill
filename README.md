#  Java高并发秒杀项目
    主要技术栈：Spring + SpringMVC+Mybatis+MySQL
## 难点
    如何高效的解决竞争问题？
        事务+行级锁
## 实现的功能
    1.秒杀接口暴露
    2.执行秒杀
    3.秒杀相关查询
## 编码阶段
    1.DAO设计编码
        * 数据库的表设计
        * DAO的接口
        * Mybatis如何去实现DAO
    2.Service设计编码
        * Service接口设计及编码实现
        * 通过Spring去管理Service
        * 通过声明式事务简化对事务的控制
    3.Web设计编码
        * RESTful接口设计
        * 前端交互
    
