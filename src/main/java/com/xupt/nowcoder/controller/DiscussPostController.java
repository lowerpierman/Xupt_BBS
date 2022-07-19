package com.xupt.nowcoder.controller;

import com.xupt.nowcoder.annotation.LoginRequired;
import com.xupt.nowcoder.entity.Comment;
import com.xupt.nowcoder.entity.DiscussPost;
import com.xupt.nowcoder.entity.Page;
import com.xupt.nowcoder.entity.User;
import com.xupt.nowcoder.service.CommentService;
import com.xupt.nowcoder.service.DiscussService;
import com.xupt.nowcoder.service.LikeService;
import com.xupt.nowcoder.service.UserService;
import com.xupt.nowcoder.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author yzw
 * @Date 2022-07-08 15:47 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Controller
@RequestMapping("discuss")
public class DiscussPostController {
    @Autowired
    DiscussService discussService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;

    @LoginRequired
    @PostMapping("add")
    @ResponseBody
    public String addDiscussPost(String title,String content){
        User user = hostHolder.getUser();
        if(user==null) return NowCoderUtil.getJsonString(ResponseStatusEnum.NO_AUTH.status(), ResponseStatusEnum.NO_AUTH.msg());
        DiscussPost discussPost = new DiscussPost();
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setUserId(user.getId());
        discussPost.setStatus(0);
        discussPost.setType(0);
        discussPost.setCreateTime(new Date());
        discussPost.setCommentCount(0);
        discussPost.setScore(0.0);
        int code = discussService.releaseDiscussPost(discussPost);
        //报错将来统一处理
        return NowCoderUtil.getJsonString(ResponseStatusEnum.SUCCESS.status(), ResponseStatusEnum.SUCCESS.msg());
    }

    @LoginRequired
    @GetMapping("detail/{id}")
    public String detailDiscussPost(@PathVariable("id") int id, Model model, Page page){

        DiscussPost discussPost = discussService.findDiscussPostById(id);
        model.addAttribute("discusspost",discussPost);


        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user",user);

        //帖子的点赞量及状态
        String redisKey = RedisKeyUtil.getEntityKey(discussPost.getId(),NowCoderConsist.ENTITY_TYPE_POST);
        long likeCount = likeService.likeCount(NowCoderConsist.ENTITY_TYPE_POST,discussPost.getId());
        String likeStatus = likeService.likeStatus(user.getId(),NowCoderConsist.ENTITY_TYPE_POST,discussPost.getId());
        model.addAttribute("likeCount",likeCount);
        model.addAttribute("likeStatus",likeStatus);
        //这样更低效，表中已经设置好了评论总数comment_count
        //page.setRows(commentService.findCommentsRows(discussPost.getType(),discussPost.getId()));
        page.setRows(discussPost.getCommentCount());
        page.setPath("/discuss/detail/"+id);
        page.setLimit(8);
        List<Comment> lists = commentService.findComments(NowCoderConsist.ENTITY_TYPE_POST,
                discussPost.getId(), page.getOffset(),page.getLimit());
        //评论VO列表
        List<Map<String,Object>> CommentVo = new ArrayList<>();
        if(lists!=null){
            for(Comment comment:lists){
                Map<String,Object> map = new HashMap<>();
                user = userService.findUserById(comment.getUserId());
                //帖子评论的作者
                map.put("user",user);
                //帖子的评论
                map.put("comment",comment);
                //帖子评论的点赞及状态
                redisKey = RedisKeyUtil.getEntityKey(comment.getEntityType(),comment.getEntityId());
                likeCount = likeService.likeCount(NowCoderConsist.ENTITY_TYPE_COMMENT,comment.getId());
                likeStatus = likeService.likeStatus(user.getId(), NowCoderConsist.ENTITY_TYPE_COMMENT,comment.getId());
                map.put("likeCount",likeCount);
                map.put("likeStatus",likeStatus);
                //将评论的评论存入结果集
                List<Comment> list_replays = commentService.findComments(NowCoderConsist.ENTITY_TYPE_COMMENT
                ,comment.getId(),0,Integer.MAX_VALUE);
                List<Map<String,Object>> ReplayVo = new ArrayList<>();
                if(list_replays!=null){
                    for(Comment comment1:list_replays){
                        HashMap<String,Object> map1 = new HashMap<>();
                        user = userService.findUserById(comment1.getUserId());
                        //评论的评论的作者
                        map1.put("re_user",user);
                        //评论的评论的点赞量及状态
                        redisKey = RedisKeyUtil.getEntityKey(comment1.getEntityType(),comment1.getEntityId());
                        likeCount =  likeService.likeCount(NowCoderConsist.ENTITY_TYPE_COMMENT,comment1.getId());
                        likeStatus = likeService.likeStatus(user.getId(),NowCoderConsist.ENTITY_TYPE_COMMENT ,comment1.getId());
                        map1.put("likeCount",likeCount);
                        map1.put("likeStatus",likeStatus);
                        //评论的评论
                        map1.put("replay",comment1);
                        //该评论的评论是跟帖还是回复评论的评论
                        User target = userService.findUserById(comment1.getTargetId());
                        map1.put("target",target);
                        ReplayVo.add(map1);
                    }
                }
                map.put("replaylist",ReplayVo);

                //回复的数量
                int replatCount = commentService.findCommentsRows(NowCoderConsist.ENTITY_TYPE_COMMENT
                        , discussPost.getId());
                map.put("replayCount",replatCount);
                CommentVo.add(map);
            }
        }
        model.addAttribute("CommentVo",CommentVo);

        return "site/discuss-detail";
    }
}
