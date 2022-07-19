package com.xupt.nowcoder.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * @Author yzw
 * @Date 2022-07-02 9:34 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Repository
@Primary
//Primary 声明实现相同接口情况下，该类优先被装配
public class AlphaDaoMybatisImpl implements AlphaDao{
    @Override
    public String select() {
        return "Mybatis";
    }
}
