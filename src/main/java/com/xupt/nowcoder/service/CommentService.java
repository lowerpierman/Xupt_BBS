package com.xupt.nowcoder.service;

import com.xupt.nowcoder.dao.CommentDao;
import com.xupt.nowcoder.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author yzw
 * @Date 2022-07-09 14:58 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Service
public class CommentService {
    @Autowired
    CommentDao commentDao;

    public int findCommentsRows(int entityType,int entityId){
        return  commentDao.selectCommentsRows(entityType,entityId);
    }

    public List<Comment> findComments(int entityType,int entityId, int offset, int limit){
        return commentDao.selectCommentsByEntity(entityType,entityId,offset,limit);
    }

    public int addComment(Comment comment){
        return commentDao.insertComment(comment);
    }

}
