package com.xupt.nowcoder.dao;

import com.xupt.nowcoder.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

/**
 * @Author yzw
 * @Date 2022-07-10 15:14 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Mapper
public interface MessageDao {
    public List<Message> selectMessages(@Param("fromId") int fromId,@Param("toId") int toId, int offset, int limit);

    public List<Integer> selectConversationCount(@Param("toId")int toId
            ,@Param("offset")int offset,@Param("limit")int limit);

    public Message selectNewMessage(@Param("toId")int toId,@Param("fromId")int fromId);


    public int selectMessagesCount(@Param("toId")int toId,@Param("fromId")int fromId,@Param("status") int status);

    public int selectRows(@Param("fromId") int fromId,@Param("toId") int toId);

    public int insertMessage(Message message);

    public int updateMessages(@Param("fromId") int fromId,@Param("toId") int toId,@Param("status") int status,@Param("messageId")int messageId);
}
