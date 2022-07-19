package com.xupt.nowcoder.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author yzw
 * @Date 2022-07-06 18:07 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Data
public class LoginTicket {
    private int id;
    private int userId;
    private String ticket;
    private int status;
    private Date expired;

    @Override
    public String toString() {
        return "LoginTicket{" +
                "id=" + id +
                ", userId=" + userId +
                ", ticket='" + ticket + '\'' +
                ", status=" + status +
                ", expired=" + expired +
                '}';
    }
}
