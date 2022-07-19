package com.xupt.nowcoder.controller.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author yzw
 * @Date 2022-07-12 17:50 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Component
@Aspect
public class UserLoginAspect {
   @Pointcut("execution(* com.xupt.nowcoder.service.*.*(..))")
   public void login(){};
    private Logger logger = LoggerFactory.getLogger(UserLoginAspect.class);

   @Before("login()")
    public void before(JoinPoint joinPoint){
       //[]用户在2022年7月12日17:52:55访问了[类.方法()]
       ServletRequestAttributes requestAttributes =  (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
       HttpServletRequest request = requestAttributes.getRequest();
       String ip = request.getRemoteHost();
       String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
       String path = joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
       logger.info("用户"+ip+"在"+now+"访问了"+path);
   }
}
