package com.xupt.nowcoder.dao;

import com.xupt.nowcoder.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import javax.jws.soap.SOAPBinding;

/**
 * @Author yzw
 * @Date 2022-07-01 18:00 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Mapper
public interface UserDao {
    public User selectById(int id);
    public User selectByName(String username);
    public User selectByEmail(String Email);
    public String selectByActivation(String activationCode);
    public int insertUser(User user);
    public int updateStatus(int id,int status);
    public int updateHeader(int id,String headerUrl);
    int updatePassword(int id,String password);

    @Update({"update user set header_url = #{arg1} where id = #{arg0}"})
    int updateHeaderUrl(int id,String headerUrl);

}
