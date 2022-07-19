package com.xupt.nowcoder.controller;

import com.xupt.nowcoder.annotation.LoginRequired;
import com.xupt.nowcoder.entity.Message;
import com.xupt.nowcoder.entity.Page;
import com.xupt.nowcoder.entity.User;
import com.xupt.nowcoder.service.MessageService;
import com.xupt.nowcoder.service.UserService;
import com.xupt.nowcoder.util.HostHolder;
import com.xupt.nowcoder.util.NowCoderUtil;
import com.xupt.nowcoder.util.ResponseStatusEnum;
import com.xupt.nowcoder.util.SensitiveFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author yzw
 * @Date 2022-07-10 15:44 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Controller
@RequestMapping("letter")
public class MessageController   {
    @Autowired
    MessageService messageService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    SensitiveFilter sensitiveFilter;

    private Logger logger = LoggerFactory.getLogger(MessageController.class);
    @LoginRequired
    @GetMapping("find")
    public String findMessages(Model model, Page page){
        //Integer.valueOf("abc");
        User user = hostHolder.getUser();
        //分页信息
        int rows = messageService.findConversationCount(user.getId());
        page.setRows(rows);
        page.setLimit(8);
        page.setPath("/letter/find");

        //会话列表查询
        //目前存在bug   未读消息显示有问题   私信列表的排序不是按照从近到远的顺序  分页之后数据显示有问题
        //07-12 解决了未读消息的显示，分页之后的数据
        List<Integer> fromIdlists = messageService.findFromId(user.getId(),page.getOffset(),page.getLimit());
        ArrayList<Map<String,Object>> res = new ArrayList<>();
        if(fromIdlists!=null){
            int count = 0;
            for(int fromId:fromIdlists){
                logger.error("fromId="+fromId);
                Map<String,Object> map = new HashMap();
                //添加发送私信的用户信息
                User fromUser = userService.findUserById(fromId);
                map.put("fromuser",fromUser);
                //添加发送私信的最新消息
                Message message = messageService.findNewMessage(user.getId(),fromId);
                map.put("message",message);
                //添加未读消息
                int unRead = messageService.findUnReadMessagesCount(user.getId(),fromUser.getId(),0);
                count+=unRead;
                map.put("unRead",unRead);
                //添加会话数
                int messages = messageService.findAllMesagesCount(user.getId(),fromId)
                        + messageService.findAllMesagesCount(fromId, user.getId());
                map.put("messages",messages);
                res.add(map);
            }
            //添加总未读消息
            model.addAttribute("count",count);
            model.addAttribute("lists",res);
        }
        return "site/letter";
    }

    @LoginRequired
    @GetMapping("detail/{fromId}")
    public String detailMessages(@PathVariable("fromId")int fromId,Model model,Page page){
            //Map<String,Object> map = new HashMap<>();
            User user = hostHolder.getUser();
            User fromuser = userService.findUserById(fromId);
            //存入私信的from用户信息
            //map.put("fromuser",fromuser);
            page.setPath("/letter/detail/"+fromId);
            page.setRows(messageService.findMessageRows(fromId,user.getId()));
            page.setLimit(5);
            //需要在html遍历的不只是message信息，还有用户信息
            List<Map<String,Object>> lists = new ArrayList<>();
            List<Message> messages = messageService.findMessages(fromId,user.getId(),page.getOffset(), page.getLimit());
            logger.error("私信对话次数"+messages.size());
            List<Integer> message_id = new ArrayList<>();
            if(messages!=null){
                for(Message message:messages){
                    if(message.getStatus()==0) message_id.add(message.getId());
                    Map<String,Object> iter = new HashMap<>();
                    iter.put("message",message);
                    iter.put("user",userService.findUserById(message.getFromId()));
                    lists.add(iter);
                }
            }
            //map.put("lists",lists);
            model.addAttribute("fromuser",fromuser);
            model.addAttribute("lists",lists);
            //需要将所有fromId发来的未读信息变为已读
            if(message_id!=null){
                for(int id:message_id){
                    messageService.ReadMessages(fromId, user.getId(),1,id);
                }
            }
            return "site/letter-detail";
    }

    @PostMapping("add")
    @ResponseBody
    public String addMessage(String toName,String content){
        if(toName==null || content==null) return NowCoderUtil.getJsonString(
                ResponseStatusEnum.FAILED.status(),ResponseStatusEnum.FAILED.msg());
         Message message = new Message();
         message.setContent(content);
         User user = userService.findByName(toName);
         if(user==null) logger.error("fail!!!:user is null");
         else logger.error("useris"+user.toString());
         if(user==null) return NowCoderUtil.getJsonString(
                 ResponseStatusEnum.FAILED.status(),ResponseStatusEnum.FAILED.msg());
         message.setToId(user.getId());
         message.setFromId(hostHolder.getUser().getId());
         message.setStatus(0);
         message.setCreateTime(new Date());
         message.setContent(message.getContent());
         if(message.getFromId()< message.getToId()){
             message.setConversationId(message.getFromId()+"_"+ message.getToId());
         }else{
             message.setConversationId(message.getToId()+"_"+ message.getFromId());
         }
         logger.error(message.getConversationId());
         int status = messageService.addMessage(message);
         if(status==1) return NowCoderUtil.getJsonString(0,ResponseStatusEnum.SUCCESS.msg() );
         return NowCoderUtil.getJsonString(ResponseStatusEnum.FAILED.status(), ResponseStatusEnum.FAILED.msg());
    }
}
