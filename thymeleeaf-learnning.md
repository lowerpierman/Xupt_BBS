thymeleaf模板引擎学习笔记
    1.关于${}  @{}  #{} *{}m
        ${} 获取key-value值    
            如获取User对象的username  使用时为th:utext${user.username}
            获取一个List可以使用map来获取，然后在循环过程中使用map.获取
            如获取list<user>   th:each=map:${discussposts}     th:utext${map.user.username}
            获取session域中的值   [[${sno}]]
        @{} 获取网址值可以和src和href使用  也用来提供样式路径
            th:src  th:href=@{"/index/login"}
            th:href="${/css}"
        #{} 用的较少 一般取代了标签中的值
    2. 关于html中使用thymeleaf模板
        首先需要在html标签中引用：xmlns:th="http://www.thymeleaf.org"
        在标签中使用
            2.1 th:text th:if   th:each
            2.2 @和$组合使用在href中 提供网址以及传参@{${page.path}(current=${page.total},limit=5)}
            2.3 页面样式复用th:fragment="index" th:replace="index::header"