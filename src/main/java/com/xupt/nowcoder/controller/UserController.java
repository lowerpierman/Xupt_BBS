package com.xupt.nowcoder.controller;

import com.xupt.nowcoder.annotation.LoginRequired;
import com.xupt.nowcoder.entity.User;
import com.xupt.nowcoder.service.FollowService;
import com.xupt.nowcoder.service.LikeService;
import com.xupt.nowcoder.service.UserService;
import com.xupt.nowcoder.util.HostHolder;
import com.xupt.nowcoder.util.NowCoderUtil;
import com.xupt.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;

/**
 * @Author yzw
 * @Date 2022-07-01 17:33 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    FollowService followService;
    @Value("${nowcoder.path.upload}")
    private String uploadPath;

    @Value("${nowcoder.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @LoginRequired
    @GetMapping("profile/{id}")
    public String getProfile(@PathVariable("id")int id, Model model){
        User loginUser = hostHolder.getUser();
        User user = userService.findUserById(id);
        if(user==null) throw new RuntimeException("该用户不存在!");
        model.addAttribute("user",user);
        model.addAttribute("likeCount",likeService.likeUserCount(user.getId()));
        Long followCount = followService.followCount(user.getId());
        Long followedCount = followService.followedCount(user.getId());
        String followStatus = followService.followStatus(loginUser.getId(), user.getId() );
        model.addAttribute("followCount",followCount);
        model.addAttribute("followedCount",followedCount);
        model.addAttribute("followStatus",followStatus);
        return "site/profile";
    }

    @LoginRequired
    @GetMapping("setting")
    public String getSetting(){
        return "/site/setting";
    }

    Logger logger = LoggerFactory.getLogger(UserController.class);
    @LoginRequired
    @PostMapping("upload")
    public String setSetting(MultipartFile headerImage, Model model){
        if(headerImage==null){
            model.addAttribute("error","请上传图片");
            return "site/setting";
        }
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        if(StringUtils.isEmpty(suffix)){
            model.addAttribute("error","文件格式有误");
            return "site/setting";
        }
        String newFileName = NowCoderUtil.generateUUID()+suffix;
        File dest = new File(uploadPath+"/"+newFileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("ioexception:"+e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }
        User user = hostHolder.getUser();
        // http://localhost:8080/nowcoder/image/xxx.png
        String headerUrl = domain+contextPath+"/image/"+newFileName;
        userService.updateHeaderUrl(user.getId(),headerUrl);
        return "redirect:/index";
    }

    @GetMapping("/image/{filename}")
    public void getHeader(@PathVariable("filename") String filename, HttpServletResponse response){
        filename = uploadPath+filename;
        String suffix = filename.substring(filename.lastIndexOf('.'));
        response.setContentType("image/"+suffix);
        try (
                FileInputStream fileInputStream = new FileInputStream(filename);
                OutputStream outputStream = response.getOutputStream();
                ){
                byte[] buffer = new byte[1024];
                int b=0;
                while((b=fileInputStream.read(buffer))!=-1){
                    outputStream.write(buffer,0,b);
                }
        } catch (IOException e) {
            logger.error("头像输出异常"+e.getMessage());
            throw new RuntimeException("头像输出异常",e);
        }
    }

    @LoginRequired
    @PostMapping("updatePassword")
    public String updatePassword(String oldPassword, String newPassword, @CookieValue("ticket")String ticket,Model model){
        if(ticket==null){
            return "redirect:site/login";
        }
        logger.debug("oldPassword="+oldPassword);
        logger.debug("newPassword="+newPassword);
        User user = hostHolder.getUser();
        HashMap<String,Object> map=userService.updatePassword(oldPassword,newPassword,ticket,user);
        if(map==null||map.isEmpty()){
            return "redirect:/index";
        }else {
            if(map.get("notFoundTicket")!=null) return "redirect:/login";
            logger.debug("map="+map.toString());
            model.addAttribute("oldPasswordMsg",map.get("oldPasswordMessage"));
            model.addAttribute("newPasswordMsg",map.get("newPasswordMessage"));
            return "site/setting";
        }
    }
}
