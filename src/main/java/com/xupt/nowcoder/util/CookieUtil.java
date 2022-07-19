package com.xupt.nowcoder.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author yzw
 * @Date 2022-07-07 11:23 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Component
public class CookieUtil {
    public static String getValue(HttpServletRequest request,String name){
        if(request==null||name==null) throw new IllegalArgumentException("参数为空");
        Cookie[] cookies = request.getCookies();
        if(cookies==null) return null;
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(name)) return cookie.getValue();
        }
        return null;
    }
}
