package com.xupt.nowcoder.service;

import com.xupt.nowcoder.dao.DiscussPostDao;
import com.xupt.nowcoder.entity.DiscussPost;
import com.xupt.nowcoder.util.SensitiveFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author yzw
 * @Date 2022-07-02 16:29 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Service
public class DiscussService {
    @Autowired
    DiscussPostDao discussPostDao;

    @Autowired
    SensitiveFilter sensitiveFilter;

    public List<DiscussPost> findDiscussPosts(int userId,int offset,int limit){
        return discussPostDao.selectDiscussPosts(userId,offset,limit);
    }

    public int findDiscussPostRows(int userId){
        return discussPostDao.selectDiscussPostRows(userId);
    }

    public int releaseDiscussPost(DiscussPost discussPost){
        if(discussPost==null) throw new IllegalArgumentException("参数无效！");
        discussPost.setTitle(HtmlUtils.htmlEscape(sensitiveFilter.filter(discussPost.getTitle())));
        discussPost.setContent(HtmlUtils.htmlEscape(sensitiveFilter.filter(discussPost.getContent())));
        return discussPostDao.insertDiscussPost(discussPost);
    }

    public DiscussPost findDiscussPostByTitle(String title){
        if(StringUtils.isBlank(title)) return null;
        return discussPostDao.selectDisscussPostByTitle(title);
    }

    public DiscussPost findDiscussPostById(int id){
        return discussPostDao.selectDiscussPostById(id);
    }
}
