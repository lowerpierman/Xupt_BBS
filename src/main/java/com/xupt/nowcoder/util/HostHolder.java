package com.xupt.nowcoder.util;

import com.xupt.nowcoder.entity.User;
import com.xupt.nowcoder.service.UserService;
import org.springframework.stereotype.Component;

/**
 * 用于持有用户信息，代替session存储，避免多线程多用户请求登录情况下 持有用户信息混乱
 * 线程隔离
 * @Author yzw
 * @Date 2022-07-07 12:37 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Component
public class HostHolder {
    private ThreadLocal<User> threadLocal = new ThreadLocal<User>();
    public void setUser(User user){
        threadLocal.set(user);
    }
    public User getUser(){
        return  threadLocal.get();
    }
    public void clear(){
        threadLocal.remove();
    }
}
