package com.xupt.nowcoder.controller;

import com.google.code.kaptcha.Producer;
import com.xupt.nowcoder.entity.User;
import com.xupt.nowcoder.service.UserService;
import com.xupt.nowcoder.util.HostHolder;
import com.xupt.nowcoder.util.NowCoderConsist;
import com.xupt.nowcoder.util.NowCoderUtil;
import com.xupt.nowcoder.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.xupt.nowcoder.util.NowCoderConsist.DEFAULT_EXPIRED_SECONDS;
import static com.xupt.nowcoder.util.NowCoderConsist.REMEMBER_EXPIRED_SECONDS;

/**
 * @Author yzw
 * @Date 2022-07-01 17:53 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${server.servlet.context-path}")
    private String path;

    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);

    @GetMapping("register")
    public ModelAndView responseRegister(){
        return new ModelAndView("site/register");
    }

    @GetMapping("login")
    public String login(){ return "site/login";}

    @GetMapping("kaptcha")
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        //生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage bufferedImage=kaptchaProducer.createImage(text);
        //将验证码存入Redis  还需要kaptcha的一个专属钥匙 放到cookie中 设置60s有效期
        String kaptchaOwner = NowCoderUtil.generateUUID();
        Cookie cookie = new Cookie("kaptchaOwner",kaptchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath(path);
        response.addCookie(cookie);
        String kaptcha = RedisKeyUtil.getKaptcha(kaptchaOwner);
        redisTemplate.opsForValue().set(kaptcha,text,60, TimeUnit.SECONDS);
        //不适用于分布式系统
        //session.setAttribute("kaptcha",text);
        //声明返回类型
        response.setContentType("image/png");
        OutputStream outputStream = null;
        try {
            outputStream= response.getOutputStream();
            ImageIO.write(bufferedImage,"png",outputStream);
        } catch (IOException e) {
            logger.error("响应验证码失败"+e.getMessage());
        }
    }

    @PostMapping("register")
    public String userRegister(Model model,User user){
        Map<String,Object> map =  userService.register(user);
        if(map==null||map.isEmpty()){
            model.addAttribute("msg","我们已经向您的邮箱发送了一封激活邮件,请尽快激活");
            model.addAttribute("url","/index");
            model.addAttribute("username",user.getUsername());
            model.addAttribute("seconds",6);
            return "site/operate-result";
        }else{
            model.addAttribute("usernamemsg",map.get("usernameMessage"));
            model.addAttribute("passwordmsg",map.get("passwordMessage"));
            model.addAttribute("emailmsg",map.get("mailMessage"));
            return "site/register";
        }
    }

    @GetMapping("activation/{id}/{code}")
    public String activationUser(Model model, @PathVariable("id")int id,
                                 @PathVariable("code")String code){
        int res=userService.activation(id,code);
        switch (res){
            case 0:model.addAttribute("msg", "激活成功,您的账号已经可以正常使用了!");
                model.addAttribute("url", "/login");break;
            case 1:model.addAttribute("msg", "无效操作,该账号已经激活过了!");
                model.addAttribute("url", "/index");break;
            case 2: model.addAttribute("msg", "激活失败,您提供的激活码不正确!");
                model.addAttribute("url", "/index");break;
        }
        return "site/operate-result";
    }

    @PostMapping("login")
    public String login(@CookieValue("kaptchaOwner")String kaptchaOwner,String username,String password,String verifycode,boolean rememberme,
                        Model model,HttpSession session,HttpServletResponse response){
       User user = new User();
       //从Redis中获取验证码原码
        //String change = (String) session.getAttribute("kaptcha");
        String change = null;
        if(StringUtils.isNotBlank(kaptchaOwner)){
            String kaptcha  = RedisKeyUtil.getKaptcha(kaptchaOwner);
             change = (String) redisTemplate.opsForValue().get(kaptcha);
        }
       user.setUsername(username);
       user.setPassword(password);
       /*System.out.println("username=="+username);
       System.out.println("password=="+password);*/
//       System.out.println("code="+verifycode);
//        System.out.println("my_code="+change);
       long expiredSeconds = rememberme?REMEMBER_EXPIRED_SECONDS:DEFAULT_EXPIRED_SECONDS;
       HashMap<String,Object> map=  userService.login(user,verifycode,change,expiredSeconds);
       if(map.get("ticket")==null){
           model.addAttribute("usernameMsg",map.get("usernameMessage"));
           model.addAttribute("passwordMsg",map.get("passwordMessage"));
           model.addAttribute("verifycodeMsg",map.get("verifycodeMessage"));
           return "site/login";
       }else{
           Cookie cookie = new Cookie("ticket",(String) map.get("ticket"));
           cookie.setPath(path);
           cookie.setMaxAge((int) expiredSeconds);
           response.addCookie(cookie);
           return "redirect:/index";
       }
    }

    @GetMapping("logout")
    public String logout(@CookieValue("ticket")String ticket){
        int status = userService.logout(ticket);
        if(status==0) return "redirect:/index";
        return "redirect:/login";
    }

}
