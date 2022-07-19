package com.xupt.nowcoder.service;

import com.xupt.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;
import java.text.SimpleDateFormat;

/**
 * @Author yzw
 * @Date 2022-07-14 16:45 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Service
public class FollowService {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    SimpleDateFormat simpleDateFormat;
    @Autowired
    UserService userService;
    //设计思路
    //每一个用户都有两个ZSet 一个是自己关注的(follow:user)   另一个是被自己关注的(followed:user)
    public void follow(int userId,int followedUserId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                //要关注用户的 KeySet
                String followUser = RedisKeyUtil.getFollowKey(userId);
                //被关注用户的 KeySet
                String followedUser = RedisKeyUtil.getFollowedKey(followedUserId);
                Object obj = redisOperations.opsForZSet().score(followUser,followedUserId);
                redisOperations.multi();
                if(obj!=null){
                    //取关
                    redisOperations.opsForZSet().remove(followUser,followedUserId);
                    redisOperations.opsForZSet().remove(followedUser,userId);
                }else{
                    //关注
                    redisOperations.opsForZSet().add(followUser,followedUserId,System.currentTimeMillis());
                    redisOperations.opsForZSet().add(followedUser,userId,System.currentTimeMillis());
                }
                return redisOperations.exec();
            }
        });
    }
    //当前用户关注数
    public Long followCount(int userId){
        String userKey = RedisKeyUtil.getFollowKey(userId);
        return redisTemplate.opsForZSet().zCard(userKey);
    }
    //当前用户被关注数
    public Long followedCount(int userId){
        String followedKey = RedisKeyUtil.getFollowedKey(userId);
        return redisTemplate.opsForZSet().zCard(followedKey);
    }
    //当前用户对其他用户的状态
    public String followStatus(int userId,int followedId){
        String userKey = RedisKeyUtil.getFollowKey(userId);
        if(redisTemplate.opsForZSet().score(userKey,followedId)!=null){
            return "取关";
        }else{
            return "关注";
        }
    }
    //当前用户关注的人的idlist
    public List<Map<String,Object>> followList(int userId, int offset, int limit){
        //获取
        List<Map<String,Object>> res = new ArrayList<>();
        String userKey = RedisKeyUtil.getFollowKey(userId);
        Set<Integer> followList = redisTemplate.opsForZSet().reverseRange(userKey,offset,offset+limit-1);
        if(followList!=null){
            //要存入   关注的 用户信息、关注时间、关注状态
            for(int targetId:followList){
                Map<String,Object> map = new HashMap<>();
                map.put("user",userService.findUserById(targetId));
                map.put("followTime",new Date(redisTemplate.opsForZSet().score(userKey,targetId).longValue()));
                map.put("followStatus","关注TA");
                res.add(map);
            }
        }
        return res;
    }
    //当前用户被关注的idlist
    public List<Map<String,Object>> followedList(int userId, int offset, int limit){
        //获取
        List<Map<String,Object>> res = new ArrayList<>();
        String followedKey = RedisKeyUtil.getFollowedKey(userId);
        Set<Integer> followList = redisTemplate.opsForZSet().reverseRange(followedKey,offset,offset+limit-1);
        if(followList!=null){
            //要存入   关注的 用户信息、关注时间、关注状态
            for(int targetId:followList){
                Map<String,Object> map = new HashMap<>();
                map.put("user",userService.findUserById(targetId));
                map.put("followTime",new Date(redisTemplate.opsForZSet().score(followedKey,targetId).longValue()));
                map.put("followStatus","关注TA");
                res.add(map);
            }
        }
        return res;
    }
}
