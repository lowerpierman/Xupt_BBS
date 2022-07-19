package com.xupt.nowcoder.service;

import com.xupt.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @Author yzw
 * @Date 2022-07-14 11:03 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Service
public class LikeService {
    @Autowired
    RedisTemplate redisTemplate;
    public void like(int userId,int entityType,int entityId,int entityUserId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String redisKey = RedisKeyUtil.getEntityKey(entityType,entityId);
                String redisUserKey = RedisKeyUtil.getUserKey(entityUserId);
                boolean isLike = redisTemplate.opsForSet().isMember(redisKey,userId);
                redisOperations.multi();
                if(isLike){
                    redisOperations.opsForSet().remove(redisKey,userId);
                    redisOperations.opsForValue().decrement(redisUserKey);
                    //已点赞 需要取消赞
                }else{
                    //为点赞 要点赞
                    redisOperations.opsForSet().add(redisKey,userId);
                    redisOperations.opsForValue().increment(redisUserKey);
                }
                return redisOperations.exec();
            }
        });

    }

    public Long likeCount(int entityType,int entityId){
        String redisKey = RedisKeyUtil.getEntityKey(entityType,entityId);
        return redisTemplate.opsForSet().size(redisKey);
    }

    public String likeStatus(int userId,int entityType,int entityId){
        String redisKey = RedisKeyUtil.getEntityKey(entityType,entityId);
        if(redisTemplate.opsForSet().isMember(redisKey,userId))
            return  "已赞";
        else
            return "点赞";
    }

    //查询当前用户获赞数
    public Integer likeUserCount(int userId){
        String userKey = RedisKeyUtil.getUserKey(userId);
        return (Integer) redisTemplate.opsForValue().get(userKey);
    }
}
