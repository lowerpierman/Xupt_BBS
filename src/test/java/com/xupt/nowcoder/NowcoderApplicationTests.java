package com.xupt.nowcoder;

import com.xupt.nowcoder.config.AlphaConfig;
import com.xupt.nowcoder.dao.AlphaDao;
import com.xupt.nowcoder.dao.AlphaDaoHibernateImpl;
import com.xupt.nowcoder.dao.AlphaDaoMybatisImpl;
import com.xupt.nowcoder.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NowCoderApplication.class)
class NowcoderApplicationTests implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    //必须实现set方法将上下文装进去;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
    }
    @Test
    public void testApplicationContext(){
        System.out.println(applicationContext);
        AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);//通过类获取
        System.out.println(alphaDao.select());

        alphaDao = (AlphaDao) applicationContext.getBean("alphaHibernate");//通过名字获取
        System.out.println(alphaDao.select());

    }

    @Test
    public  void testBeanManange(){
        AlphaService alphaService = applicationContext.getBean(AlphaService.class);
        System.out.println(alphaService);
        alphaService = applicationContext.getBean(AlphaService.class);
        System.out.println(alphaService);
    }

    @Test
    public void testBeanConfigAndController(){
        SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
        System.out.println(simpleDateFormat.format(new Date()));
        DateFormat dateFormat = applicationContext.getBean(DateFormat.class);
        System.out.println(dateFormat.format(new Date()));
    }

}
