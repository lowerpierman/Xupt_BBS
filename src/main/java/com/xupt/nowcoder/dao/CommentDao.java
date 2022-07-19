package com.xupt.nowcoder.dao;

import com.xupt.nowcoder.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author yzw
 * @Date 2022-07-09 14:35 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Mapper
public interface CommentDao {

    public int insertComment(Comment comment);


    public List<Comment> selectCommentsByEntity(int entityType,int etityId,int offset,int limit);

    public int selectCommentsRows(int entityType,int entityId);
}
