package com.xupt.nowcoder.controller;

import com.xupt.nowcoder.service.AlphaService;
import com.xupt.nowcoder.util.NowCoderUtil;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @Author yzw
 * @Date 2022-07-01 17:46 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Controller
@RequestMapping("demo")
public class DemoController {

    @Autowired
    private AlphaService alphaService;

    @GetMapping("echo")
    @ResponseBody
    //@RequestMapping(value = "echo",method = RequestMethod.GET)
    public  String echo(){
        return "echo";
    }

    @RequestMapping(value = "alpha",method = RequestMethod.GET)
    @ResponseBody
    public String alpha(){
       return alphaService.find();
    }

    /**
     * 使用?传参
     * @param id
     * @param count
     * @return
     */
    @GetMapping(value = "students")
    @ResponseBody
    public String getStudents(
            @RequestParam(name="id",required = false,defaultValue = "1") int id,
            @RequestParam(name="count",required = false,defaultValue = "10")  int count){
        System.out.println(id+"  "+count);
        return "some students";
    }

    /**
     * 使用/ 来传值的实例方法使用了PathVariable注解来获取对应的值，使用RequestParam来保证未传入参数时仍能给予客户端响应
     * @param id
     * @return
     */
    @GetMapping(value = "student/{id}")
    @ResponseBody
    public String getStudent(
            @RequestParam(name="id",required = false,defaultValue = "100")
            @PathVariable("id")
           int id){
        System.out.println(id);
        return "a student";
    }

    /**
     * 查看http的请求参数
     * @param request
     * @param response
     */
    @GetMapping(value = "http")
    @ResponseBody
    public void getHttpHandlerName(HttpServletRequest request, HttpServletResponse response){
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while(enumeration.hasMoreElements()){
            String name = enumeration.nextElement();
            System.out.println(name+"  "+request.getHeader(name));
        }
        response.setContentType("text/html;charset=utf-8");
        try(
                PrintWriter printWriter = response.getWriter();
                ){
                printWriter.write("<h1>仿牛客论坛</h1>");
        }catch(IOException e){
                e.printStackTrace();
        }
    }

    /**
     * 使用thymeleaf来动态生成html的demo
     * @return
     */
    @GetMapping("teacher")
    public ModelAndView getTeacher(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name","杨泽伟");
        modelAndView.addObject("password","yzw19970202");
        modelAndView.setViewName("demo/view");
        return modelAndView;
    }

    /**
     * 使用Model返回到DispatcherServlet中在经过ModelAndView渲染给到客户端
     * @param model
     * @return
     */
    @GetMapping("school")
    public String getSchool(Model model){
        model.addAttribute("name","杨泽伟Model类型传参");
        model.addAttribute("password","杨泽伟Model类型传参");
        return "/demo/view";
    }

    /**
     * 使用post来接收并返回值
     * @param sname
     * @param spwd
     * @return
     */
    @PostMapping(value = "student")
    @ResponseBody
    public String saveStudent(String sname,String spwd){
        System.out.println("注册学生名字："+sname+"注册密码："+spwd);
        return "success";
    }

    @GetMapping("json")
    @ResponseBody
    public Map<String,Object> getJson(){
        Map<String,Object> map=new HashMap<>();
        map.put("name","yzwjson");
        map.put("school","xupt");
        map.put("age","23");
        return map;
    }

    @GetMapping("jsons")
    @ResponseBody
    public List<Map<String,Object>> getJsons(){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","yzwjsons");
        map.put("school","xupt");
        map.put("age","23");
        Map<String,Object> map1 = new HashMap<>();
        map1.put("name","yzwjsons");
        map1.put("school","nuc");
        map1.put("age","20");
        list.add(map);
        list.add(map1);
        return list;
    }

    /**
     * 设置cookie 的作用域以及内容以及有效期
     * @param response
     * @return
     */
    @GetMapping("cookie/set")
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("cookieCode", NowCoderUtil.generateUUID());
        cookie.setPath("/nowcoder/demo");
        cookie.setMaxAge(60*10);
        response.addCookie(cookie);
        return "set cookie";
    }

    /**
     * 获取cookie
     */
    @GetMapping("cookie/get")
    @ResponseBody
    public String getCookie(@CookieValue("cookieCode")String code, HttpServletRequest request){
        String cookie = request.getCookies().toString();
        return code;
    }

    /**
     * 设置session
     * @param session
     * @return
     */
    @GetMapping("session/set")
    @ResponseBody
    public String setSession(HttpSession session){
        session.setAttribute("name","yzw");
        return "session set";
    }

    /**
     * 获取session
     * @param session
     * @return
     */
    @GetMapping("session/get")
    @ResponseBody
    public String getSession(HttpSession session){
        return (String) session.getAttribute("name");
    }

}
