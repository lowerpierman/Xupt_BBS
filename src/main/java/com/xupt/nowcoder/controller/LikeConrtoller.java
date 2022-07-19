package com.xupt.nowcoder.controller;

import com.xupt.nowcoder.entity.Comment;
import com.xupt.nowcoder.entity.DiscussPost;
import com.xupt.nowcoder.entity.User;
import com.xupt.nowcoder.service.CommentService;
import com.xupt.nowcoder.service.DiscussService;
import com.xupt.nowcoder.service.LikeService;
import com.xupt.nowcoder.util.HostHolder;
import com.xupt.nowcoder.util.NowCoderUtil;
import com.xupt.nowcoder.util.ResponseStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yzw
 * @Date 2022-07-14 11:27 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Controller
public class LikeConrtoller {
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;
    @PostMapping("/like")
    @ResponseBody
    public String like(int entityType,int entityId,int entityUserId){
        User user = hostHolder.getUser();
        DiscussPost discussPost = null;
        Comment comment = null;
        likeService.like(user.getId(), entityType,entityId,entityUserId);
        long likeCount = likeService.likeCount(entityType,entityId);
        String likeStatus = likeService.likeStatus(user.getId(), entityType,entityId);
        Map<String,Object> map = new HashMap<>();
        map.put("likeCount",likeCount);
        map.put("likeStatus",likeStatus);
        return NowCoderUtil.getJsonString(0,null,map);
    }

}
