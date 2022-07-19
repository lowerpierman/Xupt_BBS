package com.xupt.nowcoder.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Author yzw
 * @Date 2022-07-06 14:59 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Configuration
public class KaptchaConfig {
    //核心类 Producer 在这里实现KaptchaConfig实际上是使用了代理模式
    //为DefaultKaptcha扩展了Config 而DefaultKaptcha又是Producer的实现
    @Bean
    public Producer kaptchaProducer(){
        //kaptcha的图形验证码配置
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width","100");
        properties.setProperty("kaptcha.image.hide","40");
        properties.setProperty("kaptcha.textproducer.font.size","32");
        properties.setProperty("kaptcha.textproducer.font.color","0,0,0");
        properties.setProperty("kaptcha.textproducer.char.string","0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        properties.setProperty("kaptcha.textproducer.char.length","5");
        //properties.setProperty("kaptcha.noise.impl","4");
        properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");

        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
