package com.xupt.nowcoder.controller.aspect;

import com.xupt.nowcoder.entity.User;
import com.xupt.nowcoder.util.HostHolder;
import com.xupt.nowcoder.util.RedisKeyUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author yzw
 * @Date 2022-07-17 16:48 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Component
@Aspect
public class ClearUserAspect {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    RedisTemplate redisTemplate;
    private Logger logger = LoggerFactory.getLogger(UserLoginAspect.class);
    @Pointcut("execution(* com.xupt.nowcoder.dao.UserDao.update*(..))")
    public void log(){}
    @Before("log()")
    public void beforeUpdateUserMsg(){
        //logger.info("访问到该方法了");
        User user = hostHolder.getUser();
        String redisUserKey = RedisKeyUtil.getUserMsg(user.getId());
        redisTemplate.opsForValue().set(redisUserKey,null);
    }
}
