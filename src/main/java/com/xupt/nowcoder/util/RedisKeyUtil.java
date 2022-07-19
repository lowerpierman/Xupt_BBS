package com.xupt.nowcoder.util;

/**
 * @Author yzw
 * @Date 2022-07-14 11:16 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
public class RedisKeyUtil {
    public static final String SPLIT = ":";
    public static final String ENTITY_LIKE = "like:entity";
    public static final String USER_LIKE = "like:user";
    public static final String USER_FOLLOW = "follow:user";
    public static final String USER_FOLLOWED = "followed:user";
    public static final String USER_KAPTCHA = "user:kaptcha";
    public static final String USER_LOGIN = "user:login";
    public static final String USER_MSG = "user:msg";
    //like:entity:{entityType}:{entityId}   作为一个set集合存放
    public static String getEntityKey(int entityType,int entityId){
        return ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
    //like:user:{userId}   作为一个set集合存放
    public static String getUserKey(int userId){
        return USER_LIKE + SPLIT + userId;
    }
    //follow:user:{userId}
    public static String getFollowKey(int userId){
        return USER_FOLLOW + SPLIT + userId;
    }
    //followed:user:{userId}
    public static String getFollowedKey(int userId){
        return USER_FOLLOWED + SPLIT +userId;
    }
    //user:kaptcha:{userId}
    public static String getKaptcha(String kaptchaOwner){
        return USER_KAPTCHA + SPLIT+kaptchaOwner ;
    }

    //user:login:{userId}
    public static String getUserLogin(String ticket){
        return USER_LOGIN + SPLIT + ticket;
    }
    //user:msg:{userId}
    public static String getUserMsg(int userId){
        return USER_MSG + SPLIT + userId;
    }
}
