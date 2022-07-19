package com.xupt.nowcoder.controller.inteceptor;

import com.xupt.nowcoder.annotation.LoginRequired;
import com.xupt.nowcoder.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Interceptor和注解的接口结合实现真正的注解功能
 *
 * @Author yzw
 * @Date 2022-07-08 11:20 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            Method method = ((HandlerMethod) handler).getMethod();
            LoginRequired loginRequired= method.getAnnotation(LoginRequired.class);
            if(loginRequired!=null && hostHolder.getUser()==null){
               response.sendRedirect(request.getContextPath() + "/login");
            }
        }
        return true;
    }
}
