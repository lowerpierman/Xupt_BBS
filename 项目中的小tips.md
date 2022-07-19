1.项目调试技巧
    f7  进入断点执行的语句的方法内部
    f8  执行下一条语句 从f7方法内部退回
    不行继续跟踪循环，则设置新断点，按f9 直接执行到新断点
    统一管理断点 点击breakpoints按钮
2.日志输出 log
    需要设置debug级别 logging.level.包路径=debug/...
    需要打印到txt中   logging.file=d://data/work/community.log    已经过时，目前使用logback配置 超过指定大小则分文件记录

    trace 跟踪级别
    debug 级别    开发过程中使用较多
    info  级别    在使用线程池时使用
    warn
    error