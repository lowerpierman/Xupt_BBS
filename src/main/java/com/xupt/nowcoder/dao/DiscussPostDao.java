package com.xupt.nowcoder.dao;

import com.xupt.nowcoder.entity.DiscussPost;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author yzw
 * @Date 2022-07-02 15:51 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Mapper
public interface DiscussPostDao {
    //为后续查询个人发布的帖子提供接口，在xml文件中使用if
    //offset 起始行行号
    //limit 数据
    List<DiscussPost> selectDiscussPosts(@Param("userId") int userId,int offset,int limit);

    //动态拼接sql时且只有一个参数 且要在if中使用 必须加Param
    //Param用来给参数起别名
    int selectDiscussPostRows(@Param("userId") int userId);

    //增加帖子
    int insertDiscussPost(DiscussPost discussPost);

    //通过标题查询帖子
    DiscussPost selectDisscussPostByTitle(@Param("title") String title);

    DiscussPost selectDiscussPostById(int id);
}
