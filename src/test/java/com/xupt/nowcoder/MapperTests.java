package com.xupt.nowcoder;

import com.xupt.nowcoder.dao.DiscussPostDao;
import com.xupt.nowcoder.dao.LoginTicketDao;
import com.xupt.nowcoder.dao.UserDao;
import com.xupt.nowcoder.entity.DiscussPost;
import com.xupt.nowcoder.entity.LoginTicket;
import com.xupt.nowcoder.entity.User;
import com.xupt.nowcoder.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @Author yzw
 * @Date 2022-07-02 14:52 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NowCoderApplication.class)
public class MapperTests {
    @Autowired
    private UserDao userDao;

    @Test
    public void selectUserTest(){
        /*User user = userDao.selectById(101);
        System.out.println(user.toString());
        user = userDao.selectByEmail("952012079@qq.com");
        System.out.println(user.toString());
        user = userDao.selectByName("tianchong");
        System.out.println(user.toString());*/
        System.out.println(userDao.selectByActivation("107eb2cbb8454fbe96848790e6a730b1"));
    }
    @Test
    public void insertUserTest(){
        User user = new User();
        user.setUsername("杨泽1111伟");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test11@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/104.png");
        user.setCreateTime(new Date());
        //user.setActivationCode("NATATA");

        int rows = userDao.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void updateUserTest(){
        int status1 = userDao.updateStatus(101,1);
        System.out.println(status1);
        status1 = userDao.updateHeader(101,"http://www.nowcoder.com/102.png");
        System.out.println(status1);
        status1 = userDao.updatePassword(101,"yzw1999");
        System.out.println(status1);
    }

    @Autowired
    private DiscussPostDao discussPostDao;
    @Test
    public void selectDiscussTest(){
        List<DiscussPost> list = discussPostDao.selectDiscussPosts(0,0,50);
        for(DiscussPost d:list){
            System.out.println(d.getContent());
        }
        int rows = discussPostDao.selectDiscussPostRows(0);
        System.out.println("共有"+rows+"行");
    }

    @Autowired
    private LoginTicketDao loginTicketDao;
    @Test
    public void loginTicketTest(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setTicket("dsadasda");
        loginTicket.setUserId(111);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));
        loginTicket.setStatus(1);
        /*System.out.println(loginTicketDao.insertLoginTicket(loginTicket));
        LoginTicket test = loginTicketDao.selectByTicket(loginTicket.getTicket());
        System.out.println("test="+test.toString());*/
        int status=loginTicketDao.updateStatus(loginTicket.getTicket(),0);
        System.out.println("status="+status);
    }

    @Autowired
    MessageService messageService;
    @Test
    public void messageTests(){/*
        System.out.println("rows="+messageService.findMessageRows(112));
        System.out.println("size="+messageService.findMessages(112,1,10));
        System.out.println("size="+messageService.findConversationCount(1));*/
        /*System.out.println(messageService.findNewMessage(111,112).getContent());
        System.out.println(messageService.findAllMesagesCount(111,112));*/
        System.out.println(messageService.findMessageRows(111,145));
    }
}
