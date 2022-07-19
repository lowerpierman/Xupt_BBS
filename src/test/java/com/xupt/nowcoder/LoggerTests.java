package com.xupt.nowcoder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;

/**
 * @Author yzw
 * @Date 2022-07-05 10:03 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NowCoderApplication.class)
public class LoggerTests {


    private static final Logger logger = LoggerFactory.getLogger(LoggerTests.class);

    @Test
    public void loggerTest(){
        System.out.println(logger.getName());
        logger.debug("debug log");
        logger.trace("trace log");
        logger.info("info log");
        logger.warn("warn log");
        logger.error("error log");
    }
}
