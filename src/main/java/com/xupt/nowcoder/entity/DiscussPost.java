package com.xupt.nowcoder.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author yzw
 * @Date 2022-07-02 15:47 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Data
public class DiscussPost {
    private int id;
    private int userId ;
    private String title;
    private String content;
    private int type;
    private int status;
    private Date createTime;
    private int commentCount;
    private double score;

    @Override
    public String toString() {
        return "DiscussPost{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", createTime=" + createTime +
                ", commentCOunt=" + commentCount +
                ", score=" + score +
                '}';
    }
}
