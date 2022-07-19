package com.xupt.nowcoder.service;

import com.xupt.nowcoder.dao.AlphaDao;
import com.xupt.nowcoder.dao.AlphaDaoMybatisImpl;
import com.xupt.nowcoder.dao.DiscussPostDao;
import com.xupt.nowcoder.dao.UserDao;
import com.xupt.nowcoder.entity.DiscussPost;
import com.xupt.nowcoder.entity.User;
import com.xupt.nowcoder.util.NowCoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

/**
 * @Author yzw
 * @Date 2022-07-02 9:50 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Service
@Scope("singleton")
public class AlphaService {

    @Autowired
    AlphaDao alphaDao;

    @Autowired
    UserDao userDao;

    @Autowired
    DiscussPostDao discussPostDao;

    @Autowired
    TransactionTemplate transactionTemplate;

    AlphaService(){
        System.out.println("实例化类");
    }

    @PostConstruct
    public void init(){
        System.out.println("初始化类");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("销毁类");}


    public String find(){
       return alphaDao.select();
    }


    /**
     * 使用事务注解的例子  模拟插入一个用户并且该用户发帖 作为一个事务
     *     REQUIRED(),有外部事务，就使用外部事务，没有才创建一个新事务
     *     REQUIRES_NEW(),直接创建一个新事务，外部事务暂停
     *     NESTED(); 创建新事物并嵌套在外部事物中
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String save1(){
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(NowCoderUtil.generateUUID().substring(0,5));
        user.setPassword(NowCoderUtil.md5("123")+user.getSalt());
        user.setStatus(1);
        user.setCreateTime(new Date());
        user.setHeaderUrl("http://localhost:8000/nowcoder/image/sdada.jpg");
        user.setType(1);
        user.setEmail("alpha@qq.com");
        userDao.insertUser(user);

        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle("Hello");
        discussPost.setContent("大家好");
        discussPostDao.insertDiscussPost(discussPost);

        Integer.valueOf("abccd");
        return "ok";
    }

    public Object save2(){
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                User user = new User();
                user.setUsername("alpha");
                user.setSalt(NowCoderUtil.generateUUID().substring(0,5));
                user.setPassword(NowCoderUtil.md5("123")+user.getSalt());
                user.setStatus(1);
                user.setCreateTime(new Date());
                user.setHeaderUrl("http://localhost:8000/nowcoder/image/sdada.jpg");
                user.setType(1);
                user.setEmail("alpha@qq.com");
                userDao.insertUser(user);

                DiscussPost discussPost = new DiscussPost();
                discussPost.setUserId(user.getId());
                discussPost.setTitle("Hello");
                discussPost.setContent("大家好");
                discussPostDao.insertDiscussPost(discussPost);

                Integer.valueOf("abccd");

                return "ok";
            }
        });

    }
}
