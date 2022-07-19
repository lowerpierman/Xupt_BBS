package com.xupt.nowcoder.controller.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @Author yzw
 * @Date 2022-07-12 17:40 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
/*@Component
@Aspect*/
public class AlphaAspect {
    /*@Pointcut("execution(* com.xupt.nowcoder.service.*.*(..))")
    public void log(){}
    
    @Before("log()")
    public void before(){
        System.out.println("before");
    }
    @After("log()")
    public void after(){
        System.out.println("after");
    }
    @AfterReturning("log()")
    public void afterReturn(){
        System.out.println("return after");
    }
    @AfterThrowing("log()")
    public void afterThrow(){
        System.out.println("afterThrowing");
    }
    @Around("log()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        System.out.println("around before");
        Object obj = proceedingJoinPoint.proceed();
        System.out.println("around after");
        return obj;
    }*/
}
