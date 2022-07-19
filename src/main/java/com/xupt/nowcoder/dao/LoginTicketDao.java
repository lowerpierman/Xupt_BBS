package com.xupt.nowcoder.dao;

import com.xupt.nowcoder.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @Author yzw
 * @Date 2022-07-06 18:10 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Mapper
public interface LoginTicketDao {
    //注解中可以写if，使用script来使用if
    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) "
            ,"values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);
    @Select({"select id , user_id, ticket, status, expired from login_ticket" ,
            "where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);
    @Update({
            "<script>",
            "update login_ticket set status=#{arg1}",
            "<if test=\"#{arg0}!=null\">",
            "and 1=1",
            "</if>",
            "where ticket=#{arg0}",
            "</script>"})
    int updateStatus(String ticket,int status);
}
