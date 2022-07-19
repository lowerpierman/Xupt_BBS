package com.xupt.nowcoder.controller.inteceptor;

import com.xupt.nowcoder.entity.LoginTicket;
import com.xupt.nowcoder.entity.User;
import com.xupt.nowcoder.service.UserService;
import com.xupt.nowcoder.util.CookieUtil;
import com.xupt.nowcoder.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * preHandler方法主要用来根据loginTicket找User 放入ThreadLocal中
 * post方法主要根据当前的ThreadLocal找User放入要传回前端的loginUser
 * 前端根据loginUser有无来决定用户设置等的显示以及登录注册按钮的显示
 * @Author yzw
 * @Date 2022-07-07 11:20 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(LoginTicketInterceptor.class);
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("handle:"+handler.toString());
        logger.debug("cotextPath:"+request.getContextPath());
        String ticket = CookieUtil.getValue(request,"ticket");
        if(ticket!=null){
            LoginTicket loginTicket = userService.findByTicket(ticket);
            if(loginTicket!=null
                    &&loginTicket.getStatus()==0
                    &&loginTicket.getExpired().after(new Date())){
                    hostHolder.setUser(userService.findUserById(loginTicket.getUserId()));
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        //System.out.println(user.getHeaderUrl());
        if(user!=null && modelAndView!=null){
            modelAndView.addObject("loginUser",user);
            //System.out.println(user.getUsername());

        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
