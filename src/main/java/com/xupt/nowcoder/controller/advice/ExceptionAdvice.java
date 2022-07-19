package com.xupt.nowcoder.controller.advice;

import com.xupt.nowcoder.util.NowCoderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author yzw
 * @Date 2022-07-12 16:42 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {
    private Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler
    public void handlerException(Exception e, HttpServletResponse response, HttpServletRequest request) throws IOException {
        logger.error("服务器异常"+e.getMessage());
        for(StackTraceElement stackTraceElement:e.getStackTrace()){
            logger.error(stackTraceElement.toString());
        }
        String  xRequestedWith = request.getHeader("x-requested-with");
        if("XMLHttpRequest".equals(xRequestedWith)){
            response.setContentType("application/plain;charest=utf8");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(NowCoderUtil.getJsonString(0,"服务器异常"));
        }else{
            response.sendRedirect(request.getContextPath()
            +"/error");
        }
    }
}
