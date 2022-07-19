package com.xupt.nowcoder.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * 0x2e80~0x9FFF是东亚文字范围
 * @Author yzw
 * @Date 2022-07-08 12:30 （可以根据需要修改）
 * @Version 1.0 （版本号）
 */
@Component
public class SensitiveFilter {
    private Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //替换符号
    private String REPLACEMENT = "***";

    //根节点
    private TrimNode root = new TrimNode();

    @PostConstruct
    public void init(){
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                ){
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String keyword;
            while ((keyword=bufferedReader.readLine())!=null){
                this.addKeyWord(keyword);
            }
        } catch (Exception e) {
            logger.error("加载敏感词文件失败："+e.getMessage());
        }

    }

    //将敏感词加入到前缀树中
    private void addKeyWord(String keyword) {
        TrimNode point = root;
        for(int i=0;i<keyword.length();i++){
            char a = keyword.charAt(i);
            TrimNode subNode = root.getSubNode(a);
            if(subNode==null){
                subNode = new TrimNode();
            }
            point.addSubNode(a,subNode);
            point=subNode;
            if(i==keyword.length()-1) point.setKeyWordEnd(true);
        }
    }
    /**
     * abc df de
     * a b c d f e
     * abbdfccav
     * 判断该字符串是否含有敏感词并替换
     * @param text
     * @return
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return  null;
        }
        StringBuilder sb = new StringBuilder();
        TrimNode point = root;
        int begin = 0;
        int post = 0;
        while(begin<text.length()){
            if(post>=text.length()) break;
            char a = text.charAt(post);
            if(isSymbol(a)){
                if(point==root){
                    sb.append(a);
                    begin++;
                }
                post++;
                continue;
            }
            point = point.getSubNode(a);
            if(point==null){
                sb.append(a);
                post = ++begin;
                point=root;
            }else if(point.isKeyWordEnd()){
                sb.append(REPLACEMENT);
                begin=++post;
                point=root;
            }else{
                post++;
            }
        }
        sb.append(text.substring(begin));
        return sb.toString();
    }

    /**
     *
     * @param c
     * @return
     */
    public boolean isSymbol(char c){
       return !CharUtils.isAsciiAlphanumeric(c) &&(c < 0x2E80 || c > 0x9FFF);
    }

    private class TrimNode{
        //该节点是否为叶子节点
        private boolean isKeyWordEnd = false;

        //该节点的后续节点
        private HashMap<Character,TrimNode> map = new HashMap<>();

        //提供访问keyword的方法
        private boolean isKeyWordEnd(){
            return isKeyWordEnd;
        }
        private void setKeyWordEnd(boolean keyWordEnd){
            isKeyWordEnd = keyWordEnd;
        }

        //添加后续节点
        private void addSubNode(Character c,TrimNode subNode){
            map.put(c,subNode);
        }

        //获取后续节点
        private TrimNode getSubNode(Character c){
            return map.get(c);
        }
    }
}
