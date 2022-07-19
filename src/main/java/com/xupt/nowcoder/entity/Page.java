package com.xupt.nowcoder.entity;

/**
 * @Author yzw
 * @Date 2022-07-02 17:22 （可以根据需要修改）
 * @Version 1.0 （版本号）
 * 封装分页
 */

public class Page {
    //均设置默认值，防止不安全的输入
    //当前页
    private int current=1;
    //显示上限
    private int limit=10;
    //数据总数
    private int rows;
    //查询路径(复用分页连接)
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if(current>=1)  this.current = current;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if(limit>=1&&limit<=100) this.limit = limit;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if(rows>=0)    this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取总页数
     * @return
     */
    public int getTotal(){
        if(rows%limit==0){
            return rows/limit;
        }else{
            return rows/limit+1;
        }
    }

    /**
     * 获取当前页的起始row
     * @return
     */
    public int getOffset(){
        return (current-1)*limit;
    }

    /**
     * 获取页段的首部，如(1-5)/(1 2 3 4 5)
     * @return
     */
    public int getFrom(){
        return current-2<1?1:current-2;
    }

    /**
     * 获取页的尾部
     *
     * @return
     */
    public int getTo(){
        return current+2>getTotal()?getTotal():current+2;
    }
}