package com.xupt.nowcoder.util;

import org.springframework.stereotype.Component;

/**
 * @Author yzw
 * @Date 2022-07-05 16:55 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Component
public class NowCoderConsist {
    /**
     * 激活成功
     */
    public static int ACTIVATION_SUCCESS = 0;
    /**
     * 重复激活
     */
    public static int ACTIVATION_REPEAT = 1;
    /**
     * 激活失败
     */
    public static int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态的登录凭证的超时时间 12小时
     */
    public static int DEFAULT_EXPIRED_SECONDS = 3600*12;
    /**
     * 记住状态下的登录凭证超时时间
     */
    public static int REMEMBER_EXPIRED_SECONDS = 3600*12*100;
    /**
     * 评论对应实体的类型:帖子
     */
    public static int ENTITY_TYPE_POST = 1;
    /**
     * 对应评论
     */
    public static int ENTITY_TYPE_COMMENT = 2;
}
