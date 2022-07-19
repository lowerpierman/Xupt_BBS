package com.xupt.nowcoder.controller;

import com.xupt.nowcoder.entity.Page;
import com.xupt.nowcoder.entity.User;
import com.xupt.nowcoder.service.FollowService;
import com.xupt.nowcoder.service.UserService;
import com.xupt.nowcoder.util.HostHolder;
import com.xupt.nowcoder.util.NowCoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author yzw
 * @Date 2022-07-14 17:06 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Controller
public class FollowController {
    @Autowired
    FollowService followService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @PostMapping("follow")
    @ResponseBody
    public String follow(String followedUserName){
        User user = hostHolder.getUser();
        User followedUser = userService.findByName(followedUserName);
        if(followedUser==null) throw  new RuntimeException("传入的用户名为空!");
        followService.follow(user.getId(), followedUser.getId());
        Map<String,Object> map = new HashMap<>();
        String followStatus = followService.followStatus(user.getId(), followedUser.getId());
        map.put("followStatus",followStatus);
        return NowCoderUtil.getJsonString(0,null,map);
    }
    @GetMapping("/follower/{id}")
    public String follow(@PathVariable("id")int id,Page page, Model model){
        User user = userService.findUserById(id);
        model.addAttribute("user",user);
        page.setRows(followService.followCount(user.getId()).intValue());
        page.setLimit(5);
        page.setPath("/follower");
        //存入关注的人 以及被多少人关注
        long followCount = followService.followCount(user.getId());
        long followedCount = followService.followedCount(user.getId());
        model.addAttribute("followCount", followCount);
        model.addAttribute("followedCount",followedCount);

        List<Map<String,Object>> followList = followService.followList(user.getId(), page.getOffset(), page.getLimit());
        model.addAttribute("followList",followList);
        return "site/follower";
    }

    @GetMapping("/followee/{id}")
    public String followwee(@PathVariable("id")int id,Page page, Model model){
        User user = userService.findUserById(id);
        model.addAttribute("user",user);
        page.setRows(followService.followedCount(user.getId()).intValue());
        page.setLimit(5);
        page.setPath("/followee");
        //存入关注的人 以及被多少人关注
        long followCount = followService.followCount(user.getId());
        long followedCount = followService.followedCount(user.getId());
        model.addAttribute("followCount", followCount);
        model.addAttribute("followedCount",followedCount);

        List<Map<String,Object>> followedList = followService.followedList(user.getId(), page.getOffset(), page.getLimit());
        model.addAttribute("followedList",followedList);
        return "site/followee";
    }

}
