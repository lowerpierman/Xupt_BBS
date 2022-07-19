package com.xupt.nowcoder.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.thymeleaf.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

/**
 * @Author yzw
 * @Date 2022-07-05 14:38 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Component
public class NowCoderUtil {
    //提供简单的工具方法

    /**
     * 生成随机码 如图片名字等
     * @return
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

    /**
     * 对password进行md5加密
     */
    public static String md5(String key){
        if(StringUtils.isEmpty(key)) return null;
        return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
    }

    public static String getJsonString(int code, String message, Map<String,Object> map){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",message);
        if(map!=null){
            for(String key:map.keySet()){
                jsonObject.put(key,map.get(key));
            }
        }
        return jsonObject.toJSONString();
    }
    public static String getJsonString(int code,String message){
        return getJsonString(code,message,null);
    }
}
