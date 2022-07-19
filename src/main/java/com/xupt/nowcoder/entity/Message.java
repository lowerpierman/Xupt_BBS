package com.xupt.nowcoder.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author yzw
 * @Date 2022-07-10 15:13 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Data
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String conversationId;
    private String content;
    private int status;
    private Date createTime;

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", fromId=" + fromId +
                ", toId=" + toId +
                ", conversationId=" + conversationId +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }
}
