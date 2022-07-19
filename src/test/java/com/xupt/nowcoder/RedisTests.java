package com.xupt.nowcoder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author yzw
 * @Date 2022-07-13 17:27 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NowCoderApplication.class)
public class RedisTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void redisTestStructrue(){
        redisTemplate.opsForValue().set("yzw",111);
        redisTemplate.opsForHash().put("test:map","yzw",1234);
        redisTemplate.opsForHash().put("test:map","yzw1",12346);
        redisTemplate.opsForHash().put("test:map","yzw2",12347);
        redisTemplate.opsForZSet().add("test:zset","yzwzset",1243434);

    }

    @Test
    public void redisTestTran(){
        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String redisKey = "test:trans1";
                redisOperations.multi();
                redisOperations.opsForSet().add(redisKey,"111","222","333","444");
                System.out.println(redisOperations.opsForSet().members(redisKey));
                return redisOperations.exec();
            }
        });
        System.out.println(obj);
    }
}
