package com.xupt.nowcoder.controller;

import com.xupt.nowcoder.entity.Comment;
import com.xupt.nowcoder.entity.Page;
import com.xupt.nowcoder.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 注意：无论是贴中回帖还是直接针对该贴回帖 entityId都是本次遍历下的回复的id！！！
 * 只有直接回复楼主的帖子用的才是discuss的id
 * 也注意 贴中回帖对应的用户id可以根据rep.user.id获取 也可以根据rep.comment.userId获取
 * @Author yzw
 * @Date 2022-07-09 15:00 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Controller
@RequestMapping("comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("add")
    public String addComment(Model model, Page page){

        return "";
    }
}
