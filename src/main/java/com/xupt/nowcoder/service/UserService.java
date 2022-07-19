package com.xupt.nowcoder.service;

import com.xupt.nowcoder.dao.LoginTicketDao;
import com.xupt.nowcoder.dao.UserDao;
import com.xupt.nowcoder.entity.LoginTicket;
import com.xupt.nowcoder.entity.User;
import com.xupt.nowcoder.util.MailClient;
import com.xupt.nowcoder.util.NowCoderConsist;
import com.xupt.nowcoder.util.NowCoderUtil;
import com.xupt.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author yzw
 * @Date 2022-07-01 17:33 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Service
public class UserService extends NowCoderConsist {
    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTicketDao loginTicketDao;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    RedisTemplate redisTemplate;

    @Value("${nowcoder.path.domain}")
    private String pathName;

    @Value("${server.servlet.context-path}")
    private String projectName;


    public User findUserById(int id){
        User user = getCache(id);
        if(user==null){
            initCache(id);
        }
        user = getCache(id);
        return  user;
    }

    public Map<String,Object> register(User user){
        Map<String,Object> map = new HashMap<>();
        //传入参数有效
        if(user==null) throw new IllegalArgumentException("参数不能为空");
        if(StringUtils.isEmpty(user.getUsername())){
            map.put("usernameMessage","账户不能为空！");
            return map;
        }else if(StringUtils.isEmpty(user.getPassword())){
            map.put("passwordMessage","密码不能为空！");
            return map;
        }else if(StringUtils.isEmpty(user.getEmail())) {
            map.put("mailMessage", "邮箱不能为空！");
            return map;
        }
        //验证账户
        if(userDao.selectByName(user.getUsername())!=null){
            map.put("usernameMessage", "该用户名已存在！");
            return map;
        }
        if(userDao.selectByName(user.getEmail())!=null){
            map.put("mailMessage", "该邮箱已被注册！");
            return map;
        }

        //查验无误，可以注册
        user.setSalt(NowCoderUtil.generateUUID().substring(0,5));
        user.setPassword(NowCoderUtil.md5(user.getPassword())+user.getSalt());
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(NowCoderUtil.generateUUID());
        user.setHeaderUrl(String.format("http://static.nowcoder.com/images/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userDao.insertUser(user);
        //激活邮件的信息以及url 传入template
        Context context = new Context();
        context.setVariable("email",user.getEmail());

        String content = pathName+projectName+"/activation/"+user.getId()+"/"+user.getActivationCode();

        context.setVariable("url",content);
        String text=templateEngine.process("/mail/activation",context);
        System.out.println(text);
        //发送邮件
        mailClient.sendMail(user.getEmail(),"激活账户信息",content);
        return map;
    }

    public int activation(int userId,String code){
        if(userId==0) return ACTIVATION_FAILURE;
        User user=findUserById(userId);
        if(user.getStatus()==1) return ACTIVATION_REPEAT;
        if(user.getStatus()==0){
            if(StringUtils.isEmpty(code)) return ACTIVATION_FAILURE;
            if(userDao.selectByActivation(code)!=null){
                userDao.updateStatus(userId,1);
                return ACTIVATION_SUCCESS;
            }else return ACTIVATION_FAILURE;
        }
        return ACTIVATION_FAILURE;
    }

    public HashMap<String,Object> setLoginTicket(int userId,long expiredSeconds){
        HashMap<String,Object> map= new HashMap<String,Object>();
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        String ticket = NowCoderUtil.generateUUID();
        loginTicket.setTicket(ticket);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+expiredSeconds*1000));
        loginTicket.setStatus(0);
        String loginTieketKey = RedisKeyUtil.getUserLogin(loginTicket.getTicket());
        redisTemplate.opsForValue().set(loginTieketKey,loginTicket);
        //int status=loginTicketDao.insertLoginTicket(loginTicket);
        map.put("ticket",loginTicket.getTicket());
        return map;
    }

    public HashMap<String,Object> login(User user,String verifycode,String change,long expiredSeconds){
        HashMap<String,Object> map = new HashMap<>();
        if(user==null) throw new IllegalArgumentException("参数不能为空");
        if(StringUtils.isEmpty(verifycode)||StringUtils.isEmpty(change)){
            map.put("verifycodeMessage","验证码不能为空或获取验证码失败");
            return map;
        }
        if(!change.equalsIgnoreCase(verifycode)){
            map.put("verifycodeMessage","验证码输入错误");
            return map;
        }

        if(StringUtils.isEmpty(user.getUsername())){
            map.put("usernameMessage","用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(user.getPassword())){
            map.put("passwordMessage","密码不能为空");
            return map;
        }


        if(userDao.selectByName(user.getUsername())==null){
            map.put("usernameMessage","该用户不存在");
            return map;
        }
        User user1 = userDao.selectByName(user.getUsername());
        if(user1.getStatus()==0){
            map.put("usernameMessage","该用户不存在");
            return map;
        }
        /*System.out.println("password="+user1.getPassword());
        System.out.println("password="+user.getPassword());
        System.out.println("my_password="+NowCoderUtil.md5(user.getPassword())+user1.getSalt());*/
        if(!(user1.getPassword()).equals(NowCoderUtil.md5(user.getPassword())+user1.getSalt())){
            map.put("passwordMessage","密码输入错误");
            return map;
        }
        user.setId(user1.getId());
        map = setLoginTicket(user.getId(),expiredSeconds);
        return map;

    }

    public int logout(String ticket) {
        String redisTicketKey = RedisKeyUtil.getUserLogin(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisTicketKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisTicketKey,loginTicket);
        return 1;
    }

    public LoginTicket findByTicket(String ticket){
        String redisTicketKey = RedisKeyUtil.getUserLogin(ticket);
        return (LoginTicket)redisTemplate.opsForValue().get(redisTicketKey);
    }


    public HashMap<String, Object> updatePassword(String oldPassword, String newPassword,String ticket,User user) {
            HashMap<String,Object> map = new HashMap<>();
            if(oldPassword==null){
                map.put("oldPasswordMessage","旧密码不得为空");
                return  map;
            }
            if(newPassword==null){
                map.put("newPasswordMessage","新密码不得为空");
                return  map;
            }
            if(oldPassword.equals(newPassword)){
                map.put("newPasswordMessage","新密码不得与旧密码相同");
                return  map;
            }
            LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(RedisKeyUtil.getUserLogin(ticket));
            if(loginTicket==null){
                map.put("notFoundTicket","登录凭证不存在");
                return  map;
            }
            User user1 = findUserById(loginTicket.getUserId());
            if(!user.getPassword().equals(NowCoderUtil.md5(oldPassword)+user.getSalt())){
                map.put("oldPasswordMessage","旧密码输入错误!");
                return map;
            }
            userDao.updatePassword(user.getId(),NowCoderUtil.md5(newPassword)+user.getSalt());
            //clearCache(user.getId());
            return map;
    }

    public int updateHeaderUrl(int id,String headerUrl){
        //clearCache(id);
        return  userDao.updateHeader(id,headerUrl);
    }

    public User findByName(String username){
        return userDao.selectByName(username);
    }
    //先从Redis取数据
    public User getCache(int userId){
        String redisUserKey = RedisKeyUtil.getUserMsg(userId);
        User user = (User) redisTemplate.opsForValue().get(redisUserKey);
        return user;
    }
    //初始化缓存
    public void initCache(int userId){
        String redisUserKey = RedisKeyUtil.getUserMsg(userId);
        redisTemplate.opsForValue().set(redisUserKey,userDao.selectById(userId),3600, TimeUnit.SECONDS);
    }
    //当User数据变化 清空缓存
    public void clearCache(int userId){
        String redisUserKey = RedisKeyUtil.getUserMsg(userId);
        redisTemplate.opsForValue().set(redisUserKey,null);
    }

}
