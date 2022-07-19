package com.xupt.nowcoder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * @Author yzw
 * @Date 2022-07-02 10:48 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Configuration
//用来声明配置类，也继承了Component注解
public class AlphaConfig {
    @Bean
    //声明装配Bean的方法
    public SimpleDateFormat SimpleDataFormat(){
        return new SimpleDateFormat("yyyy:MM:dd,HH-mm-ss");
    }
}
