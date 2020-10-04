-- 秒杀执行存储过程
DELIMITER $$ -- console ;转换为 $$
-- 定义存储过程
-- 参数：in 输入参数；out 输出参数
-- ROW_COUNT()：返回上一条修改类型sql(DELETE,INSERT,UPDATE)的影响行数
-- ROW_COUNT()：0:表示未修改数据，>0:表示修改的行数；<0:sql错误/未执行修改sql

CREATE PROCEDURE seckill.execute_seckill(
    IN v_seckill_id BIGINT,
    IN v_phone BIGINT,
    IN v_kill_time TIMESTAMP,
    OUT r_result INT
)
BEGIN
    DECLARE insert_count INT DEFAULT 0;
    START TRANSACTION;
    INSERT IGNORE INTO success_killed(seckill_id,user_phone,create_time,state)
    VALUES(v_seckill_id,v_phone,v_kill_time,0);
    SELECT ROW_COUNT() INTO insert_count;
    IF(insert_count=0) THEN -- 插入影响结果为0，则说明是重复秒杀，返回-1；
        ROLLBACK;
        SET r_result = -1;
    ELSEIF(insert_count<0) THEN-- 插入影响结果小于0，则说明内部错误，返回-2；
        ROLLBACK;
        SET r_result = -2;
    ELSE -- 插入影响结果大于0，则说明插入操作成功，进行update减库存操作
        UPDATE seckill
        SET number = number - 1
        WHERE seckill_id = v_seckill_id
          AND end_time > v_kill_time
          AND start_time < v_kill_time
          AND number > 0;
        SELECT ROW_COUNT() INTO insert_count;
        IF(insert_count=0) THEN -- 减库存操作影响结果为0，说明秒杀已结束
            ROLLBACK;
            SET r_result = 0;
        ELSEIF(insert_count<0) THEN -- 减库存操作影响结果小于0，说明产生操作异常
            ROLLBACK;
            SET r_result = -2;
        ELSE -- 减库存操作影响结果大于0，说明减库存成功，返回1；
            COMMIT;
            SET r_result = 1;
        END IF;
    END IF;
END;
$$
-- 存储过程定义结束
DELIMITER ;

-- 测试
SET @r_result = -3;
CALL execute_seckill(1011,13144571382,NOW(),@r_result);
SELECT @r_result;
