package com.xupt.nowcoder.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author yzw
 * @Date 2022-07-09 14:36 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Data
public class Comment {
    private int id;
    private int userId;
    private int entityType;
    private int entityId;
    private int targetId;
    private String content;
    private int status;
    private Date createTime;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                "userId=" + userId +
                ", entityType=" + entityType +
                ", entityId=" + entityId +
                ", targetId=" + targetId +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}
