package com.xupt.nowcoder.controller;

import com.xupt.nowcoder.entity.DiscussPost;
import com.xupt.nowcoder.entity.Page;
import com.xupt.nowcoder.entity.User;
import com.xupt.nowcoder.service.DiscussService;
import com.xupt.nowcoder.service.LikeService;
import com.xupt.nowcoder.service.UserService;
import com.xupt.nowcoder.util.HostHolder;
import com.xupt.nowcoder.util.NowCoderConsist;
import com.xupt.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author yzw
 * @Date 2022-07-02 16:38 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@RestController
public class HomeController {
    @Autowired
    DiscussService discussService;
    @Autowired
    UserService userService;
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;

    @GetMapping("index")
    public ModelAndView getIndexPage(Model model, Page page){
        //在Spring MVC中DispatcherServlet已经自动将Page和Model实例化
        //且将Page装入Model中
        page.setRows(discussService.findDiscussPostRows(0));
        page.setPath("index");
        List<DiscussPost> lists =  discussService.findDiscussPosts(0,page.getOffset(),page.getLimit());
        //这里使用Map比定义一个复合类去装DiscussPost和User更灵活
        List<Map<String,Object>> list = new ArrayList<>();
        if(lists!=null){
            for(DiscussPost post:lists){
                HashMap<String,Object> map= new HashMap<>();
                User user = userService.findUserById(post.getUserId());
                String redisKey = RedisKeyUtil.getEntityKey(NowCoderConsist.ENTITY_TYPE_POST,post.getId());
                long likeCount = likeService.likeCount(NowCoderConsist.ENTITY_TYPE_POST, post.getId());
                String likeStatus = likeService.likeStatus(user.getId(),NowCoderConsist.ENTITY_TYPE_POST, post.getId());
                map.put("likeCount",likeCount);
                map.put("likeStatus",likeStatus);
                map.put("post",post);
                map.put("user",user);
                list.add(map);
            }
        }
        model.addAttribute("discussposts",list);
        return new ModelAndView("index");
    }
}
