package com.xupt.nowcoder;

import com.xupt.nowcoder.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author yzw
 * @Date 2022-07-08 14:47 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NowCoderApplication.class)
public class SensitiveWordTest {
    @Autowired
    SensitiveFilter sensitiveFilter;
    private Logger logger = LoggerFactory.getLogger(SensitiveWordTest.class);
    @Test
    public void SensitiveWordTest(){
        String text = "赌博吸毒***";
        logger.debug(sensitiveFilter.filter(text));
        text = "我要赌☹☼博☹☼，我要☹☼我☹☼我☹☼我，我要☹☼吸☹☼毒☹☼";
        logger.debug(sensitiveFilter.filter(text));

    }
}
