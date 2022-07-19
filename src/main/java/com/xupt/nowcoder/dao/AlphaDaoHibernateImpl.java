package com.xupt.nowcoder.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

/**
 * @Author yzw
 * @Date 2022-07-02 9:31 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Repository("alphaHibernate")
public  class AlphaDaoHibernateImpl implements AlphaDao {
    @Override
    public String select() {
        return "Hibernate";
    }
}
