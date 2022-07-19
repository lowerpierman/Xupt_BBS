package com.xupt.nowcoder;

import com.xupt.nowcoder.service.AlphaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author yzw
 * @Date 2022-07-09 14:26 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NowCoderApplication.class)
public class TtansactionTest {
    @Autowired
    AlphaService alphaService;
    @Test
    public void TransactionTest(){
        /*Object obj =  alphaService.save1();
        System.out.println(obj);*/
        Object obj = alphaService.save2();
        System.out.println(obj);
    }
}
