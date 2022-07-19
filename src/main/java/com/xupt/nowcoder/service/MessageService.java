package com.xupt.nowcoder.service;

import com.xupt.nowcoder.dao.MessageDao;
import com.xupt.nowcoder.entity.Message;
import com.xupt.nowcoder.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author yzw
 * @Date 2022-07-10 15:32 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Service
public class MessageService {
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    public int findMessageRows(int fromId,int toId){
        return messageDao.selectRows(fromId,toId);
    }
    public List<Message> findMessages(int fromId,int toId,int offset,int limit) {
        return messageDao.selectMessages(fromId,toId,offset,limit);
    }

    public int findConversationCount(int toId){
        return messageDao.selectConversationCount(toId,0,Integer.MAX_VALUE).size();
    }

    public List<Integer> findFromId(int toId,int offset,int limit){
        return messageDao.selectConversationCount(toId,offset,limit);
    }
    public Message findNewMessage(int toId,int fromId){
        return messageDao.selectNewMessage(toId,fromId);
    }
    public int findUnReadMessagesCount(int toId,int fromId,int status){
        return messageDao.selectMessagesCount(toId,fromId,status);
    }
    public int findAllMesagesCount(int toId,int fromId){
        return messageDao.selectMessagesCount(toId,fromId,1)+messageDao.selectMessagesCount(toId,fromId,0);
    }

    public int addMessage(Message message){
        if(message==null) throw new RuntimeException("参数不能为空!");
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageDao.insertMessage(message);
    }

    public int ReadMessages(int fromId,int toId,int status,int messageId){
        return messageDao.updateMessages(fromId,toId,status,messageId);
    }

}
